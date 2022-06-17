package com.lada.vicinity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.lada.vicinity.databinding.ActivityModelBinding
import com.lada.vicinity.modelviewer.ModelSurfaceView
import com.lada.vicinity.modelviewer.ModelViewer
import com.lada.vicinity.modelviewer.ModelViewerApplication
import com.lada.vicinity.modelviewer.stl.StlModel
import java.io.IOException

class ModelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModelBinding
    private lateinit var sampleModels: List<String>
    private var sampleModelIndex = 0
    private var modelView: ModelSurfaceView? = null
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerView)) { _, insets ->
            (binding.progressBar.layoutParams as FrameLayout.LayoutParams).apply {
                topMargin = insets.systemWindowInsetTop
                bottomMargin = insets.systemWindowInsetBottom
                leftMargin = insets.systemWindowInsetLeft
                rightMargin = insets.systemWindowInsetRight
            }
            insets.consumeSystemWindowInsets()
        }

        sampleModels = assets.list("")!!.filter { it.endsWith(".stl") }

    }

    override fun onStart() {
        super.onStart()
        createNewModelView(ModelViewerApplication.currentModel)
        if (ModelViewerApplication.currentModel != null) {
            title = ModelViewerApplication.currentModel!!.title
        }

        loadSampleModel()
    }

    override fun onPause() {
        super.onPause()
        modelView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        modelView?.onResume()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == OPEN_DOCUMENT_REQUEST && resultCode == RESULT_OK && resultData!!.data != null) {
            val uri = resultData.data
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun createNewModelView(model: ModelViewer?) {
        if (modelView != null) {
            binding.containerView.removeView(modelView)
        }
        modelView = ModelSurfaceView(this, model)
        binding.containerView.addView(modelView, 0)
    }

    private fun setCurrentModel(model: ModelViewer) {
        createNewModelView(model)
        Toast.makeText(applicationContext, R.string.open_model_success, Toast.LENGTH_SHORT).show()
        title = model.title
        binding.progressBar.visibility = View.GONE
    }

    private fun loadSampleModel() {
        try {
            val stream = assets.open(sampleModels[sampleModelIndex++ % sampleModels.size])
            setCurrentModel(StlModel(stream))
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val OPEN_DOCUMENT_REQUEST = 101
    }
}