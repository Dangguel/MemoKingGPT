package kr.co.dangguel.memokinggpt.domain.usecase

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@ViewModelScoped
class OcrUseCase @Inject constructor() {

    suspend fun extractTextFromImage(context: Context, uri: Uri): String {
        return try {
            val image = InputImage.fromFilePath(context, uri)

            // ✅ 한글 & 영어만 인식하도록 설정
            val recognizer = TextRecognition.getClient(
                TextRecognizerOptions.Builder()
                    .build()
            )

            suspendCancellableCoroutine { continuation ->
                recognizer.process(image)
                    .addOnSuccessListener { result ->
                        continuation.resume(result.text)
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
