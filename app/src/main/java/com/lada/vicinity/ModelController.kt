package com.lada.vicinity

import android.content.Context
import com.google.gson.Gson
import com.lada.vicinity.utils.Model
import java.io.File


class ModelController {
    private val configFilename: String = "config.cfg"

    fun getModelName(model: Model): String? {
        return model.name
    }

    fun getOutputDirectory(context: Context, model: Model): File {
        val modelName: String? = getModelName(model)
        val filesDir = context.filesDir
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, modelName).apply { mkdirs() } }

        if (mediaDir == null) {
            val internalDir = File(filesDir, modelName)
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
        val folder = getOutputDirectory(context, model)

        writeModel(model, folder, configFilename)
    }

}