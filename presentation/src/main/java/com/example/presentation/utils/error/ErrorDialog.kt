package com.example.presentation.utils.error

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.Space16
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicDialog

@Composable
fun ErrorDialog(
    directErrorTitle: String = "오류 발생!",
    directErrorMessage: String? = null,
    errorDialogState: ErrorDialogState,
    errorHandler: (Any?) -> Unit
) {
    errorDialogState.logErrorEvent()
    BasicDialog {
        Text(
            text = directErrorTitle,
            style = MaterialTheme.typography.headlineSmall.emp(),
            color = PetbulanceTheme.colorScheme.text.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = directErrorMessage ?: errorDialogState.userMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = PetbulanceTheme.colorScheme.text.caption,
            softWrap = true,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = errorDialogState.exceptionMessage ?: "No exception message",
            style = MaterialTheme.typography.bodyMedium,
            color = PetbulanceTheme.colorScheme.text.caption,
            softWrap = true,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        Space16()

        BasicButton(
            text = "닫기",
            buttonType = BasicButtonType.PRIMARY,
            onClicked = { errorHandler(null) },
            size = BasicButtonSize.M,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun ErrorDialogPreview() {
    PetbulanceTheme {
        ErrorDialog(
            errorDialogState = ErrorDialogState(
                userMessage = "데이터를 불러오는데 실패했습니다.",
                exceptionMessage = "some exception eee",
                isErrorDialogVisible = true
            ),
            errorHandler = {}
        )
    }
}