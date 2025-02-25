package kr.co.dangguel.memokinggpt.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.presentation.ui.components.AdBanner
import kr.co.dangguel.memokinggpt.presentation.ui.components.FolderListItem
import kr.co.dangguel.memokinggpt.presentation.ui.components.NoteListItem
import kr.co.dangguel.memokinggpt.presentation.ui.components.TopBar
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    folders: List<Item.FolderItem>,
    notes: List<Item.NoteItem>,
    onFolderClick: (Long) -> Unit,
    onNoteClick: (Long) -> Unit,
    onAddFolderClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onEditFolderClick: (Long) -> Unit,
    onEditNoteClick: (Long) -> Unit
) {
    println("🖥️ MainScreen 렌더링, 폴더 개수: ${folders.size}, 노트 개수: ${notes.size}")

    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { AdBanner(context) }, // ✅ 하단 배너 광고
        floatingActionButton = {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(onClick = onAddFolderClick) {
                    Text("📂")
                }
                FloatingActionButton(onClick = onAddNoteClick) {
                    Text("📝")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 🔹 폴더 목록
            item {
                Text(text = "Folders", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(folders.size) { index ->
                val folder = folders[index]
                FolderListItem(folder, onFolderClick, onEditFolderClick)
            }

            // 🔹 노트 목록
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Notes", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(notes.size) { index ->
                val note = notes[index]
                NoteListItem(note, onNoteClick, onEditNoteClick)
            }
        }
    }
}