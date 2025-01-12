package com.damolks.ouxy.modules.notes.data

import com.damolks.ouxy.module.ModuleContext
import kotlinx.serialization.json.Json

class NotesStorage(private val context: ModuleContext) {
    private val notesFile = "notes.json"
    private val json = Json { prettyPrint = true }

    suspend fun saveNote(note: Note) {
        val notes = getAllNotes().toMutableList()
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
        } else {
            notes.add(note)
        }
        context.storage.writeText(notesFile, json.encodeToString(Notes.serializer(), Notes(notes)))
    }

    suspend fun getAllNotes(): List<Note> {
        return try {
            val content = context.storage.readText(notesFile)
            json.decodeFromString(Notes.serializer(), content).notes
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteNote(noteId: String) {
        val notes = getAllNotes().filter { it.id != noteId }
        context.storage.writeText(notesFile, json.encodeToString(Notes.serializer(), Notes(notes)))
    }

    @Serializable
    private data class Notes(val notes: List<Note>)
}