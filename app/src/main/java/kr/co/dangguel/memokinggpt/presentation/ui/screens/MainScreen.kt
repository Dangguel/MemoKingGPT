package kr.co.dangguel.memokinggpt.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.presentation.ui.components.AdBanner
import kr.co.dangguel.memokinggpt.presentation.ui.components.ItemGridContainer
import kr.co.dangguel.memokinggpt.presentation.ui.components.NoteSortButton
import kr.co.dangguel.memokinggpt.presentation.ui.components.TopBar
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    items: List<Item>,
    onItemClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit
) {
    println("🖥️ MainScreen 렌더링, 아이템 개수: ${items.size}")

    val context = LocalContext.current
    val sortOrder by viewModel.sortOrder.collectAsState() // ✅ 정렬 상태 반영
    val isAscending by viewModel.isAscending.collectAsState() // ✅ 정렬 방향 상태 반영

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { AdBanner(context) }, // ✅ 하단 배너 광고
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(Icons.Default.Add, contentDescription = "노트 추가")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 4.dp)
        ) {
            NoteSortButton(
                currentSortOrder = sortOrder,
                isAscending = isAscending,
                onSortChange = { order -> viewModel.updateSortOrder(order) },
                onToggleSort = { viewModel.toggleSortDirection() }
            )
            ItemGridContainer(items = items, onItemClick = onItemClick)
        }
    }
}
