package com.example.presentation.component.ui.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate

/**
 * Bottom Navigation Bar
 *
 * @param selectedItem The currently selected item on the Bottom Navigation Bar
 * @param backgroundColor The background color of the Bottom Navigation Bar. Default is [Color.Transparent]
 * @param navController The instance of [NavController] used for navigation
 */
@Composable
fun BottomNavigationBar(
    selectedItem: CurrentBottomNav,
    backgroundColor: Color = Color.Transparent,
    navController: NavController
) {
    val navItemList = listOf(
        BottomNavInfo(
            label = "홈",
            iconResource = IconResource.Drawable(R.drawable.ic_gnb_home),
            bottomNavType = CurrentBottomNav.HOME,
            onClicked = {
                navController.safeNavigate(ScreenDestinations.Home.route)
            }
        ),
        BottomNavInfo(
            label = "병원검색",
            iconResource = IconResource.Drawable(R.drawable.ic_gnb_healthcross),
            bottomNavType = CurrentBottomNav.SEARCH,
            onClicked = {
                /* TODO : Navigate to Hospital Search Page */
            }
        ),
        BottomNavInfo(
            label = "병원후기",
            iconResource = IconResource.Drawable(R.drawable.ic_gnb_review),
            bottomNavType = CurrentBottomNav.REVIEW,
            onClicked = {
                /* TODO : Navigate to Review Page */
            }
        ),
        BottomNavInfo(
            label = "커뮤니티",
            iconResource = IconResource.Drawable(R.drawable.ic_gnb_community),
            bottomNavType = CurrentBottomNav.COMMUNITY,
            onClicked = {
                /* TODO : Navigate to Community Page */
            }
        ),
        BottomNavInfo(
            label = "My",
            iconResource = IconResource.Drawable(R.drawable.ic_gnb_other),
            bottomNavType = CurrentBottomNav.MY,
            onClicked = {
                /* TODO : Navigate to MyPage */
            }
        ),
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            color = colorScheme.border.subtle,
            thickness = 1.dp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(backgroundColor),
            verticalAlignment = Alignment.Bottom
        ) {
            navItemList.forEachIndexed { _, item ->
                BottomNavItem(
                    iconRes = item.iconResource,
                    itemLabel = item.label,
                    isSelected = (selectedItem == item.bottomNavType),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            item.onClicked
                        },
                    onClicked = item.onClicked
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    iconRes: IconResource,
    itemLabel: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClicked: () -> Unit
) {
    val selectedColor = PetbulanceTheme.colorScheme.icon.gnb.selected
    val unselectedColor = PetbulanceTheme.colorScheme.icon.gnb.default

    val iconColor = if (isSelected) selectedColor else unselectedColor

    Column(
        modifier = modifier.clickable { onClicked() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(PetbulanceTheme.colorScheme.bg.frame.default),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicIcon(
                iconResource = iconRes,
                contentDescription = itemLabel,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = itemLabel,
                color = iconColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

data class BottomNavInfo(
    val label: String,
    val iconResource: IconResource,
    val bottomNavType: CurrentBottomNav,
    val onClicked: () -> Unit
)

enum class CurrentBottomNav {
    HOME, SEARCH, REVIEW, COMMUNITY, MY
}

@Preview
@Composable
private fun BottomNavigationBarPreview1() {
    PetbulanceTheme {
        BottomNavigationBar(
            selectedItem = CurrentBottomNav.HOME,
            navController = rememberNavController()
        )
    }
}