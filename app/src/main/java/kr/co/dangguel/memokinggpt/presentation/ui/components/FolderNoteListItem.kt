package kr.co.dangguel.memokinggpt.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingTypography

@Composable
fun FolderListItem(
    folder: Item.FolderItem,
    noteCount: Int,  // ✅ 폴더 내 노트 개수 전달
    onClick: (Long) -> Unit,
    onDeleteFolderClick: (Long) -> Unit
) {
    ListItem(
        headlineContent = { Text(folder.folder.name, style = MemoKingTypography.labelMedium) },
        supportingContent = {
            Text(
                text = "$noteCount notes",  // ✅ 정확한 노트 개수 표시
                style = MemoKingTypography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.rounded_folder),
                contentDescription = "폴더 아이콘",
                tint = Color.Unspecified,
                modifier = Modifier.size(48.dp)
            )
        },
        trailingContent = {
            IconButton(onClick = { onDeleteFolderClick(folder.folder.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete, // ✅ 삭제 아이콘으로 변경
                    contentDescription = "폴더 삭제" // ✅ 설명 변경
                )
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
fun NoteListItem(
    note: Item.NoteItem,
    onClick: (Long) -> Unit,
    onDeleteNoteClick: (Long) -> Unit
) {
    ListItem(
        headlineContent = { Text(note.note.title, style = MemoKingTypography.labelMedium) },
        supportingContent = {
            Text(
                text = note.note.content.take(30) + if (note.note.content.length > 30) "..." else "",
                style = MemoKingTypography.labelSmall
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.rounded_note),
                contentDescription = "노트 아이콘",
                tint = Color.Unspecified,
                modifier = Modifier.size(48.dp)
            )
        },
        trailingContent = {
            IconButton(onClick = { onDeleteNoteClick(note.note.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete, // ✅ 삭제 아이콘으로 변경
                    contentDescription = "노트 삭제" // ✅ 설명 변경
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent), // ✅ 배경색 제거
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(note.note.id) }
            .padding(4.dp)
    )
}
