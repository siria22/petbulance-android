package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicCheckBox
import com.petbulance.presentation.component.ui.atom.CustomRadioButton
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.molecule.WarningDialog
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition.composables.CoalitionSuccessDialog
import com.petbulance.presentation.screen.feature.review.common.ReviewInputTextField
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun CoalitionScreen(
    navController: NavController,
    argument: CoalitionArgument,
    data: CoalitionData
) {
    val dataState = argument.dataState
    val isOnProgress = dataState == CoalitionDataState.OnProgress

    var showExitDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is CoalitionEvent.SubmitSuccess -> {
                    showSuccessDialog = true
                }
            }
        }
    }

    BackHandler(enabled = !isOnProgress) {
        val hasInput = data.inquiryType.isNotBlank() ||
                data.companyName.isNotBlank() ||
                data.managerName.isNotBlank() ||
                data.content.isNotBlank()

        if (hasInput) {
            showExitDialog = true
        } else {
            navController.safePopBackStack()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "광고/병원 제휴 문의",
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        val hasInput = data.inquiryType.isNotBlank() ||
                                data.companyName.isNotBlank() ||
                                data.managerName.isNotBlank() ||
                                data.content.isNotBlank()

                        if (hasInput) {
                            showExitDialog = true
                        } else {
                            navController.safePopBackStack()
                        }
                    },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = false,
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.MY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            CoalitionScreenContents(
                data = data,
                onIntent = argument.intent,
                isOnProgress = isOnProgress
            )
        }
    }

    if (showExitDialog) {
        WarningDialog(
            title = "변경 사항이 있습니다.",
            content = "작성 중인 내용이 있습니다. 정말 나가시겠습니까?",
            cancelText = "취소",
            confirmText = "삭제",
            onDismissRequest = { showExitDialog = false },
            onExitButtonClicked = {
                showExitDialog = false
                navController.safePopBackStack()
            }
        )
    }

    if (showSuccessDialog) {
        CoalitionSuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                navController.safeNavigate(ScreenDestinations.MyPage.Help.CS.route)
            },
            onNavigateToHome = {
                showSuccessDialog = false
                navController.safeNavigate(ScreenDestinations.Home.route)
            }
        )
    }
}

@Composable
private fun CoalitionScreenContents(
    data: CoalitionData,
    onIntent: (CoalitionIntent) -> Unit,
    isOnProgress: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacingMedium, vertical = spacingXL),
        verticalArrangement = Arrangement.spacedBy(spacingLarge)
    ) {
        // 1. 문의 유형
        InquiryTypeSection(
            selectedType = data.inquiryType,
            onTypeSelected = { onIntent(CoalitionIntent.OnInquiryTypeChanged(it)) }
        )

        // 2. 회사/병원명
        InputFieldSection(
            label = "회사/병원명",
            value = data.companyName,
            placeholder = "예) 펫뷸런스 동물병원",
            onValueChange = { onIntent(CoalitionIntent.OnCompanyNameChanged(it)) }
        )

        // 3. 담당자명 & 직책
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacingSmall)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                InputFieldSection(
                    label = "담당자명",
                    value = data.managerName,
                    placeholder = "홍길동",
                    onValueChange = { onIntent(CoalitionIntent.OnManagerNameChanged(it)) }
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                InputFieldSection(
                    label = "직책",
                    value = data.managerPosition,
                    placeholder = "예) 마케팅 매니저",
                    onValueChange = { onIntent(CoalitionIntent.OnManagerPositionChanged(it)) }
                )
            }
        }

        // 4. 연락처
        InputFieldSection(
            label = "연락처",
            value = data.phone,
            placeholder = "010-0000-0000",
            onValueChange = { onIntent(CoalitionIntent.OnPhoneChanged(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // 5. 이메일
        InputFieldSection(
            label = "이메일",
            value = data.email,
            placeholder = "email@company.com",
            onValueChange = { onIntent(CoalitionIntent.OnEmailChanged(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // 6. 관심 항목 (중복선택 가능)
        InterestTypeSection(
            selectedTypes = data.interestTypes,
            onTypeToggled = { onIntent(CoalitionIntent.OnInterestTypeToggled(it)) }
        )

        // 7. 문의 내용
        ContentSection(
            content = data.content,
            onContentChange = { onIntent(CoalitionIntent.OnContentChanged(it)) }
        )

        // 8. 개인정보 동의
        PrivacyConsentSection(
            isChecked = data.privacyConsent,
            onCheckedChange = { onIntent(CoalitionIntent.OnPrivacyConsentChanged(it)) }
        )

        // 9. 제출 버튼
        BasicButton(
            modifier = Modifier.fillMaxWidth(),
            text =  if (isOnProgress) "제출 중..." else "문의 제출",
            size = BasicButtonSize.L,
            buttonType = BasicButtonType.PRIMARY,
            radius = 16.dp
        ) {
            onIntent(CoalitionIntent.OnSubmitClicked)
        }
        Spacer(modifier = Modifier.height(spacingXL))
    }
}


// 문의 유형 선택 섹션
@Composable
private fun InquiryTypeSection(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingXXS)) {
        Text(
            text = "문의 유형",
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
        Text(
            text = "제품 유형을 선택하고 정보를 입력해주세요.",
            style = typography.labelMedium,
            color = colorScheme.text.caption
        )

        Spacer(modifier = Modifier.height(spacingXS))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacingMedium)
        ) {
            RadioButtonItem(
                text = "광고 문의",
                selected = selectedType == "광고 문의",
                onClick = { onTypeSelected("광고 문의") }
            )
            RadioButtonItem(
                text = "병원 제휴 문의",
                selected = selectedType == "병원 제휴 문의",
                onClick = { onTypeSelected("병원 제휴 문의") }
            )
        }
    }
}

// 라디오 버튼 아이템
@Composable
private fun RadioButtonItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingXXS)
    ) {
        CustomRadioButton(
            selected = selected,
            onClick = onClick,
            size = iconSizeMedium
        )
        Text(
            text = text,
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
    }
}

// 입력 필드 섹션
@Composable
private fun InputFieldSection(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingXXS)) {
        Text(
            text = label,
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
        ReviewInputTextField(
            modifier = Modifier.fillMaxWidth(),
            queryString = value,
            placeholder = placeholder,
            onQueryStringChanged = onValueChange,
            singleLine = true,
            keyboardOptions = keyboardOptions
        )
    }
}

