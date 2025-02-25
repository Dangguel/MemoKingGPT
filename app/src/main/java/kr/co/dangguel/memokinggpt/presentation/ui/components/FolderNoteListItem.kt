package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item

@Composable
fun FolderListItem(folder: Item.FolderItem, onClick: (Long) -> Unit, onEditClick: (Long) -> Unit) {
    ListItem(
        headlineContent = { Text(folder.folder.name) }, // ✅ `headlineText` → `headlineContent`
        leadingContent = {
            Icon(imageVector = Icons.Default.Folder, contentDescription = "폴더 아이콘")
        },
        trailingContent = {
            IconButton(onClick = { onEditClick(folder.folder.id) }) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "폴더 편집")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(folder.folder.id) }
            .padding(8.dp)
    )
}

@Composable
fun NoteListItem(note: Item.NoteItem, onClick: (Long) -> Unit, onEditClick: (Long) -> Unit) {
    ListItem(
        headlineContent = { Text(note.note.title) }, // ✅ `headlineText` → `headlineContent`
        leadingContent = {
            Icon(imageVector = Icons.Default.Create, contentDescription = "노트 아이콘")
        },
        trailingContent = {
            IconButton(onClick = { onEditClick(note.note.id) }) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "노트 편집")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(note.note.id) }
            .padding(8.dp)
    )
}
