package com.petbulance.presentation.component.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.spacingSmall

/**
 * 공통 에러 상태 메시지 컴포넌트
 * 
 * 정책 기반 에러 메시지:
 * - 404: "존재하지 않는 페이지에요." / "컨텐츠를 다시 한번 확인해주시겠어요?"
 * - 네트워크: "네트워크 연결이 불안정해요." / "잠시 후 다시 접속해주세요."
 * - 500번대: "일시적인 서버 오류에요." / "잠시 후 다시 시도해주세요."
 */
@Composable
fun ErrorStateMessage(
    primaryMessage: String,
    secondaryMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = primaryMessage,
            style = typography.titleSmall.emp(),
            color = colorScheme.text.tertiary,
            textAlign = TextAlign.Center
        )
        Text(
            text = secondaryMessage,
            style = typography.bodyMedium,
            color = colorScheme.text.caption,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 에러 타입별 미리 정의된 메시지
 */
object ErrorMessages {
    object NotFound {
        const val PRIMARY = "존재하지 않는 페이지에요."
        const val SECONDARY = "컨텐츠를 다시 한번 확인해주시겠어요?"
    }
    
    object Network {
        const val PRIMARY = "네트워크 연결이 불안정해요."
        const val SECONDARY = "잠시 후 다시 접속해주세요."
    }
    
    object ServerError {
        const val PRIMARY = "일시적인 서버 오류에요."
        const val SECONDARY = "잠시 후 다시 시도해주세요."
    }
    
    object TermsLoadFailed {
        const val PRIMARY = "약관을 불러올 수 없습니다."
        const val SECONDARY = "잠시 후 다시 시도해주세요."
    }
}

@Preview
@Composable
private fun ErrorStateMessagePreview() {
    PetbulanceTheme {
        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            ErrorStateMessage(
                primaryMessage = ErrorMessages.NotFound.PRIMARY,
                secondaryMessage = ErrorMessages.NotFound.SECONDARY
            )
            ErrorStateMessage(
                primaryMessage = ErrorMessages.Network.PRIMARY,
                secondaryMessage = ErrorMessages.Network.SECONDARY
            )
            ErrorStateMessage(
                primaryMessage = ErrorMessages.ServerError.PRIMARY,
                secondaryMessage = ErrorMessages.ServerError.SECONDARY
            )
        }
    }
}
