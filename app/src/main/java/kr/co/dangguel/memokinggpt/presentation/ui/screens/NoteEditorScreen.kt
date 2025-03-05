package kr.co.dangguel.memokinggpt.presentation.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.presentation.viewmodel.NoteEditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    navController: NavController,
    noteId: Long? // ✅ 노트 ID 추가
) {
    val context = LocalContext.current
    var showOcrDialog by remember { mutableStateOf(false) }

    // ✅ 기존 노트 로드
    LaunchedEffect(noteId) {
        noteId?.let { viewModel.loadNote(it) }
    }

    // ✅ 상태 값들 (노트 제목 & 내용)
    val noteTitle by viewModel.title.collectAsState()
    val noteText by viewModel.text.collectAsState()

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
                title = { Text(noteTitle.ifEmpty { stringResource(R.string.new_note) }) }, // ✅ 제목이 없으면 "새 노트" 표시
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { showOcrDialog = true }) {
                        Icon(Icons.Default.Camera, contentDescription = "OCR 기능")
                    }
                    IconButton(onClick = {
                        viewModel.saveNote(noteId)
                        navController.popBackStack() // 저장 후 뒤로가기
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "저장")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // ✅ 제목 입력 필드 추가
            OutlinedTextField(
                value = noteTitle ?: "",  // ✅ Null 방지
                onValueChange = viewModel::updateTitle,
                label = { Text(stringResource(R.string.new_note)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 본문 내용 입력 필드
            BasicTextField(
                value = noteText ?: "", // ✅ Null 방지
                onValueChange = viewModel::updateText,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 📌 OCR 선택 다이얼로그
        if (showOcrDialog) {
            AlertDialog(
                onDismissRequest = { showOcrDialog = false },
                title = { Text(stringResource(R.string.ocr_dialog_title)) },
                text = { Text(stringResource(R.string.ocr_dialog_message)) },
                confirmButton = {
                    Button(onClick = {
                        showOcrDialog = false
                        cameraLauncher.launch(Uri.EMPTY) // ✅ 카메라 실행
                    }) {
                        Icon(Icons.Default.Camera, contentDescription = null)
                        Text(stringResource(R.string.ocr_camera_button))
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showOcrDialog = false
                        galleryLauncher.launch("image/*") // ✅ 갤러리 실행
                    }) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Text(stringResource(R.string.ocr_gallery_button))
                    }
                }
            )
        }
    }
}
