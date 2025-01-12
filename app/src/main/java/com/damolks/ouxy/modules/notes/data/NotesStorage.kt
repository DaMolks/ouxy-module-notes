package com.damolks.ouxy.modules.notes.data

import com.damolks.ouxy.module.ModuleContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

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
        saveNotes(notes)
    }

    suspend fun getAllNotes(): List<Note> {
        return try {
            val content = context.storage.readText(notesFile)
            json.decodeFromString<NotesList>(content).notes
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteNote(noteId: String) {
        val notes = getAllNotes().filter { it.id != noteId }
        saveNotes(notes)
    }

    private suspend fun saveNotes(notes: List<Note>) {
        context.storage.writeText(notesFile, json.encodeToString(NotesList.serializer(), NotesList(notes)))
    }

    @Serializable
    private data class NotesList(val notes: List<Note>)
}