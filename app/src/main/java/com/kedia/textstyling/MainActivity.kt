package com.kedia.textstyling

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = mutableListOf<TextFormat>()
        list.apply {
            add(TextFormat('*', TextStyle.BOLD))
            add(TextFormat('~', TextStyle.ITALICS))
        }
        edit.textFormatter(list)
    }
}