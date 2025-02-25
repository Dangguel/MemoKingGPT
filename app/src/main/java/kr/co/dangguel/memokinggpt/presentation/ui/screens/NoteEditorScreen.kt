package kr.co.dangguel.memokinggpt.presentation.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.presentation.viewmodel.NoteEditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    onBackClick: () -> Unit,
    onSaveClick: (String) -> Unit
) {
    val context = LocalContext.current
    var showOcrDialog by remember { mutableStateOf(false) }

    // 📌 갤러리 런처 (이미지 선택)
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.handleGalleryResult(context, uri)
    }

    // 📌 카메라 런처 (사진 촬영)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        viewModel.handleCameraResult(context, success)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("노트 편집") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { showOcrDialog = true }) {
                        Icon(Icons.Default.Camera, contentDescription = "OCR 기능")
                    }
                    IconButton(onClick = { onSaveClick(viewModel.text.value) }) {
                        Icon(Icons.Default.Check, contentDescription = "저장")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            BasicTextField(
                value = viewModel.text.value,
                onValueChange = viewModel::updateText
            )
        }

        // 📌 OCR 선택 다이얼로그
        if (showOcrDialog) {
            AlertDialog(
                onDismissRequest = { showOcrDialog = false },
                title = { Text("사진 속 글자 인식") },
                text = { Text("사진을 찍거나 갤러리에서 선택하여 텍스트를 추출하세요.") },
                confirmButton = {
                    Button(onClick = {
                        showOcrDialog = false
                        viewModel.launchCamera(cameraLauncher) // ✅ 카메라 실행
                    }) {
                        Icon(Icons.Default.Camera, contentDescription = null)
                        Text("카메라로 촬영")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showOcrDialog = false
                        viewModel.pickImageFromGallery(galleryLauncher) // ✅ 갤러리 실행
                    }) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Text("갤러리에서 선택")
                    }
                }
            )
        }
    }
}
