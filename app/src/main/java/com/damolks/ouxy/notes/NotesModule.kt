package com.damolks.ouxy.notes

import android.view.View
import com.damolks.ouxy.module.ModuleContext
import com.damolks.ouxy.module.OuxyModule

class NotesModule : OuxyModule {
    private lateinit var context: ModuleContext

    override fun initialize(context: ModuleContext) {
        this.context = context
    }

    override fun getMainView(): View {
        return NotesView(context.applicationContext)
    }

    override fun cleanup() {
        // Nettoyage des ressources si nécessaire
    }
}