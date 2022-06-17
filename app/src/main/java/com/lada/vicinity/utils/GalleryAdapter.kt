package com.lada.vicinity.utils

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lada.vicinity.ModelController
import com.lada.vicinity.R

class GalleryAdapter(private val context: Context) : RecyclerView.Adapter<GalleryAdapter.ModelViewHolder>() {

    private val modelController = ModelController()

    private val list = modelController.getModelsList(context)

    inner class ModelViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(model: String) {
            val textField = view.findViewById<TextView>(R.id.item_text)
            val imageField = view.findViewById<ImageView>(R.id.item_image)

            textField.text = model
            setGalleryThumbnail(model, imageField)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        layout.accessibilityDelegate = Accessibility
        return ModelViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val item = list.get(position)

        holder.bind(item)

        // IN PROGRESS
        holder.itemView.setOnClickListener {
            // DO SOMETHING
        }

    }

    private fun setGalleryThumbnail(modelName: String, imageView: ImageView) {
        val modelDirectory = modelController.getModelDirectory(context, modelName)
        val photoPreview = modelDirectory?.let { modelController.getModelPhotoPreview(it) }

        Glide.with(context).load(photoPreview).into(imageView)

    }

    companion object Accessibility : View.AccessibilityDelegate() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onInitializeAccessibilityNodeInfo(
            host: View?,
            info: AccessibilityNodeInfo?
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            val customString = host?.context?.getString(R.string.look_up_words)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info?.addAction(customClick)
        }
    }
}