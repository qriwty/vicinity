package com.lada.vicinity

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.lada.vicinity.fragments.CameraFragment
import com.lada.vicinity.utils.Model
import java.io.File
import java.util.*


class ModelController {
    private val gson = Gson()
    private val configFilename: String = "config.cfg"

    fun getModelsList(context: Context): ArrayList<String> {
//        val internalDir = context.filesDir
        val mediaDir = context.getExternalFilesDir(null)

        val modelsName: ArrayList<String> = ArrayList()
        mediaDir?.walk()?.forEach {
            if (it.isDirectory && File(it, configFilename).exists()) {
                val model = readModel(it, configFilename)

                modelsName.add(model.name.toString())
            }
        }

        return modelsName
    }

    fun getModelName(model: Model): String? {
        return model.name
    }

    fun getModelDirectory(context: Context, directoryName: String): File? {
        val mediaDir = context.getExternalFilesDir(null)

        mediaDir?.walk()?.forEach {
            if (it.isDirectory && it.name.equals(directoryName)) {
                return it
            }
        }

        return mediaDir
    }

    fun getModelPhotoPreview(directory: File): Uri? {
        directory.listFiles { file ->
            CameraFragment.EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
        }?.maxOrNull()?.let {
            return Uri.fromFile(it)
        }

        return null
    }

    fun getOutputDirectory(context: Context, model: Model): File {
        val modelName: String? = getModelName(model)
        val filesDir = context.filesDir
//        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
//            File(it, modelName).apply { mkdirs() } }

        val mediaDir = File(context.getExternalFilesDir(null), modelName)
        mediaDir.mkdirs()

        if (!mediaDir.exists()) {
            val internalDir = File(filesDir, modelName)
            internalDir.mkdir()

            return internalDir
        }

        return mediaDir
    }

    fun writeModel(model: Model, folder: File, fileName: String) {
        val file = folder.resolve(File(fileName))
        val gsonModel = gson.toJson(model)

        file.writeText(gsonModel)
    }

    fun readModel(folder: File, fileName: String): Model {
        val file = folder.resolve(File(fileName))

        val model = gson.fromJson(file.readText(), Model::class.java)

        return model
    }

    fun createModelConfig(context: Context, model: Model) {
        val folder = getOutputDirectory(context, model)

        writeModel(model, folder, configFilename)
    }

    fun deleteModel(context: Context, model: String) {
        val modelDirectory = getModelDirectory(context, model)

        modelDirectory?.deleteRecursively()

    }

}