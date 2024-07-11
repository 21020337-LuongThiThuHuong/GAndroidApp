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
import com.example.androidproject.databinding.FragmentStringBinding

class StringFragment : Fragment() {
    private var _binding: FragmentStringBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStringBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val editTextInput: EditText = binding.editTextInput
        val buttonOK: ImageView = binding.buttonOK
        val textViewResult: TextView = binding.textViewResult
        textViewResult.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.holo_red_dark,
            ),
        )
        textViewResult.text = "⚠️ Vui lòng nhập một chuỗi"

        buttonOK.setOnClickListener {
            val inputString = editTextInput.text.toString()

            if (inputString.isEmpty()) {
                textViewResult.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.holo_red_dark,
                    ),
                )
                textViewResult.text = "⚠️ Vui lòng nhập một chuỗi"
            } else {
                val charCountMap = mutableMapOf<Char, Int>()

                for (char in inputString) {
                    charCountMap[char] = charCountMap.getOrDefault(char, 0) + 1
                }

                val resultString =
                    charCountMap.entries
                        .mapIndexed { index, entry ->
                            "${index + 1}. " + "<b><font color='#34A049'>${entry.key}</font></b>: " +
                                "<font color='#6200EA'>${entry.value}</font>"
                        }.joinToString(separator = "<br>")

                textViewResult.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.black,
                    ),
                )
                textViewResult.text = Html.fromHtml(resultString, Html.FROM_HTML_MODE_COMPACT)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
