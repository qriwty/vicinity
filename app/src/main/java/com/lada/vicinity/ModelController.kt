package com.lada.vicinity

import android.content.Context
import com.google.gson.Gson
import com.lada.vicinity.utils.Model
import java.io.File


class ModelController {
    private val configFilename: String = "config.cfg"

    fun getOutputDirectory(context: Context, name: String): File {
        val filesDir = context.filesDir
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, name).apply { mkdirs() } }

        if (mediaDir == null) {
            val internalDir = File(filesDir, name)
            internalDir.mkdir()

            return internalDir
        }

        return mediaDir
    }

    fun writeModel(model: Model, folder: File, fileName: String) {
        val file = folder.resolve(File(fileName))
        val gson = Gson()
        val gsonModel = gson.toJson(model)

        file.writeText(gsonModel)
    }

    fun createModelConfig(context: Context, model: Model) {
        val folder = model.name?.let { getOutputDirectory(context, it) }

        if (folder != null) {
            writeModel(model, folder, configFilename)
        }
    }

}