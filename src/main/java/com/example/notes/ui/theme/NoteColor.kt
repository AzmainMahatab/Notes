package com.example.notes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.composeext.color.adjustSaturation
import com.example.composeext.color.darken
import com.example.composeext.color.lighten
import java.util.*

enum class NoteColor(val base: Color) {
    VANILLA(Color(0xFFFFE0B2)),
    MINT(Color(0xFFC8E6C9)),
    SKY(Color(0xFFBBDEFB)),
    BLUSH(Color(0xFFF8BBD9)),
    LAVENDER(Color(0xFFE1BEE7)),
    LEMON(Color(0xFFFFF9C4)),
    AQUA(Color(0xFFB2DFDB)),
    PEACH(Color(0xFFFFCCBC)),
    VIOLET(Color(0xFFD1C4E9)),
    OCEAN(Color(0xFFB3E5FC));

    val light: Color
        get() = base.lighten(0.08f)
    val dark: Color
        get() = base.darken(0.55f)
            .adjustSaturation(0.75f)

    // Additional variants for flexibility
    val extraLight: Color get() = base.lighten(0.2f)
    val extraDark: Color get() = base.darken(0.7f)

    val displayName: String get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    // Get color based on theme
    @Composable
    fun themedColor(): Color = if (isSystemInDarkTheme()) dark else light


    companion object {
        fun byIndex(index: Int): NoteColor = entries[index] //% entries.size

        @Composable
        fun themedColors(): List<Color> {
            val isDark = isSystemInDarkTheme()
            return entries.map {
                if (isDark) it.dark else it.light
            }
        }

        fun random() = entries.random()
    }
}

// Semantic Colors
val NotePrimaryColor = Color(0xFF6750A4)
val NoteSecondaryColor = Color(0xFF625B71)
val NoteErrorColor = Color(0xFFBA1A1A)
val NoteSuccessColor = Color(0xFF388E3C)
val NoteWarningColor = Color(0xFFFF9800)
