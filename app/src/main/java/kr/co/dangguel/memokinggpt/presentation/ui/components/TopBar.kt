package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = {  }) {
                Icon(Icons.Default.Search, contentDescription = "검색")
            }

            IconButton(onClick = { /* 더보기 메뉴 추가 예정 */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "더보기")
            }
        }
    )
}