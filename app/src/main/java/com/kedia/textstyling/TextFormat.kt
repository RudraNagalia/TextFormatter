package com.kedia.textstyling

data class TextFormat(
    val character: Char,
    val textStyle: TextStyle
)


enum class TextStyle {
    BOLD,
    ITALICS,
    UNDERLINE,
    STRIKETHROUGH
}
