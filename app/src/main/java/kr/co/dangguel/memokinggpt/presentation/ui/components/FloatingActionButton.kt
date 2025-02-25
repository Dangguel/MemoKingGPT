package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun FloatingActionButtonArea(onNoteAdd: () -> Unit) {
    FloatingActionButton(
        onClick = onNoteAdd
    ) {
        Icon(Icons.Default.Add, contentDescription = "노트 추가")
    }
}
