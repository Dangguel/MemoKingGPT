package kr.co.dangguel.memokinggpt.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.presentation.ui.components.AdBanner
import kr.co.dangguel.memokinggpt.presentation.ui.components.FolderListItem
import kr.co.dangguel.memokinggpt.presentation.ui.components.NoteListItem
import kr.co.dangguel.memokinggpt.presentation.ui.components.RoundedButton
import kr.co.dangguel.memokinggpt.presentation.ui.components.TopBar
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

    val title = if (currentFolderId == null) "MemoKing GPT" else "📂 폴더"
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
                    onClick = onAddNoteClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                // 🔹 폴더 목록
                item {
                    Text(text = "Folders", style = MemoKingTypography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(folders.size) { index ->
                    val folderItem = folders[index]
                    val folderNotes = notes.filter { it.note.folderId == folderItem.folder.id } // ✅ 해당 폴더의 노트만 필터링
                    FolderListItem(
                        folder = folderItem,
                        notes = folderNotes, // ✅ 필터링된 노트 리스트 전달
                        onClick = onFolderClick,
                        onEditClick = onEditFolderClick
                    )
                }

                // 🔹 노트 목록
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Notes", style = MemoKingTypography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(notes.size) { index ->
                    val noteItem = notes[index]
                    NoteListItem(noteItem, onNoteClick, onEditNoteClick)
                }
            }
        }
    }
}