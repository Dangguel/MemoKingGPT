package kr.co.dangguel.memokinggpt.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onAddNoteClick: () -> Unit
) {
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteTargetId by remember { mutableStateOf<Long?>(null) }
    var isFolder by remember { mutableStateOf(false) }

    val newNoteIcon = painterResource(id = R.drawable.new_note)
    val newFolderIcon = painterResource(id = R.drawable.new_folder)

    val folders = viewModel.folders.collectAsState().value
        .filter { it.parentFolderId == currentFolderId }
        .map { Item.FolderItem(it) }

    val allNotes = viewModel.notes.collectAsState().value // ✅ 모든 노트를 미리 가져옴

    val title =
        if (currentFolderId == null) stringResource(R.string.app_name) else "📂 ${folders.find { it.folder.id == currentFolderId }?.folder?.name ?: "Unknown"}"
    val showBackButton = currentFolderId != null

    Scaffold(
        topBar = {
            TopBar(
                title = title,
                showBackButton = showBackButton,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(bottom = 16.dp)) {
                AdBanner(context)
            }
        },
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
                    text = stringResource(R.string.new_note),
                    onClick = onAddNoteClick,
                    modifier = Modifier.weight(1f)
                )
                RoundedButton(
                    icon = newFolderIcon,
                    text = stringResource(R.string.new_folder),
                    onClick = onAddFolderClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                // 🔹 폴더 목록

                item {
                    Text(
                        text = stringResource(R.string.folders),
                        style = MemoKingTypography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (folders.isNotEmpty()) {
                    items(folders.size) { index ->
                        val folderItem = folders[index]

                        // ✅ 폴더 내 노트 개수 직접 for문 돌려서 확인
                        var noteCount = 0
                        for (note in allNotes) {
                            if (note.folderId == folderItem.folder.id) {
                                noteCount++
                            }
                        }

                        Log.d(
                            "NoteCountCheck",
                            "Folder ID: ${folderItem.folder.id}, Note Count: $noteCount"
                        )

                        FolderListItem(
                            folder = folderItem,
                            noteCount = noteCount,
                            onClick = { onFolderClick(folderItem.folder.id) },
                            onDeleteFolderClick = {
                                deleteTargetId = folderItem.folder.id
                                isFolder = true
                                showDeleteDialog = true
                            }
                        )
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_folders),
                                style = MemoKingTypography.bodyLarge
                            )
                        }
                    }
                }

                // 🔹 노트 목록
                val notes = allNotes.filter { it.folderId == currentFolderId }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.notes),
                        style = MemoKingTypography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (notes.isNotEmpty()) {
                    items(notes.size) { index ->
                        val noteItem = notes[index]
                        NoteListItem(
                            note = Item.NoteItem(noteItem),
                            onClick = { onNoteClick(noteItem.id) },
                            onDeleteNoteClick = {
                                deleteTargetId = noteItem.id
                                isFolder = false
                                showDeleteDialog = true
                            }
                        )
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_notes),
                                style = MemoKingTypography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_confirmation)) },
            text = { Text(stringResource(R.string.delete_warning)) },
            confirmButton = {
                TextButton(onClick = {
                    deleteTargetId?.let { id ->
                        if (isFolder) {
                            viewModel.onDeleteFolder(id)
                        } else {
                            viewModel.onDeleteNote(id)
                        }
                    }
                    showDeleteDialog = false
                    deleteTargetId = null
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}
