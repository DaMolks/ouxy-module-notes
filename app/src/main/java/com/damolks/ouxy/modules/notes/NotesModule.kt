package com.damolks.ouxy.modules.notes

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.damolks.ouxy.module.ModuleContext
import com.damolks.ouxy.module.OuxyModule
import com.damolks.ouxy.modules.notes.data.Note
import com.damolks.ouxy.modules.notes.data.NotesStorage
import com.damolks.ouxy.modules.notes.databinding.ModuleNotesMainBinding
import com.damolks.ouxy.modules.notes.ui.NoteEditDialog
import com.damolks.ouxy.modules.notes.ui.NotesAdapter
import kotlinx.coroutines.launch

class NotesModule : OuxyModule {
    private lateinit var context: ModuleContext
    private lateinit var binding: ModuleNotesMainBinding
    private lateinit var storage: NotesStorage
    private lateinit var adapter: NotesAdapter
    private lateinit var editDialog: NoteEditDialog
    
    override fun initialize(context: ModuleContext) {
        this.context = context
        storage = NotesStorage(context)
        binding = ModuleNotesMainBinding.inflate(LayoutInflater.from(context.getContext()))
        editDialog = NoteEditDialog(context.getContext())
        setupViews()
        loadNotes()
    }

    override fun getMainView(): View = binding.root

    override fun cleanup() {
        // Nettoyage si nécessaire
    }

    private fun setupViews() {
        adapter = NotesAdapter(::onNoteClick)
        binding.notesList.apply {
            layoutManager = LinearLayoutManager(context.getContext())
            adapter = this@NotesModule.adapter
        }
        
        binding.addNoteFab.setOnClickListener {
            showNoteDialog()
        }
    }

    private fun loadNotes() {
        context.lifecycleScope.launch {
            val notes = storage.getAllNotes()
            adapter.submitList(notes)
        }
    }

    private fun onNoteClick(note: Note) {
        showNoteDialog(note)
    }

    private fun showNoteDialog(note: Note? = null) {
        editDialog.show(note) { savedNote ->
            context.lifecycleScope.launch {
                storage.saveNote(savedNote)
                loadNotes()
            }
        }
    }
}