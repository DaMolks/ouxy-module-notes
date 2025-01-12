package com.damolks.ouxy.modules.notes.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.damolks.ouxy.modules.notes.data.Note
import com.damolks.ouxy.modules.notes.databinding.DialogEditNoteBinding
import java.util.UUID

class NoteEditDialog(private val context: Context) {

    fun show(note: Note? = null, onSave: (Note) -> Unit) {
        val binding = DialogEditNoteBinding.inflate(context.layoutInflater)
        
        // Pré-remplir avec les données existantes si c'est une modification
        note?.let {
            binding.titleInput.setText(it.title)
            binding.contentInput.setText(it.content)
            binding.tagsInput.setText(it.tags.joinToString(", "))
        }

        MaterialAlertDialogBuilder(context)
            .setTitle(if (note == null) "Nouvelle Note" else "Éditer la Note")
            .setView(binding.root)
            .setPositiveButton("Enregistrer") { _, _ ->
                val newNote = Note(
                    id = note?.id ?: UUID.randomUUID().toString(),
                    title = binding.titleInput.text.toString(),
                    content = binding.contentInput.text.toString(),
                    tags = binding.tagsInput.text.toString()
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() },
                    createdAt = note?.createdAt ?: System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                onSave(newNote)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}