package com.example.acpia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1F8ED6),    // Vibrant Blue (Cyan)
    secondary = Color(0xFF7A6CF1),  // Violet
    tertiary = Color(0xFFF49E0B),   // Amber
    background = Color(0xFFF4FBFF), // Soft Light Blue Background
    surface = Color(0xFFFFFFFF),    // White Panels
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF11324C), // Deep Navy Text
    onSurface = Color(0xFF11324C),
    surfaceVariant = Color(0xFFF4F9FC), // Panel 2
    onSurfaceVariant = Color(0xFF5D7890), // Muted Text
    outline = Color(0xFFD9EAF2),      // Border
    error = Color(0xFFFF6B5F),      // Red
    primaryContainer = Color(0xFFDFF2FF), // Cyan Dim
)

@Composable
fun ACPIATheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
