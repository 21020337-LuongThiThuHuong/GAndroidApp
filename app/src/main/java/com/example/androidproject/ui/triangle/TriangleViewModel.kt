package com.example.androidproject.ui.triangle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TriangleViewModel : ViewModel() {
    private val _resultString = MutableLiveData<String>()
    val resultString: LiveData<String> get() = _resultString

    private val _points = MutableLiveData<List<Triple<String, Float, Float>>>()
    val points: LiveData<List<Triple<String, Float, Float>>> get() = _points

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun processPoints(editTexts: List<Pair<String, String>>) {
        val points = mutableListOf<Triple<String, Float, Float>?>()
        var errorMessage: String? = null
        var emptyCount = 0

        for ((name, text) in editTexts) {
            if (text.isEmpty()) {
                emptyCount++
                points.add(null)
            } else {
                val point = getCoordinatesFromString(name, text)
                points.add(point)
                if (point == null && errorMessage == null) {
                    errorMessage =
                        "Bạn nhập sai định dạng điểm: $name. Định dạng đúng là x,y (VD: 2.5,3.0)"
                }
            }
        }

        _points.value = points.filterNotNull()

        if (errorMessage != null) {
            _errorMessage.value = errorMessage
            _resultString.value = ""
        } else if (emptyCount > 0) {
            _errorMessage.value = "Bạn chưa nhập đủ các trường tọa độ 4 điểm"
            _resultString.value = ""
        } else {
            val validPoints = points.filterNotNull()
            val pointA = validPoints.first { it.first == "A" }
            val trianglePoints = validPoints.filter { it.first != "A" }

            val result = checkPointInTriangle("A", pointA, trianglePoints)
            Log.d("TriangleViewModel", "Result: $result")
            _resultString.value = result
            _errorMessage.value = ""
        }
    }

    private fun getCoordinatesFromString(
        name: String,
        text: String,
    ): Triple<String, Float, Float>? {
        val coords = text.split(",")
        return if (coords.size == 2) {
            val x = coords[0].trim().toFloatOrNull()
            val y = coords[1].trim().toFloatOrNull()
            if (x != null && y != null) {
                Triple(name, x, y)
            } else {
                _errorMessage.value =
                    "Bạn nhập sai định dạng điểm: $name. Định dạng đúng là x,y (VD: 2.5,3.0)"
                null
            }
        } else {
            _errorMessage.value =
                "Bạn nhập sai định dạng điểm: $name. Định dạng đúng là x,y (VD: 2.5,3.0)"
            null
        }
    }

    internal fun checkPointInTriangle(
        pointName: String,
        touchPoint: Triple<String, Float, Float>,
        trianglePoints: List<Triple<String, Float, Float>>,
    ): String {
        val (touchX, touchY) = Pair(touchPoint.second, touchPoint.third)
        val (xX, xY) = Pair(trianglePoints[0].second, trianglePoints[0].third)
        val (yX, yY) = Pair(trianglePoints[1].second, trianglePoints[1].third)
        val (zX, zY) = Pair(trianglePoints[2].second, trianglePoints[2].third)

        val areaXYZ = calculateArea(xX, xY, yX, yY, zX, zY)
        val areaAXY = calculateArea(touchX, touchY, xX, xY, yX, yY)
        val areaAYZ = calculateArea(touchX, touchY, yX, yY, zX, zY)
        val areaAZX = calculateArea(touchX, touchY, zX, zY, xX, xY)

        return when {
            areaXYZ == 0f -> "XYZ không phải là hình tam giác"
            areaAXY + areaAYZ + areaAZX == areaXYZ -> {
                if (areaAXY == 0f || areaAYZ == 0f || areaAZX == 0f) {
                    "Kết quả: Điểm $pointName nằm trên cạnh của tam giác XYZ"
                } else {
                    "Kết quả: Điểm $pointName nằm trong tam giác XYZ"
                }
            }

            else -> "Kết quả: Điểm $pointName nằm ngoài tam giác XYZ"
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
}
