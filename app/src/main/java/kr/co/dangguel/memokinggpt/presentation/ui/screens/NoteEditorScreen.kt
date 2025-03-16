package kr.co.dangguel.memokinggpt.presentation.ui.screens

import android.net.Uri
import android.provider.MediaStore
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
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    navController: NavController,
    noteId: Long?
) {
    val context = LocalContext.current
    var showOcrDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("ko") } // ✅ OCR 언어 선택
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) // ✅ BottomSheet 상태 관리
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        noteId?.let { viewModel.loadNote(it) }
    }

    val noteTitle by viewModel.title.collectAsState()
    val noteText by viewModel.text.collectAsState()
    val ocrResult by viewModel.ocrResult.collectAsState() // ✅ OCR 결과 상태값

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            viewModel.processOcrImage(context, uri) // ✅ OCR 실행
            showBottomSheet = true // ✅ OCR 실행 후 BottomSheet 표시
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri?.let {
                viewModel.processOcrImage(context, it)
                showBottomSheet = true // ✅ OCR 실행 후 BottomSheet 표시
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = noteTitle,
                        onValueChange = viewModel::updateTitle,
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                if (noteTitle.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.enter_note_title),
                                        style = MemoKingTypography.labelLarge
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { showLanguageDialog = true }) { // ✅ OCR 실행 전에 언어 선택 다이얼로그 표시
                        Icon(Icons.Default.Camera, contentDescription = "OCR 실행")
                    }
                    IconButton(onClick = {
                        viewModel.saveNote(noteId)
                        navController.popBackStack()
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
            BasicTextField(
                value = noteText,
                onValueChange = viewModel::updateText,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // 🔹 OCR 언어 선택 다이얼로그
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.ocr_select_language)) },
            text = { Text(stringResource(R.string.ocr_select_language_description)) },
            confirmButton = {
                TextButton(onClick = {
                    selectedLanguage = "ko" // ✅ 한국어 선택
                    showLanguageDialog = false
                    showOcrDialog = true // ✅ OCR 선택 다이얼로그 표시
                }) {
                    Text(stringResource(R.string.korean))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    selectedLanguage = "en" // ✅ 영어 선택
                    showLanguageDialog = false
                    showOcrDialog = true // ✅ OCR 선택 다이얼로그 표시
                }) {
                    Text(stringResource(R.string.english))
                }
            }
        )
    }

    // 🔹 OCR 이미지 선택 다이얼로그
    if (showOcrDialog) {
        AlertDialog(
            onDismissRequest = { showOcrDialog = false },
            title = { Text(stringResource(R.string.ocr_dialog_title)) },
            text = { Text(stringResource(R.string.ocr_dialog_message)) },
            confirmButton = {
                Button(onClick = {
                    showOcrDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Text(stringResource(R.string.ocr_gallery_button))
                }
            },
            dismissButton = {
                Button(onClick = {
                    showOcrDialog = false
                    imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null)
                    cameraLauncher.launch(imageUri ?: Uri.EMPTY) // ✅ null 방지
                }) {
                    Icon(Icons.Default.Camera, contentDescription = null)
                    Text(stringResource(R.string.ocr_camera_button))
                }
            }
        )
    }

    // 🔹 OCR 결과 BottomSheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.ocr_result),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                var editedText by remember { mutableStateOf(ocrResult) } // ✅ OCR 결과 수정 가능하도록 상태 관리
                BasicTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { showBottomSheet = false }) {
                        Text(stringResource(R.string.close))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        viewModel.updateText(editedText) // ✅ OCR 결과를 노트에 반영
                        showBottomSheet = false
                    }) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }
        }
    }
}
