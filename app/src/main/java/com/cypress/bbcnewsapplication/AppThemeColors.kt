package com.cypress.bbcnewsapplication

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppThemeColors {
    val LightColors = lightColorScheme(
        primary = Color(0xFF6200EE),
        onPrimary = Color.White,
        background = Color(0xFFFFFFFF),
        onBackground = Color.Black
    )

    val DarkColors = darkColorScheme(
        primary = Color(0xFFBB86FC),
        onPrimary = Color.Black,
        background = Color(0xFF121212),
        onBackground = Color.White
    )

    var lightBlue = Color(0xFF2196F3)
    var pink = Color(0xFFE91E63)
    var deepPurple = Color(0xFF673AB7)
    var focusedSearchColor = Color(0xFF212121)
    var lightGray = Color(0xFFAEAEAE)
    var newsTitleColor = Color(0xFF212121)
    var descriptionColor = Color(0xFF8E8E8E)
    var appBarColor = Color(0x11555555)
    var selectedColor = Color(0xEEEEEEEE)
    var selectTextColor = Color(0xFFFFFFFF)


}