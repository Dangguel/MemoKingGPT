package kr.co.dangguel.memokinggpt.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

fun extractTextFromImage(context: Context, uri: Uri, onSuccess: (String) -> Unit) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = InputImage.fromFilePath(context, uri)

    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            onSuccess(visionText.text)
        }
        .addOnFailureListener { e ->
            Log.e("OCR", "텍스트 인식 실패: ${e.localizedMessage}")
        }
}

fun extractTextFromImage(context: Context, bitmap: Bitmap, onSuccess: (String) -> Unit) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = InputImage.fromBitmap(bitmap, 0)

    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            onSuccess(visionText.text)
        }
        .addOnFailureListener { e ->
            Log.e("OCR", "텍스트 인식 실패: ${e.localizedMessage}")
        }
}
