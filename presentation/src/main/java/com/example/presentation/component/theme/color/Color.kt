package com.example.presentation.component.theme.color

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulancePrimitives
import com.example.presentation.component.theme.PetbulanceTheme


object ColorObject {
    object Provider {
        val KAKAO = Color(0xFFFEE500)
        val NAVER = Color(0xFF00C73C)
        val GOOGLE = Color(0xFFFFFFFF)
    }
}

private val LightColorScheme = PetbulanceColorScheme(

    // "bg":
    bg = BgColors.create(
        frame = FrameBgColors.create(
            default = PetbulancePrimitives.Base.white,
            subtle = PetbulancePrimitives.Gray.p100,
            overlay = PetbulancePrimitives.Gray.p900,
            medium = PetbulancePrimitives.Gray.p200,
            strong = PetbulancePrimitives.Gray.p300,
            verystrong = PetbulancePrimitives.Gray.p400
        ),
        icon = IconBgColors.create(
            hover = PetbulancePrimitives.Gray.p50,
            focused = PetbulancePrimitives.Gray.p50,
            pressed = PetbulancePrimitives.Gray.p100
        )
    ),

    // "text":
    text = TextColors.create(
        primary = PetbulancePrimitives.Base.black,
        secondary = PetbulancePrimitives.Gray.p800,
        inverse = PetbulancePrimitives.Gray.p100,
        tertiary = PetbulancePrimitives.Gray.p700,
        disabled = PetbulancePrimitives.Gray.p400,
        caption = PetbulancePrimitives.Gray.p500
    ),

    // "action":
    action = ActionColors.create(
        // "action.primary":
        primary = ActionStateColors.create(
            default = PetbulancePrimitives.Primary.p500,
            hover = PetbulancePrimitives.Primary.p600,
            pressed = PetbulancePrimitives.Primary.p700,
            disabled = PetbulancePrimitives.Gray.p300,
            text = PetbulancePrimitives.Base.white
        ),
        // "action.link":
        link = LinkStateColors.create(
            default = PetbulancePrimitives.Info.p500,
            hover = PetbulancePrimitives.Info.p600,
            pressed = PetbulancePrimitives.Info.p700,
            disabled = PetbulancePrimitives.Gray.p300,
            visited = PetbulancePrimitives.Info.p900
        )
    ),

    // "status":
    status = StatusColors.create(
        // "status.success":
        success = StatusSet.create(
            default = PetbulancePrimitives.Primary.p500,
            bg = PetbulancePrimitives.Primary.p100,
            text = PetbulancePrimitives.Gray.p900
        ),
        // "status.error":
        error = StatusSet.create(
            default = PetbulancePrimitives.Negative.p500,
            bg = PetbulancePrimitives.Negative.p100,
            text = PetbulancePrimitives.Gray.p900
        ),
        // "status.warning":
        warning = StatusSet.create(
            default = PetbulancePrimitives.Warning.p500,
            bg = PetbulancePrimitives.Warning.p100,
            text = PetbulancePrimitives.Gray.p900
        ),
        // "status.info":
        info = StatusSet.create(
            default = PetbulancePrimitives.Info.p500,
            bg = PetbulancePrimitives.Info.p100,
            text = PetbulancePrimitives.Gray.p900
        )
    ),

    // "tag":
    tag = TagColors.create(
        red = TagVariant.create(
            verysubtle = PetbulancePrimitives.Tertiary.p300,
            subtle = PetbulancePrimitives.Tertiary.p500,
            medium = PetbulancePrimitives.Tertiary.p600,
            strong = PetbulancePrimitives.Tertiary.p700,
            verystrong = PetbulancePrimitives.Tertiary.p800,
            bg = PetbulancePrimitives.Tertiary.p100
        ),
        green = TagVariant.create(
            verysubtle = PetbulancePrimitives.Positive.p100,
            subtle = PetbulancePrimitives.Positive.p300,
            medium = PetbulancePrimitives.Positive.p500,
            strong = PetbulancePrimitives.Positive.p600,
            verystrong = PetbulancePrimitives.Positive.p700,
            bg = PetbulancePrimitives.Positive.p50
        ),
        blue = TagVariant.create(
            verysubtle = PetbulancePrimitives.Info.p100,
            subtle = PetbulancePrimitives.Info.p300,
            medium = PetbulancePrimitives.Info.p500,
            strong = PetbulancePrimitives.Info.p600,
            verystrong = PetbulancePrimitives.Info.p700,
            bg = PetbulancePrimitives.Info.p50
        ),
        yellow = TagVariant.create(
            verysubtle = PetbulancePrimitives.Quaternary.p100,
            subtle = PetbulancePrimitives.Quaternary.p300,
            medium = PetbulancePrimitives.Quaternary.p500,
            strong = PetbulancePrimitives.Quaternary.p600,
            verystrong = PetbulancePrimitives.Quaternary.p700,
            bg = PetbulancePrimitives.Quaternary.p50
        ),
        trust = TagVariant.create(
            verysubtle = PetbulancePrimitives.Secondary.p100,
            subtle = PetbulancePrimitives.Secondary.p300,
            medium = PetbulancePrimitives.Secondary.p500,
            strong = PetbulancePrimitives.Secondary.p600,
            verystrong = PetbulancePrimitives.Secondary.p700,
            bg = PetbulancePrimitives.Secondary.p50
        )
    ),

    // "border":
    border = BorderColors.create(
        active = PetbulancePrimitives.Gray.p900,
        tertiary = PetbulancePrimitives.Gray.p300,
        secondary = PetbulancePrimitives.Gray.p400,
        white = PetbulancePrimitives.Base.white,
        subtle = PetbulancePrimitives.Gray.p300,
        verySubtle = PetbulancePrimitives.Gray.p200
    ),

    // "icon":
    icon = IconColors.create(
        // "icon.gnb":
        gnb = GnbIconColors.create(
            selected = PetbulancePrimitives.Primary.p500,
            default = PetbulancePrimitives.Gray.p500
        ),
        // "icon.basic":
        basic = PetbulancePrimitives.Gray.p800,
        inverse = PetbulancePrimitives.Base.white,
        disabled = PetbulancePrimitives.Gray.p400,
        dark = PetbulancePrimitives.Gray.p900,
        light = PetbulancePrimitives.Gray.p500,
        medium = PetbulancePrimitives.Gray.p600,
        rating = PetbulancePrimitives.Warning.p500,
    ),

    isDark = false
)

