package kr.co.dangguel.memokinggpt.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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

    fun updateText(newText: String) {
        text.value = newText
    }

    /**
     * 📌 ML Kit을 이용한 OCR 처리 함수
     * - 갤러리 또는 카메라에서 가져온 이미지를 분석하여 텍스트 추출
     */
    fun processOcr(context: Context, uri: Uri) {
        viewModelScope.launch {
            val extractedText = ocrUseCase.extractTextFromImage(context, uri)
            text.value += "\n$extractedText"
        }
    }

    /**
     * 📌 카메라 실행 함수
     * - 사진 촬영 후 OCR 실행
     */
    fun launchCamera(cameraLauncher: ActivityResultLauncher<Uri>) {
        val imageUri = getNewImageUri()
        _imageUri = imageUri
        cameraLauncher.launch(imageUri)
    }

    /**
     * 📌 갤러리에서 이미지 선택
     * - 사용자가 선택한 이미지를 OCR 처리
     */
    fun pickImageFromGallery(galleryLauncher: ActivityResultLauncher<String>) {
        galleryLauncher.launch("image/*")
    }

    /**
     * 📌 촬영한 이미지를 저장할 빈 URI 생성
     * - MediaStore를 사용하여 새로운 이미지 URI 생성
     */
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

    /**
     * 📌 촬영된 이미지 OCR 실행
     * - 카메라에서 촬영된 이미지를 OCR 처리
     */
    fun handleCameraResult(context: Context, success: Boolean) {
        if (success && _imageUri != null) {
            processOcr(context, _imageUri!!)
        }
    }

    /**
     * 📌 갤러리에서 선택한 이미지 OCR 실행
     * - 갤러리에서 선택한 이미지를 OCR 처리
     */
    fun handleGalleryResult(context: Context, uri: Uri?) {
        uri?.let { processOcr(context, it) }
    }
}
