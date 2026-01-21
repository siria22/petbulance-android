package com.petbulance.presentation.component.ui.atom

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme

/**
 * Basic Bottom Sheet
 *
 * @param showBottomSheet : 이 시트를 보일 것인지를 관리하는 boolean 값
 * @param sheetState : 상위 Compsoable에서 제어 필요 시 직접 주입
 * @param onDismissRequest
 * @param content : 보여줄 Composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicBottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            containerColor = colorScheme.bg.frame.default,
            sheetState = sheetState
        ) {
            content()
        }
    }
}