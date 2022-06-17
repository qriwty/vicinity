package com.lada.vicinity.modelviewer

import android.app.Application

class ModelViewerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ModelViewerApplication

        var currentModel: ModelViewer? = null
    }
}
