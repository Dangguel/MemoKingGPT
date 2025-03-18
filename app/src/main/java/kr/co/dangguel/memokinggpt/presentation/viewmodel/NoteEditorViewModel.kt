package kr.co.dangguel.memokinggpt.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity
import kr.co.dangguel.memokinggpt.data.remote.model.GptMessage
import kr.co.dangguel.memokinggpt.data.remote.model.GptRequest
import kr.co.dangguel.memokinggpt.domain.usecase.GptUseCase
import kr.co.dangguel.memokinggpt.domain.usecase.NoteUseCase
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase,
    private val gptUseCase: GptUseCase
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("ko")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _ocrResult = MutableStateFlow("")
    val ocrResult = _ocrResult.asStateFlow()

    private val _isOcrLoading = MutableStateFlow(false)
    val isOcrLoading = _isOcrLoading.asStateFlow()

    private val _gptResult = MutableStateFlow("")
    val gptResult = _gptResult.asStateFlow()

    private val _isSummaryLoading = MutableStateFlow(false)
    val isSummaryLoading = _isSummaryLoading.asStateFlow()

    private val _summaryResult = MutableStateFlow("")
    val summaryResult = _summaryResult.asStateFlow()

    private var currentNoteId: Long? = null

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateText(newText: String) {
        _text.value = newText
    }

    fun setOcrLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            val note = noteUseCase.getNoteById(noteId)
            note?.let {
                _title.value = it.title
                _text.value = it.content
                currentNoteId = noteId
            }
        }
    }

    fun saveNote(noteId: Long?, folderId: Long?) {
        viewModelScope.launch {
            if (noteId == null) {
                noteUseCase.insertNote(
                    NoteEntity(
                        title = _title.value,
                        content = _text.value,
                        folderId = if (folderId == -1L) null else folderId
                    )
                )
            } else {
                noteUseCase.updateNote(
                    NoteEntity(
                        id = noteId,
                        title = _title.value,
                        content = _text.value,
                        folderId = if (folderId == -1L) null else folderId
                    )
                )
            }
        }
    }

    fun processOcrImage(context: Context, imageUri: Uri) {
        val recognizer = if (_selectedLanguage.value == "ko") {
            TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
        } else {
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        }

        _isOcrLoading.value = true

        try {
            val image = InputImage.fromFilePath(context, imageUri)
            recognizer.process(image)
                .addOnSuccessListener { result ->
                    viewModelScope.launch {
                        _ocrResult.value = result.text
                        _isOcrLoading.value = false
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, context.getString(R.string.ocr_failed), Toast.LENGTH_SHORT).show()
                    _isOcrLoading.value = false
                }
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.ocr_failed), Toast.LENGTH_SHORT).show()
            _isOcrLoading.value = false
        }
    }

    fun requestSummary(context: Context, summaryType: String) {
        viewModelScope.launch {
            _isSummaryLoading.value = true
            val language = Locale.getDefault().language
            val content = _text.value
            val resources = context.resources

            val coreSummary = resources.getString(R.string.core_summary)
            val fullSummary = resources.getString(R.string.full_summary)
            val listConversion = resources.getString(R.string.list_conversion)
            val actionPlan = resources.getString(R.string.action_plan)

            val prompt = when (summaryType) {
                coreSummary -> resources.getString(R.string.prompt_core_summary, content)
                fullSummary -> resources.getString(R.string.prompt_full_summary, content)
                listConversion -> resources.getString(R.string.prompt_list_conversion, content)
                actionPlan -> resources.getString(R.string.prompt_action_plan, content)
                else -> content
            }

            try {
                val response = gptUseCase.getSummary(
                    GptRequest(
                        messages = listOf(GptMessage(content = prompt))
                    )
                )
                _summaryResult.value = response.choices.firstOrNull()?.message?.content ?: ""
            } catch (e: Exception) {
                Toast.makeText(context, "GPT 요약 요청 실패", Toast.LENGTH_SHORT).show()
            } finally {
                _isSummaryLoading.value = false
            }
        }
    }
}
