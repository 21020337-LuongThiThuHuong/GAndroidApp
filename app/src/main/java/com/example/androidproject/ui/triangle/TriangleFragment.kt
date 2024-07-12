package com.example.androidproject.ui.triangle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.androidproject.R
import com.example.androidproject.databinding.FragmentTriangleBinding

class TriangleFragment : Fragment() {
    private var _binding: FragmentTriangleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TriangleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTriangleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val editTextA: EditText = binding.editTextA
        val editTextX: EditText = binding.editTextX
        val editTextY: EditText = binding.editTextY
        val editTextZ: EditText = binding.editTextZ
        val buttonDraw: Button = binding.buttonDraw
        val coordinateView: CoordinateView = binding.coordinateView

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            if (error.isNotEmpty()) {
                binding.textViewResult.visibility = View.VISIBLE
                binding.textViewResult.text = error
                binding.textViewResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        })

        viewModel.resultString.observe(viewLifecycleOwner, Observer { result ->
            if (result.isNotEmpty()) {
                binding.textViewResult.visibility = View.VISIBLE
                binding.textViewResult.text = result
                binding.textViewResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_700))
            }
        })

        viewModel.points.observe(viewLifecycleOwner, Observer { points ->
            coordinateView.setPoints(points)
        })

        buttonDraw.setOnClickListener {
            val editTexts = listOf(
                "A" to editTextA.text.toString(),
                "X" to editTextX.text.toString(),
                "Y" to editTextY.text.toString(),
                "Z" to editTextZ.text.toString()
            )
            viewModel.processPoints(editTexts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
