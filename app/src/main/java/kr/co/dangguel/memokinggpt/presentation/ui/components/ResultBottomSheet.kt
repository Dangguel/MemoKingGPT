package kr.co.dangguel.memokinggpt.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.co.dangguel.memokinggpt.R
import kr.co.dangguel.memokinggpt.ui.theme.MemoKingTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBottomSheet(
    title: String,
    initialContent: String,
    onApply: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var editedText by remember { mutableStateOf(initialContent) }

    LaunchedEffect(initialContent) {
        editedText = initialContent
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MemoKingTypography.labelSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = editedText,
                onValueChange = { editedText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.close))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    onApply(editedText)
                    onDismiss()
                }) {
                    Text(stringResource(R.string.apply))
                }
            }
        }
    }
}
