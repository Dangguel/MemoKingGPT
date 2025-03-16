package kr.co.dangguel.memokinggpt.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity
import kr.co.dangguel.memokinggpt.domain.usecase.NoteUseCase
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("ko") // 기본값: 한국어
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _ocrResult = MutableStateFlow("")
    val ocrResult = _ocrResult.asStateFlow()

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

    fun saveNote(noteId: Long?) {
        viewModelScope.launch {
            if (noteId == null) {
                noteUseCase.insertNote(
                    NoteEntity(
                        title = _title.value,
                        content = _text.value
                    )
                )
            } else {
                noteUseCase.updateNote(
                    NoteEntity(
                        id = noteId,
                        title = _title.value,
                        content = _text.value
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

        try {
            val image = com.google.mlkit.vision.common.InputImage.fromFilePath(context, imageUri)
            recognizer.process(image)
                .addOnSuccessListener { result ->
                    _ocrResult.value = result.text
                }
                .addOnFailureListener {
                    Toast.makeText(context, context.getString(R.string.ocr_failed), Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.ocr_failed), Toast.LENGTH_SHORT).show()
        }
    }
}