// 관심 항목 섹션
@Composable
private fun InterestTypeSection(
    selectedTypes: List<String>,
    onTypeToggled: (String) -> Unit
) {
    val interestOptions = listOf("배너 광고", "이벤트 협업", "병원 등록", "기타")

    Column(verticalArrangement = Arrangement.spacedBy(spacingXXS)) {
        Text(
            text = "관심 항목 (중복선택 가능)",
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )

        Spacer(modifier = Modifier.height(spacingXS))

        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            interestOptions.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacingMedium)
                ) {
                    rowItems.forEach { item ->
                        CheckboxItem(
                            text = item,
                            checked = selectedTypes.contains(item),
                            onCheckedChange = { onTypeToggled(item) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// 체크박스 아이템
@Composable
private fun CheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable { onCheckedChange() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingXXS)
    ) {
        BasicCheckBox(
            checkState = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = text,
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
    }
}

// 문의 내용 섹션
@Composable
private fun ContentSection(
    content: String,
    onContentChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingXXS)) {
        Text(
            text = "문의 내용",
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
        ReviewInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            queryString = content,
            placeholder = "제휴/광고 목적과 예산, 희망 일정 등을 자유롭게 작성해 주세요.",
            onQueryStringChanged = onContentChange,
            singleLine = false
        )
    }
}

// 개인정보 동의 섹션
@Composable
private fun PrivacyConsentSection(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) },
        horizontalArrangement = Arrangement.spacedBy(spacingXXS)
    ) {
        BasicCheckBox(
            checkState = isChecked,
            onCheckedChange = { onCheckedChange(!isChecked) }
        )
        Text(
            text = "(필수) 문의 처리 목적의 개인정보 수집 및 이용에 동의합니다.",
            style = typography.bodyMedium,
            color = colorScheme.text.tertiary
        )
    }
}

@Preview
@Composable
private fun CoalitionScreenPreview() {
    PetbulanceTheme {
        CoalitionScreen(
            navController = rememberNavController(),
            argument = CoalitionArgument(
                intent = { },
                dataState = CoalitionDataState.Init,
                screenState = CoalitionScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = CoalitionData.stub()
        )
    }
}

@Preview
@Composable
private fun CoalitionScreenEmptyPreview() {
    PetbulanceTheme {
        CoalitionScreen(
            navController = rememberNavController(),
            argument = CoalitionArgument(
                intent = { },
                dataState = CoalitionDataState.Init,
                screenState = CoalitionScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = CoalitionData.empty()
        )
    }
}