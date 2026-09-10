package com.vidyutgati

import android.app.Application
import com.vidyutgati.core.database.VidyutDatabase

class VidyutGatiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Room Database eagerly
        VidyutDatabase.getInstance(this)
    }
}
