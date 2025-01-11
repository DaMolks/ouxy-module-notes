package com.damolks.ouxy.notes

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.damolks.ouxy.module.StorageApi
import com.damolks.ouxy.notes.databinding.ViewNotesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesView(
    context: Context,
    private val storage: StorageApi
) : LinearLayout(context) {
    private val binding = ViewNotesBinding.inflate(LayoutInflater.from(context), this, true)
    private val scope = CoroutineScope(Dispatchers.Main)

    companion object {
        private const val KEY_LAST_NOTE = "last_note"
    }

    init {
        orientation = VERTICAL
        setupViews()
        loadLastNote()
    }

    private fun setupViews() {
        binding.saveButton.setOnClickListener {
            val note = binding.noteInput.text?.toString()
            if (!note.isNullOrBlank()) {
                saveNote(note)
            }
        }
    }

    private fun saveNote(note: String) {
        scope.launch {
            try {
                storage.save(KEY_LAST_NOTE, note)
                Toast.makeText(context, "Note sauvegardée", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadLastNote() {
        scope.launch {
            try {
                val lastNote = storage.get(KEY_LAST_NOTE, "")
                binding.noteInput.setText(lastNote)
            } catch (e: Exception) {
                Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}