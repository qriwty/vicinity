package com.lada.vicinity.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.lada.vicinity.databinding.FragmentNewModelBinding
import com.lada.vicinity.utils.Model
import com.lada.vicinity.ModelController
import com.lada.vicinity.PhotoActivity
import com.lada.vicinity.R

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewModelFragment : Fragment() {

    private var _binding: FragmentNewModelBinding? = null

    private val binding get() = _binding!!

    private var model: Model? = null

    private val modelController = ModelController()

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
            context?.let { context ->
                model?.let { model ->
                    modelController.createModelConfig(context, model)
                }
            }

            val bundle = bundleOf("model" to model)

            val intent = Intent(activity, PhotoActivity::class.java)
            intent.putExtras(bundle)

            startActivity(intent)

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

}