package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item

@Composable
fun ItemGridContainer(items: List<Item>, onItemClick: (Long) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // ✅ 2x2 배치
        modifier = Modifier
    ) {
        items(items.size) { index ->
            val item = items[index]
            when (item) {
                is Item.FolderItem -> {
                    println("📁 폴더: ${item.folder.name}")
                    ItemGrid(
                        item = item.folder,
                        isFolder = true,
                        onClick = onItemClick
                    )
                }
                is Item.NoteItem -> {
                    println("📝 노트: ${item.note.title}")
                    ItemGrid(
                        item = item.note,
                        isFolder = false,
                        onClick = onItemClick
                    )
                }
            }
        }
    }
}
