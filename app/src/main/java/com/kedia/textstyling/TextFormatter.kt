package com.kedia.textstyling

import android.graphics.Typeface
import android.text.Spannable
import android.text.style.StyleSpan
import android.util.Log
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.view.*

class TextFormatter {

}

fun EditText.textFormatter() {
    val list: MutableList<Pair<Int, Int>> = mutableListOf()
    var pair: Pair<Int, Int> = Pair(-1, -1)
    this.addTextChangedListener(CharacterWatcher(object : CharacterWatcher.OnSequenceChanged {
        override fun characterAdded(
            index: Int,
            addedCharacter: Char?,
            sequence: CharSequence?
        ) {
            if (addedCharacter == '*') {
                if (pair.first == -1)
                    pair = Pair(index, -1)
                else if (pair.second == -1) {
                    pair = Pair(pair.first, index)
                    list.add(pair)
                    pair = Pair(-1, -1)
                }

            }
//                Log.d("TAG!!!!", "called $index")
            Log.d("TAG!!!!", "called $addedCharacter $index ${addedCharacter == '*'} $pair $list")

            if (list.isNotEmpty()) {
                for (pair in list) {
                    this@textFormatter.text.setSpan(StyleSpan(Typeface.BOLD), pair.first + 1, pair.second, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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