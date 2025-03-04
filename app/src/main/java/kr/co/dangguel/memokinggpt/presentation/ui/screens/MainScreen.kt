package kr.co.dangguel.memokinggpt.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.presentation.ui.components.*
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingTypography

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    currentFolderId: Long?,
    onFolderClick: (Long) -> Unit,
    onNoteClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    onAddFolderClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onEditFolderClick: (Long) -> Unit,
    onEditNoteClick: (Long) -> Unit
) {
    val context = LocalContext.current

    val newNoteIcon = painterResource(id = R.drawable.new_note)
    val newFolderIcon = painterResource(id = R.drawable.new_folder)

    val folders = viewModel.folders.collectAsState().value
        .filter { it.parentFolderId == currentFolderId }
        .map { Item.FolderItem(it) }

    val notes = viewModel.notes.collectAsState().value
        .filter { it.folderId == currentFolderId }
        .map { Item.NoteItem(it) }

    val title = if (currentFolderId == null) "MemoKing GPT" else "📂 ${folders.find { it.folder.id == currentFolderId }?.folder?.name ?: "Unknown"}"
    val showBackButton = currentFolderId != null

    Scaffold(
        topBar = { TopBar(title = title, showBackButton = showBackButton, onBackClick = onBackClick) },
        bottomBar = { AdBanner(context) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RoundedButton(
                    icon = newNoteIcon,
                    text = "New Note",
                    onClick = onAddNoteClick,
                    modifier = Modifier.weight(1f)
                )
                RoundedButton(
                    icon = newFolderIcon,
                    text = "New Folder",
                    onClick = onAddFolderClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                // 🔹 폴더 목록
                if (folders.isNotEmpty()) {
                    item {
                        Text(text = "Folders", style = MemoKingTypography.labelLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(folders.size) { index ->
                        val folderItem = folders[index]
                        val noteCount = notes.count { it.note.folderId == folderItem.folder.id }

                        FolderListItem(
                            folder = folderItem,
                            noteCount = noteCount,
                            onClick = { onFolderClick(folderItem.folder.id) },
                            onEditClick = { onEditFolderClick(folderItem.folder.id) }
                        )
                    }
                }

                // 🔹 노트 목록
                if (notes.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Notes", style = MemoKingTypography.labelLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(notes.size) { index ->
                        val noteItem = notes[index]
                        NoteListItem(
                            note = noteItem,
                            onClick = { onNoteClick(noteItem.note.id) },
                            onEditClick = { onEditNoteClick(noteItem.note.id) }
                        )
                    }
                }

                // ✅ 폴더와 노트가 없을 경우 Empty Message 추가
                if (folders.isEmpty() && notes.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No Folders or Notes Available", style = MemoKingTypography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}
