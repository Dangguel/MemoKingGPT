package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kr.co.dangguel.memokinggpt.data.local.entity.FolderEntity
import kr.co.dangguel.memokinggpt.data.local.entity.NoteEntity

@Composable
fun ItemGrid(
    item: Any,
    isFolder: Boolean,
    onClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(300.dp) // ✅ 전체 크기 설정
            .clickable {
                val id = if (isFolder) (item as FolderEntity).id else (item as NoteEntity).id
                onClick(id)
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // ✅ 남는 공간을 위로 밀어줌
        ) {
            // 🔵 아이콘 or 썸네일 (남는 공간을 최대로 차지)
            Box(
                modifier = Modifier
                    .weight(1f) // ✅ 남는 공간을 최대한 활용
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center // ✅ 가운데 정렬
            ) {
                if (isFolder) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = "폴더 아이콘",
                        modifier = Modifier
                            .fillMaxSize() // ✅ 아이콘 크기
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter((item as NoteEntity).thumbnailPath),
                        contentDescription = "노트 썸네일",
                        modifier = Modifier
                            .fillMaxSize() // ✅ 이미지가 최대한 크도록 설정
                    )
                }
            }

            // 🔴 텍스트 (카드 하단에 고정)
            Text(
                text = if (isFolder) (item as FolderEntity).name else (item as NoteEntity).title,
                modifier = Modifier
                    .padding(4.dp), // ✅ 바닥 여백
                fontSize = 14.sp,
                maxLines = 2, // ✅ 최대 2줄 제한
                overflow = TextOverflow.Ellipsis, // ✅ 긴 텍스트는 생략 (...)
            )
        }
    }
}
