package kr.co.dangguel.memokinggpt.presentation.ui.screens

import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.presentation.ads.AdManager
import kr.co.dangguel.memokinggpt.presentation.ui.components.ResultBottomSheet
import kr.co.dangguel.memokinggpt.presentation.util.InAppReviewManager
import kr.co.dangguel.memokinggpt.presentation.viewmodel.NoteEditorViewModel
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    navController: NavController,
    noteId: Long?,
    folderId: Long?
) {
    val context = LocalContext.current
    var showOcrDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("ko") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isOcrLoading by viewModel.isOcrLoading.collectAsState()
    val isSummaryLoading by viewModel.isSummaryLoading.collectAsState()
    var showSummaryDialog by remember { mutableStateOf(false) } // ✅ 요약 다이얼로그 표시 여부
    var selectedSummaryType by remember { mutableStateOf("") } // ✅ 선택된 요약 타입
    var showOcrBottomSheet by remember { mutableStateOf(false) }
    var showSummaryBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        noteId?.let { viewModel.loadNote(it) }
    }

    val noteTitle by viewModel.title.collectAsState()
    val noteText by viewModel.text.collectAsState()
    val ocrResult by viewModel.ocrResult.collectAsState()
    val summaryResult by viewModel.summaryResult.collectAsState() // ✅ 요약 결과
    val showAdTrigger by viewModel.showAdTrigger.collectAsState()

    LaunchedEffect(showAdTrigger) {
        if (showAdTrigger) {
            AdManager.showAd(context as Activity)
            viewModel.resetAdTrigger()  // ✅ 트리거 리셋 함수 따로 만들어주기
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            viewModel.processOcrImage(context, it)
            showOcrBottomSheet = true
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri?.let {
                viewModel.processOcrImage(context, it)
                showOcrBottomSheet = true
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imageUri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues().apply {
                    put(MediaStore.Images.Media.TITLE, "New Picture")
                    put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
                }
            )
            cameraLauncher.launch(imageUri ?: Uri.EMPTY)
        } else {
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
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
                        textStyle = MemoKingTypography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.padding(vertical = 4.dp)) {
                                if (noteTitle.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.enter_note_title),
                                        style = MemoKingTypography.labelLarge.copy(
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                        )
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
                    IconButton(onClick = { showLanguageDialog = true }) {
                        Icon(Icons.Default.Camera, contentDescription = "OCR 실행")
                    }
                    IconButton(onClick = { showSummaryDialog = true }) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "요약하기")
                    }
                    IconButton(onClick = {
                        viewModel.saveNote(noteId,folderId)
                        val activity = context as? Activity
                        activity?.let { InAppReviewManager.launchReviewFlow(it) }
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "저장")
                    }
                }
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState)
                .imePadding() // ✅ 키보드가 올라왔을 때 가려지지 않게 처리
        ) {
            BasicTextField(
                value = noteText,
                onValueChange = viewModel::updateText,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), // 스크롤 안에서 길어질 수 있도록
                textStyle = MemoKingTypography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.ocr_select_language)) },
            text = { Text(stringResource(R.string.ocr_select_language_description)) },
            confirmButton = {
                TextButton(onClick = {
                    selectedLanguage = "ko"
                    viewModel.setOcrLanguage("ko")
                    showLanguageDialog = false
                    showOcrDialog = true
                }) {
                    Text(stringResource(R.string.korean))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    selectedLanguage = "en"
                    viewModel.setOcrLanguage("en")
                    showLanguageDialog = false
                    showOcrDialog = true
                }) {
                    Text(stringResource(R.string.english))
                }
            }
        )
    }

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
                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    } else {
                        imageUri = context.contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            ContentValues().apply {
                                put(MediaStore.Images.Media.TITLE, "New Picture")
                                put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
                            }
                        )
                        cameraLauncher.launch(imageUri ?: Uri.EMPTY)
                    }
                }) {
                    Icon(Icons.Default.Camera, contentDescription = null)
                    Text(stringResource(R.string.ocr_camera_button))
                }
            }
        )
    }

    if (isOcrLoading || isSummaryLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (showSummaryDialog) {
        AlertDialog(
            onDismissRequest = { showSummaryDialog = false },
            confirmButton = { // 확인 버튼 대신 내부 Column 으로 전체 구현
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = stringResource(R.string.summary_dialog_title),
                        style = MemoKingTypography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.summary_dialog_message),
                        style = MemoKingTypography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    val resources = context.resources
                    val summaryOptions = listOf(
                        resources.getString(R.string.summary_core_title) to resources.getString(R.string.summary_core_desc),
                        resources.getString(R.string.summary_full_title) to resources.getString(R.string.summary_full_desc),
                        resources.getString(R.string.summary_list_title) to resources.getString(R.string.summary_list_desc),
                        resources.getString(R.string.summary_action_title) to resources.getString(R.string.summary_action_desc)
                    )
                    summaryOptions.forEach { (type, description) ->
                        OutlinedButton(
                            onClick = {
                                showSummaryDialog = false
                                viewModel.requestSummary(context, type)
                                showSummaryBottomSheet = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = type, style = MemoKingTypography.labelLarge)
                                Text(
                                    text = description,
                                    style = MemoKingTypography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { showSummaryDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        )
    }

    if (showOcrBottomSheet) {
        ResultBottomSheet(
            title = stringResource(R.string.ocr_result),
            initialContent = ocrResult,
            onApply = { resultText ->
                val currentText = viewModel.text.value
                val newText = if (currentText.isNotBlank()) "$currentText\n\n$resultText" else resultText
                viewModel.updateText(newText)
            },
            onDismiss = { showOcrBottomSheet = false }
        )
    }

    if (showSummaryBottomSheet) {
        ResultBottomSheet(
            title = stringResource(R.string.summary_result),
            initialContent = summaryResult,
            onApply = { resultText ->
                val currentText = viewModel.text.value
                val newText = if (currentText.isNotBlank()) "$currentText\n\n$resultText" else resultText
                viewModel.updateText(newText)
            },
            onDismiss = { showSummaryBottomSheet = false }
        )
    }
}
