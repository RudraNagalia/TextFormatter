package com.kedia.textstyling

import android.graphics.Typeface
import android.text.Spannable
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.EditText
import androidx.core.text.getSpans
import kotlinx.android.synthetic.main.activity_main.view.*

class TextFormatter {

}

fun EditText.textFormatter(textFormats: List<TextFormat>) {

    val emptyPair = Pair(-1, -1)

    if (textFormats.isEmpty()) {
        logE("You should pass at least one text format")
        return
    }

    val characterFormatMap = textFormats.associate {
        it.character to it.textStyle
    }

    val charactersPairList: MutableMap<Char, MutableList<Pair<Int, Int>>> = mutableMapOf()
    val positionPairList: MutableMap<Char, Pair<Int, Int>> = mutableMapOf()

    this.addTextChangedListener(CharacterWatcher(object : CharacterWatcher.OnSequenceChanged {
        override fun characterAdded(
            index: Int,
            addedCharacter: Char?,
            sequence: CharSequence?
        ) {
            addedCharacter?.let {
                if (it in characterFormatMap.keys) {

                    /**
                     * If pair = Pair(-1,-1), then check for the pair in the list
                     * of pairs too
                     */
                    var pair = emptyPair
                    if ((positionPairList.get(it) ?: emptyPair) == emptyPair) {
                        if (charactersPairList.containsKey(it)) {
                            pair = charactersPairList.get(it)?.last() ?: emptyPair
                        }
                    } else {
                        pair = positionPairList.get(it) ?: emptyPair
                    }

                    if (pair.isComplete()) {
                        if (!pair.contains(index)) {
                            pair = emptyPair
                        }
                    }

                    if (pair.first == -1) {
                        pair = Pair(index, -1)
                    }
                    else if (pair.second == -1) {
                        charactersPairList?.get(it)?.remove(pair)
                        if (charactersPairList.get(it)?.isEmpty() == true)
                            charactersPairList.remove(it)
                        pair = Pair(pair.first, index)
                        if (charactersPairList.containsKey(it).not())
                            charactersPairList[it] = mutableListOf()

                        charactersPairList[it]?.add(pair)
                        pair = emptyPair
                    }
                    positionPairList[it] = pair

                    if (charactersPairList.isNotEmpty()) {
                        for (character in charactersPairList.keys) {
                            val list = charactersPairList.get(character) ?: listOf()
                            val style = characterFormatMap.get(character)
                            val span = getStyleSpan(style)
                            for (addedPair in list) {
                                if (addedPair.first == -1 || addedPair.second == -1)
                                    return
                                span?.let {
                                    this@textFormatter.text.setSpan(it, addedPair.first + 1, addedPair.second, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                }
                            }
                        }
                    }
                }
            }
        }

        override fun characterDeleted(
            index: Int,
            deletedCharacter: Char?,
            sequence: CharSequence?
        ) {
            deletedCharacter?.let {
                if (it in characterFormatMap.keys) {
                    var pair = positionPairList.get(it) ?: emptyPair
                    if (pair == emptyPair)
                        pair = if (charactersPairList.containsKey(it)) charactersPairList.get(it)?.last() ?: emptyPair else emptyPair

                    logE("ca;;ed up $pair $charactersPairList $positionPairList")

                    charactersPairList.get(it)?.remove(pair)
                    if (charactersPairList.get(it)?.isEmpty() == true)
                        charactersPairList.remove(it)
                    if (pair.second != -1) {
                        pair = Pair(pair.first, -1)
                        if (charactersPairList.containsKey(it).not())
                            charactersPairList[it] = mutableListOf()
                        charactersPairList?.get(it)?.add(pair)
                    } else {
                        pair = emptyPair
                    }
                    logE("called $charactersPairList $positionPairList")

                    if (charactersPairList.isNotEmpty()) {
                        for (character in charactersPairList.keys) {
                            val list = charactersPairList.get(character) ?: listOf<Pair<Int, Int>>()
                            val style = characterFormatMap.get(character)
                            val span = getStyleSpan(style)
                            for (addedPair in list) {
                                span?.let {
                                    if (addedPair.second == -1) {
                                        val addedSpan = this@textFormatter.text.getSpans<CharacterStyle>(addedPair.first)
                                        if (addedSpan.isNotEmpty()) {
                                            this@textFormatter.text.removeSpan(addedSpan.first())
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }

    }))
}