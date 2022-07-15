package com.kedia.textstyling

import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.style.*
import android.util.Log

fun logE(message: String, tag: String = "TextStyling") {
    Log.e(tag, message)
}

fun getStyleSpan(textStyle: TextStyle?): CharacterStyle? {
    return when(textStyle) {
        TextStyle.BOLD -> StyleSpan(Typeface.BOLD)
        TextStyle.ITALICS -> StyleSpan(Typeface.ITALIC)
        TextStyle.UNDERLINE -> UnderlineSpan()
        TextStyle.STRIKETHROUGH -> StrikethroughSpan()
        null -> null
    }
}

fun Pair<Int, Int>.isComplete(): Boolean {
    return this.first != -1 && this.second != -1
}

fun Triple<Int, Int, Any>.isComplete(): Boolean {
    return this.first != -1 && this.second != -1
}

fun Pair<Int, Int>.contains(index: Int): Boolean {
    return this.first < index && this.second > index
}

fun Triple<Int, Int, Any>.contains(index: Int): Boolean {
    return this.first < index && this.second > index
}