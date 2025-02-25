package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kr.co.dangguel.memokinggpt.domain.model.SortOrder

@Composable
fun NoteSortButton(
    currentSortOrder: SortOrder,
    isAscending: Boolean,
    onSortChange: (SortOrder) -> Unit,
    onToggleSort: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Sort, contentDescription = "정렬")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("제목순") },
                    onClick = {
                        onSortChange(SortOrder.TITLE)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("생성 날짜순") },
                    onClick = {
                        onSortChange(SortOrder.CREATED_DATE)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("수정 날짜순") },
                    onClick = {
                        onSortChange(SortOrder.UPDATED_DATE)
                        expanded = false
                    }
                )
            }
        }

        IconButton(onClick = { onToggleSort() }) {
            Icon(
                imageVector = if (isAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = if (isAscending) "오름차순" else "내림차순"
            )
        }
    }
}