private val DarkColorPalette = LightColorScheme

val LocalPetbulanceColorScheme = staticCompositionLocalOf { LightColorScheme }

@Composable
fun getPetbulanceColorScheme(darkTheme: Boolean): PetbulanceColorScheme {
    return if (darkTheme) DarkColorPalette else LightColorScheme
}

@Preview(name = "Light Color Palette", showBackground = true, widthDp = 300)
@Preview(
    name = "Dark Color Palette",
    showBackground = true,
    widthDp = 300,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ColorPalettePreview() {
    PetbulanceTheme {
        val colorScheme = PetbulanceTheme.colorScheme
        val colors = listOf(
            // BgColors
            "bg.frame.default" to colorScheme.bg.frame.default,
            "bg.frame.subtle" to colorScheme.bg.frame.subtle,
            "bg.frame.overlay" to colorScheme.bg.frame.overlay,
            "bg.frame.medium" to colorScheme.bg.frame.medium,
            "bg.frame.strong" to colorScheme.bg.frame.strong,
            "bg.frame.verystrong" to colorScheme.bg.frame.verystrong,
            "bg.icon.hover" to colorScheme.bg.icon.hover,
            "bg.icon.focused" to colorScheme.bg.icon.focused,
            "bg.icon.pressed" to colorScheme.bg.icon.pressed,

            // TextColors
            "text.primary" to colorScheme.text.primary,
            "text.secondary" to colorScheme.text.secondary,
            "text.inverse" to colorScheme.text.inverse,
            "text.tertiary" to colorScheme.text.tertiary,
            "text.disabled" to colorScheme.text.disabled,
            "text.caption" to colorScheme.text.caption,

            // ActionColors - Primary
            "action.primary.default" to colorScheme.action.primary.default,
            "action.primary.hover" to colorScheme.action.primary.hover,
            "action.primary.pressed" to colorScheme.action.primary.pressed,
            "action.primary.disabled" to colorScheme.action.primary.disabled,
            "action.primary.text" to colorScheme.action.primary.text,

            // ActionColors - Link
            "action.link.default" to colorScheme.action.link.default,
            "action.link.hover" to colorScheme.action.link.hover,
            "action.link.pressed" to colorScheme.action.link.pressed,
            "action.link.disabled" to colorScheme.action.link.disabled,
            "action.link.visited" to colorScheme.action.link.visited,

            // StatusColors - Success
            "status.success.default" to colorScheme.status.success.default,
            "status.success.bg" to colorScheme.status.success.bg,
            "status.success.text" to colorScheme.status.success.text,

            // StatusColors - Error
            "status.error.default" to colorScheme.status.error.default,
            "status.error.bg" to colorScheme.status.error.bg,
            "status.error.text" to colorScheme.status.error.text,

            // StatusColors - Warning
            "status.warning.default" to colorScheme.status.warning.default,
            "status.warning.bg" to colorScheme.status.warning.bg,
            "status.warning.text" to colorScheme.status.warning.text,

            // StatusColors - Info
            "status.info.default" to colorScheme.status.info.default,
            "status.info.bg" to colorScheme.status.info.bg,
            "status.info.text" to colorScheme.status.info.text,

            // BorderColors
            "border.active" to colorScheme.border.active,
            "border.tertiary" to colorScheme.border.tertiary,
            "border.secondary" to colorScheme.border.secondary,
            "border.white" to colorScheme.border.white,

            // IconColors
            "icon.gnb.selected" to colorScheme.icon.gnb.selected,
            "icon.gnb.default" to colorScheme.icon.gnb.default,
            "icon.basic" to colorScheme.icon.basic
        )

        val backgroundColor =
            if (PetbulanceTheme.colorScheme.isDark) PetbulancePrimitives.Gray.p900 else PetbulancePrimitives.Base.white
        LazyColumn(
            modifier = Modifier
                .background(backgroundColor)
                .padding(4.dp)
        ) {
            items(colors) { (name, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color)
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.text.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}