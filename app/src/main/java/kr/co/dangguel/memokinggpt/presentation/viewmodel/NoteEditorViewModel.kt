package kr.co.dangguel.memokinggpt.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity
import kr.co.dangguel.memokinggpt.domain.usecase.NoteUseCase
import javax.inject.Inject

class NoteEditorViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateText(newText: String) {
        _text.value = newText
    }

    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            val note = noteUseCase.getNoteById(noteId)
            note?.let {
                _title.value = it.title
                _text.value = it.content
            }
        }
    }

    fun saveNote(noteId: Long?) {
        viewModelScope.launch {
            if (noteId == null) {
                // 새 노트 저장
                noteUseCase.insertNote(
                    NoteEntity(
                        title = _title.value,
                        content = _text.value
                    )
                )
            } else {
                // 기존 노트 업데이트
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

    fun handleGalleryResult(context: Context, uri: Uri?) {
        uri?.let {
            _text.value += "\n[이미지 추가됨: $uri]"
        } ?: Toast.makeText(context, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
    }

    fun handleCameraResult(context: Context, success: Boolean) {
        if (success) {
            _text.value += "\n[카메라 촬영 완료]"
        } else {
            Toast.makeText(context, "카메라 촬영 실패", Toast.LENGTH_SHORT).show()
        }
    }
}
