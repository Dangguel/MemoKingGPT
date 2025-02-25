package kr.co.dangguel.memokinggpt.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.co.dangguel.memokinggpt.domain.usecase.OcrUseCase
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val ocrUseCase: OcrUseCase,
    private val app: Application
) : AndroidViewModel(app) {

    var text = mutableStateOf("")
        private set

    private var _imageUri: Uri? = null
    private var _noteId: Long? = null

    fun setNoteId(noteId: Long?) {
        _noteId = noteId
    }

    fun updateText(newText: String) {
        text.value = newText
    }

    fun processOcr(context: Context, uri: Uri) {
        viewModelScope.launch {
            val extractedText = ocrUseCase.extractTextFromImage(context, uri)
            text.value += "\n$extractedText"
        }
    }

    fun launchCamera(cameraLauncher: ActivityResultLauncher<Uri>) {
        val imageUri = getNewImageUri()
        _imageUri = imageUri
        cameraLauncher.launch(imageUri)
    }

    fun pickImageFromGallery(galleryLauncher: ActivityResultLauncher<String>) {
        galleryLauncher.launch("image/*")
    }

    private fun getNewImageUri(): Uri {
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "ocr_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        return app.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: throw IllegalStateException("이미지를 저장할 수 없습니다.")
    }

    fun handleCameraResult(context: Context, success: Boolean) {
        if (success && _imageUri != null) {
            processOcr(context, _imageUri!!)
        }
    }

    fun handleGalleryResult(context: Context, uri: Uri?) {
        uri?.let { processOcr(context, it) }
    }
}
