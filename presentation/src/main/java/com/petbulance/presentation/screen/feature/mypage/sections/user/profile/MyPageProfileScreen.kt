package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.hooks.PhotoPickerMediaType
import com.petbulance.presentation.utils.hooks.rememberPhotoPickerLauncher
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun MyPageProfileScreen(
    navController: NavController,
    argument: MyPageProfileArgument,
    data: MyPageProfileData
) {
    val currentNickname = data.userInfo?.nickname ?: ""
    var nickname by remember(currentNickname) { mutableStateOf(currentNickname) }
    val nicknameValidation = validateNickname(nickname, currentNickname)

    val isOnProgress = argument.dataState == MyPageProfileDataState.OnProgress
    val hasImageChanged = data.selectedImageUri != null
    val isSaveEnabled = (nicknameValidation == NicknameValidation.Valid || hasImageChanged) && !isOnProgress

    val launchPhotoPicker = rememberPhotoPickerLauncher(
        multiple = false,
        mediaType = PhotoPickerMediaType.IMAGE
    ) { uris ->
        uris.firstOrNull()?.let { argument.intent(MyPageProfileIntent.SelectImage(it)) }
    }

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is MyPageProfileEvent.DataFetch.Error -> {}
            }
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            if (event is MyPageProfileEvent.SaveSuccess) {
                navController.safePopBackStack()
            }
        }
    }

    BackHandler(enabled = isOnProgress) { }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "프로필 수정",
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.MY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ProfileScreenContents(
                nickname = nickname,
                onNicknameChange = { nickname = it },
                nicknameValidation = nicknameValidation,
                selectedImageUri = data.selectedImageUri,
                profileImageUrl = data.userInfo?.profileImageUrl,
                isSaveEnabled = isSaveEnabled,
                isOnProgress = isOnProgress,
                onImageClicked = { launchPhotoPicker() },
                onSaveClicked = {
                    argument.intent(
                        MyPageProfileIntent.SaveProfile(
                            newNickname = nickname,
                            imageUriString = data.selectedImageUri?.toString()
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun ProfileScreenContents(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    nicknameValidation: NicknameValidation,
    selectedImageUri: Uri?,
    profileImageUrl: String?,
    isSaveEnabled: Boolean,
    isOnProgress: Boolean,
    onImageClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacingMedium, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        ProfileImageSection(
            selectedImageUri = selectedImageUri,
            profileImageUrl = profileImageUrl,
            onImageClicked = onImageClicked
        )

        Spacer(modifier = Modifier.height(8.dp))

        NicknameSection(
            nickname = nickname,
            onNicknameChange = onNicknameChange,
            validation = nicknameValidation
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicButton(
            modifier = Modifier.fillMaxWidth(),
            text = "저장",
            size = BasicButtonSize.L,
            buttonType = if (isSaveEnabled) BasicButtonType.SECONDARY else BasicButtonType.DISABLED,
            radius = 16.dp,
            onClicked = { if (isSaveEnabled) onSaveClicked() }
        )
    }
}

@Composable
private fun ProfileImageSection(
    selectedImageUri: Uri?,
    profileImageUrl: String?,
    onImageClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(88.dp)
            .clickable { onImageClicked() },
        contentAlignment = Alignment.BottomEnd
    ) {
        BasicImageBox(
            modifier = Modifier.clip(CircleShape),
            size = 88.dp,
            uri = selectedImageUri ?: profileImageUrl?.toUri(),
            placeholderImageResource = R.drawable.img_checker,
            errorImageResource = R.drawable.img_checker
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(colorScheme.bg.frame.default, CircleShape)
                .border(1.dp, colorScheme.border.subtle, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            BasicIcon(
                modifier = Modifier.size(16.dp),
                iconResource = IconResource.Vector(Icons.Filled.CameraAlt),
                contentDescription = "프로필 사진 변경",
                size = 16.dp,
                tint = colorScheme.icon.dark
            )
        }
    }
}

@Composable
private fun NicknameSection(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    validation: NicknameValidation
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacingXS)
    ) {
        Text(
            text = "닉네임",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.text.primary
        )

        BasicTextField(
            value = nickname,
            onValueChange = { if (it.length <= 12) onNicknameChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.bg.frame.subtle, RoundedCornerShape(8.dp))
                .padding(horizontal = spacingMedium, vertical = 14.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.text.primary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            cursorBrush = SolidColor(colorScheme.action.primary.default),
            decorationBox = { innerTextField ->
                Box {
                    if (nickname.isEmpty()) {
                        Text(
                            text = "닉네임을 입력하세요",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorScheme.text.disabled
                        )
                    }
                    innerTextField()
                    if (nickname.isNotEmpty()) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "지우기",
                            tint = colorScheme.icon.dark,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(16.dp)
                                .clickable { onNicknameChange("") }
                        )
                    }
                }
            }
        )

        when (validation) {
            NicknameValidation.Empty -> Text(
                text = "2-12자, 한글/영문/숫자 사용 가능해요",
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.text.caption
            )

            NicknameValidation.TooShort -> ValidationMessage(
                text = "2자 이상 입력해주세요",
                isError = true
            )

            NicknameValidation.TooLong -> ValidationMessage(
                text = "12자 이하로 입력해주세요",
                isError = true
            )

            NicknameValidation.InvalidChars -> ValidationMessage(
                text = "특수문자는 사용할 수 없어요",
                isError = true
            )

            NicknameValidation.SameAsCurrent -> Text(
                text = "현재 닉네임이에요",
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.text.caption
            )

            NicknameValidation.Valid -> ValidationMessage(
                text = "사용 가능한 닉네임이에요!",
                isError = false
            )
        }
    }
}

@Composable
private fun ValidationMessage(text: String, isError: Boolean) {
    val color = if (isError) colorScheme.status.error.default else colorScheme.tag.green.medium
    val icon = if (isError) Icons.Default.AddCircle else Icons.Default.CheckCircle
    val rotateAngle = if (isError) 45f else 0f

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicIcon(
            modifier = Modifier.rotate(rotateAngle),
            iconResource = IconResource.Vector(icon),
            contentDescription = "오류",
            size = iconSizeSmall,
            tint = color
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

enum class NicknameValidation { Empty, TooShort, TooLong, InvalidChars, SameAsCurrent, Valid }

private val NICKNAME_REGEX = Regex("^[가-힣a-zA-Z0-9]*$")

fun validateNickname(nickname: String, currentNickname: String): NicknameValidation {
    if (nickname.isEmpty()) return NicknameValidation.Empty
    if (!NICKNAME_REGEX.matches(nickname)) return NicknameValidation.InvalidChars
    if (nickname.length < 2) return NicknameValidation.TooShort
    if (nickname.length > 12) return NicknameValidation.TooLong
    if (nickname == currentNickname) return NicknameValidation.SameAsCurrent
    return NicknameValidation.Valid
}

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Preview
@Composable
private fun MyPageProfileScreenPreview() {
    PetbulanceTheme {
        MyPageProfileScreen(
            navController = rememberNavController(),
            argument = MyPageProfileArgument(
                intent = { },
                dataState = MyPageProfileDataState.Init,
                event = MutableSharedFlow()
            ),
            data = MyPageProfileData.stub()
        )
    }
}