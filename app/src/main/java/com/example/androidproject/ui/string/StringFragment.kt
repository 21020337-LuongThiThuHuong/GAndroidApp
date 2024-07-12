package com.example.androidproject.ui.string

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.androidproject.databinding.FragmentStringBinding

class StringFragment : Fragment() {
    private var _binding: FragmentStringBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StringViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val editTextInput: EditText = binding.editTextInput
        val buttonOK: ImageView = binding.buttonOK
        val textViewResult: TextView = binding.textViewResult

        viewModel.resultString.observe(
            viewLifecycleOwner,
            Observer { result ->
                textViewResult.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.black,
                    ),
                )
                textViewResult.text = Html.fromHtml(result, Html.FROM_HTML_MODE_COMPACT)
            },
        )

        viewModel.errorMessage.observe(
            viewLifecycleOwner,
            Observer { error ->
                textViewResult.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.holo_red_dark,
                    ),
                )
                textViewResult.text = error
            },
        )

        buttonOK.setOnClickListener {
            val inputString = editTextInput.text.toString()
            viewModel.processInputString(inputString)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
