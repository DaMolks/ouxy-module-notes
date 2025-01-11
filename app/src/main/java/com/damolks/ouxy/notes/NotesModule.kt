package com.damolks.ouxy.notes

import android.view.View
import com.damolks.ouxy.module.ModuleContext
import com.damolks.ouxy.module.OuxyModule

class NotesModule : OuxyModule {
    private lateinit var context: ModuleContext
    private lateinit var notesView: NotesView

    override fun initialize(context: ModuleContext) {
        this.context = context
        this.notesView = NotesView(context.applicationContext, context.storage)
    }

    override fun getMainView(): View = notesView

    override fun cleanup() {
        // Pas de ressources à nettoyer pour l'instant
    }
}