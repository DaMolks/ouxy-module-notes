package com.damolks.ouxy.modules.notes

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.damolks.ouxy.module.ModuleContext
import com.damolks.ouxy.module.OuxyModule
import com.damolks.ouxy.modules.notes.data.Note
import com.damolks.ouxy.modules.notes.data.NotesStorage
import com.damolks.ouxy.modules.notes.databinding.ModuleNotesMainBinding
import com.damolks.ouxy.modules.notes.ui.NotesAdapter
import kotlinx.coroutines.launch
import java.util.UUID

class NotesModule : OuxyModule {
    private lateinit var context: ModuleContext
    private lateinit var binding: ModuleNotesMainBinding
    private lateinit var storage: NotesStorage
    private lateinit var adapter: NotesAdapter
    
    override fun initialize(context: ModuleContext) {
        this.context = context
        storage = NotesStorage(context)
        binding = ModuleNotesMainBinding.inflate(LayoutInflater.from(context.getContext()))
        setupViews()
        loadNotes()
    }

    override fun getMainView(): View = binding.root

    override fun cleanup() {
        // Nettoyage des ressources si nécessaire
    }

    private fun setupViews() {
        adapter = NotesAdapter(::onNoteClick)
        binding.notesList.apply {
            layoutManager = LinearLayoutManager(context.getContext())
            adapter = this@NotesModule.adapter
        }
        
        binding.addNoteFab.setOnClickListener {
            // Pour l'exemple, ajoute une note avec un UUID aléatoire
            val newNote = Note(
                id = UUID.randomUUID().toString(),
                title = "Nouvelle Note",
                content = "Contenu de la note",
                tags = listOf("tag1", "tag2"),
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            context.lifecycleScope.launch {
                storage.saveNote(newNote)
                loadNotes()
            }
        }
    }

    private fun loadNotes() {
        context.lifecycleScope.launch {
            val notes = storage.getAllNotes()
            adapter.submitList(notes)
        }
    }

    private fun onNoteClick(note: Note) {
        // Gérer le clic sur une note
        // Par exemple, ouvrir un dialogue d'édition
    }
}