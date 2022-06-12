package com.lada.vicinity

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.lada.vicinity.databinding.FragmentNewModelBinding
import java.io.File
import com.google.gson.Gson

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewModelFragment : Fragment() {

    private var _binding: FragmentNewModelBinding? = null

    private val binding get() = _binding!!

    private var model: Model? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewModelBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDefaultValues()

        binding.buttonNewModelPrevious.setOnClickListener {
            findNavController().navigate(R.id.action_NewModelFragment_to_MenuFragment)
        }

        binding.editNewModelName.doAfterTextChanged { it ->
            updateName(it)
        }

        binding.buttonNewModelNext.setOnClickListener {
            createNewModel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isModelNameValid(name: String): Boolean {
        return name.matches("^\\w+\$?".toRegex())
    }

    private fun showNameError(name: String) {
        val errorBar = binding.newModelInfo

        errorBar.text = getString(R.string.new_model_name_error, name)
        errorBar.visibility = View.VISIBLE
    }

    private fun hideNameError() {
        val errorBar = binding.newModelInfo

        if (errorBar.visibility == View.VISIBLE) {
            errorBar.visibility = View.GONE
        }
    }

    private fun setDefaultValues() {
        val defaultName = getString(R.string.new_model_name_default)
        val modelTitle = binding.newModelName

        modelTitle.text = getString(R.string.new_model_name, defaultName)

        model = Model(defaultName)
    }

    private fun updateName(it: Editable?) {
        val inputText = it.toString()

        if (isModelNameValid(inputText)) {
            hideNameError()

            binding.newModelName.text = getString(R.string.new_model_name, inputText)
            model = Model(inputText)

        } else {
            showNameError(inputText)
        }

    }

    private fun createFolder(name: String): File {
        val filesDir = activity?.filesDir
        val folder = File(filesDir, name)

        folder.mkdir()

        return folder
    }

    private fun writeModel(model: Model, folder: File, fileName: String) {
        val file = folder.resolve(File(fileName))
        val gson = Gson()
        val gsonModel = gson.toJson(model)

        file.writeText(gsonModel)
    }

    private fun createNewModel() {
        val folder = createFolder(model!!.name)

        Toast.makeText(activity, folder.path, Toast.LENGTH_LONG).show()

        writeModel(model!!, folder, "config.json")



    }

}