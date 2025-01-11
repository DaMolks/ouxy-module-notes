package com.damolks.ouxy.notes

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.damolks.ouxy.notes.databinding.ViewNotesBinding

class NotesView(context: Context) : LinearLayout(context) {
    private val binding = ViewNotesBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        orientation = VERTICAL
    }
}