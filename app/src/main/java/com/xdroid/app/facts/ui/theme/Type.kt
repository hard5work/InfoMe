package com.xdroid.app.facts.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.xdroid.app.facts.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)


val RubikFont = FontFamily(
    Font(R.font.rubik_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(R.font.rubik_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.rubik_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(R.font.rubik_semibold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
    Font(R.font.rubik_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(R.font.rubik_extrabold, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
    Font(R.font.rubik_black, weight = FontWeight.Black, style = FontStyle.Normal),
)

val normalText = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 1.sp,
    color = white
)

val appTitle = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = 1.sp,
    color = white
)
val quoteText = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 25.sp,
    lineHeight = 32.sp,
    letterSpacing = (1.5).sp,
    color = black
)


val title = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = 1.2.sp
)