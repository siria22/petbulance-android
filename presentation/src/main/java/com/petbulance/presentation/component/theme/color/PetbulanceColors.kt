package com.petbulance.presentation.component.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class FrameBgColors(
    val default: Color,
    val subtle: Color,
    val overlay: Color,
    val medium: Color,
    val strong: Color,
    val verystrong: Color
) {
    internal companion object {
        fun create(
            default: Color,
            subtle: Color,
            overlay: Color,
            medium: Color,
            strong: Color,
            verystrong: Color
        ): FrameBgColors {
            return FrameBgColors(default, subtle, overlay, medium, strong, verystrong)
        }
    }
}

@Immutable
data class IconBgColors(
    val hover: Color,
    val focused: Color,
    val pressed: Color
) {
    internal companion object {
        fun create(
            hover: Color,
            focused: Color,
            pressed: Color
        ): IconBgColors {
            return IconBgColors(hover, focused, pressed)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class BgColors private constructor(
    val frame: FrameBgColors,
    val icon: IconBgColors
) {
    internal companion object {
        fun create(
            frame: FrameBgColors,
            icon: IconBgColors
        ): BgColors {
            return BgColors(frame, icon)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class TextColors private constructor(
    val primary: Color,
    val secondary: Color,
    val inverse: Color,
    val tertiary: Color,
    val disabled: Color,
    val caption: Color
) {
    internal companion object {
        fun create(
            primary: Color,
            secondary: Color,
            inverse: Color,
            tertiary: Color,
            disabled: Color,
            caption: Color
        ): TextColors {
            return TextColors(primary, secondary, inverse, tertiary, disabled, caption)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class ActionStateColors private constructor(
    val default: Color,
    val hover: Color,
    val pressed: Color,
    val disabled: Color,
    val text: Color
) {
    internal companion object {
        fun create(
            default: Color,
            hover: Color,
            pressed: Color,
            disabled: Color,
            text: Color
        ): ActionStateColors {
            return ActionStateColors(default, hover, pressed, disabled, text)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class LinkStateColors private constructor(
    val default: Color,
    val hover: Color,
    val pressed: Color,
    val disabled: Color,
    val visited: Color
) {
    internal companion object {
        fun create(
            default: Color,
            hover: Color,
            pressed: Color,
            disabled: Color,
            visited: Color
        ): LinkStateColors {
            return LinkStateColors(default, hover, pressed, disabled, visited)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class ActionColors private constructor(
    val primary: ActionStateColors,
    val link: LinkStateColors
) {
    internal companion object {
        fun create(
            primary: ActionStateColors,
            link: LinkStateColors
        ): ActionColors {
            return ActionColors(primary, link)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class StatusSet private constructor(
    val default: Color,
    val bg: Color,
    val text: Color
) {
    internal companion object {
        fun create(
            default: Color,
            bg: Color,
            text: Color
        ): StatusSet {
            return StatusSet(default, bg, text)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class StatusColors private constructor(
    val success: StatusSet,
    val error: StatusSet,
    val warning: StatusSet,
    val info: StatusSet
) {
    internal companion object {
        fun create(
            success: StatusSet,
            error: StatusSet,
            warning: StatusSet,
            info: StatusSet
        ): StatusColors {
            return StatusColors(success, error, warning, info)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class BorderColors private constructor(
    val active: Color,
    val tertiary: Color,
    val secondary: Color,
    val white: Color,
    val subtle: Color,
    val verySubtle: Color
) {
    internal companion object {
        fun create(
            active: Color,
            tertiary: Color,
            secondary: Color,
            white: Color,
            subtle: Color,
            verySubtle: Color
        ): BorderColors {
            return BorderColors(active, tertiary, secondary, white, subtle, verySubtle)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class GnbIconColors private constructor(
    val selected: Color,
    val default: Color
) {
    internal companion object {
        fun create(
            selected: Color,
            default: Color
        ): GnbIconColors {
            return GnbIconColors(selected, default)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class IconColors private constructor(
    val gnb: GnbIconColors,
    val basic: Color,
    val inverse: Color,
    val disabled: Color,
    val dark: Color,
    val light: Color,
    val medium: Color,
    val rating: Color
) {
    internal companion object {
        fun create(
            gnb: GnbIconColors,
            basic: Color,
            inverse: Color,
            disabled: Color,
            dark: Color,
            light: Color,
            medium: Color,
            rating: Color
        ): IconColors {
            return IconColors(gnb, basic, inverse, disabled, dark, light, medium, rating)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class TagVariant private constructor(
    val verysubtle: Color,
    val subtle: Color,
    val medium: Color,
    val strong: Color,
    val verystrong: Color,
    val bg: Color
) {
    internal companion object {
        fun create(
            verysubtle: Color,
            subtle: Color,
            medium: Color,
            strong: Color,
            verystrong: Color,
            bg: Color
        ): TagVariant {
            return TagVariant(verysubtle, subtle, medium, strong, verystrong, bg)
        }
    }
}

@ConsistentCopyVisibility
@Immutable
data class TagColors private constructor(
    val red: TagVariant,
    val green: TagVariant,
    val blue: TagVariant,
    val yellow: TagVariant,
    val trust: TagVariant
) {
    internal companion object {
        fun create(
            red: TagVariant,
            green: TagVariant,
            blue: TagVariant,
            yellow: TagVariant,
            trust: TagVariant
        ): TagColors {
            return TagColors(red, green, blue, yellow, trust)
        }
    }
}

/**
 * Petbulance의 최종 Semantic Color Scheme
 */
@Immutable
data class PetbulanceColorScheme(
    val bg: BgColors,
    val text: TextColors,
    val action: ActionColors,
    val status: StatusColors,
    val tag: TagColors,
    val border: BorderColors,
    val icon: IconColors,
    val isDark: Boolean
)
