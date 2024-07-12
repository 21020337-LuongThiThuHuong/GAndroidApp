package com.example.androidproject.ui.triangle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.androidproject.R
import com.example.androidproject.databinding.FragmentTriangleBinding

class TriangleFragment : Fragment() {
    private var _binding: FragmentTriangleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTriangleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
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

        buttonDraw.setOnClickListener {
            val points =
                listOf(
                    getCoordinatesFromEditText("A", editTextA),
                    getCoordinatesFromEditText("X", editTextX),
                    getCoordinatesFromEditText("Y", editTextY),
                    getCoordinatesFromEditText("Z", editTextZ),
                )

            binding.textViewResult.visibility = View.VISIBLE

            if (points.any { it == null }) {
                binding.textViewResult.text = "Bạn chưa nhập đủ các trường tọa độ 4 điểm"
                binding.textViewResult.setTextColor(resources.getColor(R.color.red, null))
            } else {
                val validPoints = points.filterNotNull()
                val pointA = validPoints.first { it.first == "A" }
                val trianglePoints = validPoints.filter { it.first != "A" }

                val result = checkPointInTriangle(pointA, trianglePoints)
                binding.textViewResult.text = result
                binding.textViewResult.setTextColor(resources.getColor(R.color.purple_700, null))
            }

            coordinateView.setPoints(points.filterNotNull())
        }
    }

    private fun getCoordinatesFromEditText(
        name: String,
        editText: EditText,
    ): Triple<String, Float, Float>? {
        val text = editText.text.toString()
        val coords = text.split(",")
        return if (coords.size == 2) {
            val x = coords[0].trim().toFloatOrNull()
            val y = coords[1].trim().toFloatOrNull()
            if (x != null && y != null) {
                Triple(name, x, y)
            } else {
                null
            }
        } else {
            null
        }
    }

    private fun checkPointInTriangle(
        pointA: Triple<String, Float, Float>,
        trianglePoints: List<Triple<String, Float, Float>>,
    ): String {
        val (aX, aY) = Pair(pointA.second, pointA.third)
        val (xX, xY) = Pair(trianglePoints[0].second, trianglePoints[0].third)
        val (yX, yY) = Pair(trianglePoints[1].second, trianglePoints[1].third)
        val (zX, zY) = Pair(trianglePoints[2].second, trianglePoints[2].third)

        val areaXYZ = calculateArea(xX, xY, yX, yY, zX, zY)
        val areaAXY = calculateArea(aX, aY, xX, xY, yX, yY)
        val areaAYZ = calculateArea(aX, aY, yX, yY, zX, zY)
        val areaAZX = calculateArea(aX, aY, zX, zY, xX, xY)

        return when {
            areaXYZ == 0f -> "XYZ không phải là hình tam giác"
            else -> {
                if (areaAXY + areaAYZ + areaAZX == areaXYZ) {
                    if (areaAXY == 0f || areaAYZ == 0f || areaAZX == 0f) {
                        "Kết quả: Điểm A nằm trên cạnh của tam giác XYZ"
                    } else {
                        "Kết quả: Điểm A nằm trong tam giác XYZ"
                    }
                } else {
                    "Kết quả: Điểm A nằm ngoài tam giác XYZ"
                }
            }
        }
    }

    private fun calculateArea(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        x3: Float,
        y3: Float,
    ): Float = Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0).toFloat()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
