package com.damolks.ouxy.modules.notes

import android.view.View
import android.widget.LinearLayout
import com.damolks.ouxy.module.ModuleContext
import com.damolks.ouxy.module.OuxyModule
import com.damolks.ouxy.modules.notes.data.Note
import com.damolks.ouxy.modules.notes.data.NotesStorage
import kotlinx.coroutines.launch

class NotesModule : OuxyModule {
    private lateinit var context: ModuleContext
    private lateinit var storage: NotesStorage
    private lateinit var mainView: LinearLayout
    
    override fun initialize(context: ModuleContext) {
        this.context = context
        this.storage = NotesStorage(context.storage)
        this.mainView = LinearLayout(context.context).apply {
            orientation = LinearLayout.VERTICAL
        }
        
        loadNotes()
        
        // Notification quand une note est créée
        context.registerEventHandler("request_note_creation") { data ->
            val title = data["title"] as? String ?: return@registerEventHandler
            val content = data["content"] as? String ?: return@registerEventHandler
            createNote(title, content)
        }
    }
    
    override fun getMainView(): View = mainView
    
    override fun cleanup() {
        // Nettoyage si nécessaire
    }
    
    private fun loadNotes() {
        context.lifecycleScope.launch {
            try {
                val notes = storage.getAllNotes()
                notes.forEach { note ->
                    notifyNoteAvailable(note)
                }
            } catch (e: Exception) {
                // Gestion d'erreur
            }
        }
    }
    
    private fun createNote(title: String, content: String) {
        context.lifecycleScope.launch {
            try {
                val currentTime = System.currentTimeMillis()
                val note = Note(
                    id = java.util.UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    tags = emptyList(),
                    createdAt = currentTime,
                    updatedAt = currentTime
                )
                storage.saveNote(note)
                notifyNoteAvailable(note)
            } catch (e: Exception) {
                // Gestion d'erreur
            }
        }
    }
    
    private fun notifyNoteAvailable(note: Note) {
        context.sendEvent("note_available", mapOf(
            "id" to note.id,
            "title" to note.title,
            "content" to note.content,
            "createdAt" to note.createdAt
        ))
    }
}