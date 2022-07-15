package com.kedia.textstyling

import android.graphics.Typeface
import android.text.Spannable
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.EditText
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

//    val list: MutableList<Pair<Int, Int>> = mutableListOf()
//    var pair: Pair<Int, Int> = Pair(-1, -1)

    this.addTextChangedListener(CharacterWatcher(object : CharacterWatcher.OnSequenceChanged {
        override fun characterAdded(
            index: Int,
            addedCharacter: Char?,
            sequence: CharSequence?
        ) {
            addedCharacter?.let {
                if (it in characterFormatMap.keys) {
                    var pair = positionPairList.get(it) ?: emptyPair
                    /**
                     * If pair = Pair(-1,-1), then check for the pair in the list
                     * of pairs too
                     */

                    Log.d("TAG!!!!", "pair $it $pair $positionPairList")

                    if (pair.first == -1) {
                        pair = Pair(index, -1)
                    }
                    else if (pair.second == -1) {
                        pair = Pair(pair.first, index)
                        if (charactersPairList.containsKey(it).not())
                            charactersPairList[addedCharacter] = mutableListOf()

                        charactersPairList[it]?.add(pair)
                        pair = emptyPair
                    }
                    positionPairList[it] = pair
                    Log.d("TAG!!!!", "called $charactersPairList $pair")
                }
            }

            if (charactersPairList.isNotEmpty()) {
                for (character in charactersPairList.keys) {
                    val list = charactersPairList.get(character) ?: listOf<Pair<Int, Int>>()
                    val style = characterFormatMap.get(character)
                    val span = when(style) {
                        TextStyle.BOLD -> StyleSpan(Typeface.BOLD)
                        TextStyle.ITALICS -> StyleSpan(Typeface.ITALIC)
                        TextStyle.UNDERLINE -> UnderlineSpan()
                        null -> null
                    }
                    for (pair in list) {
                        span?.let {
                            this@textFormatter.text.setSpan(it, pair.first + 1, pair.second, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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

        }

    }))
}