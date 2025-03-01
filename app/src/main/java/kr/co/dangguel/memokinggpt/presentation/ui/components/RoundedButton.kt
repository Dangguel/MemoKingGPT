package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingTypography

@Composable
fun RoundedButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // ✅ 외부에서 Modifier를 받을 수 있도록 변경
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(58.dp), // ✅ 내부에서는 weight 제거
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(painter = icon, contentDescription = text, modifier = Modifier.padding(end = 8.dp))

            Text(
                text,
                style = MemoKingTypography.labelMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
