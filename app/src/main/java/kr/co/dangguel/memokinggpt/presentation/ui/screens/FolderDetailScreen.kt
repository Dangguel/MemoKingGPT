package kr.co.dangguel.memokinggpt.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.presentation.ui.components.AdBanner
import kr.co.dangguel.memokinggpt.presentation.ui.components.NoteListItem
import kr.co.dangguel.memokinggpt.presentation.ui.components.TopBar
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingTypography

@Composable
fun FolderDetailScreen(
    navController: NavController,
    viewModel: MainViewModel,
    folderId: Long,
    onNoteClick: (Long) -> Unit,  // ✅ 추가
    onDeleteNoteClick: (Long) -> Unit // ✅ 추가
) {
    val context = LocalContext.current
    val notes = viewModel.notes.collectAsState().value
        .filter { it.folderId == folderId }
        .map { Item.NoteItem(it) }

    val folderName = "📂 " + (viewModel.folders.collectAsState().value
        .find { it.id == folderId }?.name ?: "Folder")

    Scaffold(
        topBar = {
            TopBar(
                title = folderName,
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { AdBanner(context) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            // ✅ "New Folder" 버튼 제거, "New Note" 버튼만 유지
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("note_edit/null") }, // ✅ 노트 추가 화면으로 이동
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.new_note),
                        contentDescription = "New Note"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.new_note), style = MemoKingTypography.labelMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 노트 목록 (폴더 내부)
            LazyColumn {
                item {
                    Text(text = stringResource(R.string.notes), style = MemoKingTypography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(notes.size) { index ->
                    val noteItem = notes[index]
                    NoteListItem(
                        note = noteItem,
                        onClick = { onNoteClick(noteItem.note.id) },  // ✅ 수정
                        onDeleteNoteClick = { onDeleteNoteClick(noteItem.note.id) } // ✅ 수정
                    )
                }
            }
        }
    }
}
