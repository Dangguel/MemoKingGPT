package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingTypography

@Composable
fun FolderListItem(
    folder: Item.FolderItem,
    notes: List<Item.NoteItem>,
    onClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit
) {
    val noteCount = notes.count { it.note.folderId == folder.folder.id } // ✅ 폴더 내부 노트 개수 계산

    ListItem(
        headlineContent = { Text(folder.folder.name, style = MemoKingTypography.labelMedium) },
        supportingContent = {
            Text(
                text = "$noteCount notes",
                style = MemoKingTypography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.rounded_folder),
                contentDescription = "폴더 아이콘",
                tint = Color.Unspecified
            )
        },
        trailingContent = {
            IconButton(onClick = { onEditClick(folder.folder.id) }) {
                Icon(painter = painterResource(id = R.drawable.edit), contentDescription = "폴더 편집")
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent), // ✅ 배경색 제거
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(folder.folder.id) }
            .padding(4.dp)
    )
}


@Composable
fun NoteListItem(note: Item.NoteItem, onClick: (Long) -> Unit, onEditClick: (Long) -> Unit) {
    ListItem(
        headlineContent = { Text(note.note.title, style = MemoKingTypography.labelMedium) },
        leadingContent = {
            Icon(painter = painterResource(id = R.drawable.rounded_note), contentDescription = "노트 아이콘", tint = Color.Unspecified)
        },
        trailingContent = {
            IconButton(onClick = { onEditClick(note.note.id) }) {
                Icon(painter = painterResource(id = R.drawable.edit), contentDescription = "노트 편집")
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent), // ✅ 배경색 제거
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(note.note.id) }
            .padding(4.dp)
    )
}
