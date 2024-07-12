package com.example.androidproject.ui.triangle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class CoordinateView(
    context: Context,
    attrs: AttributeSet?,
) : View(context, attrs) {
    private val decimalFormat = DecimalFormat("#.##")

    private val paint =
        Paint().apply {
            color = Color.BLACK
            strokeWidth = 5f
            textSize = 30f
        }

    private val redPaint =
        Paint().apply {
            color = Color.RED
            strokeWidth = 5f
            textSize = 30f
        }

    private val linePaint =
        Paint().apply {
            color = Color.BLUE
            strokeWidth = 5f
        }

    private var points: List<Triple<String, Float, Float>> = emptyList()

    private fun calculateScale(points: List<Triple<String, Float, Float>>): Float {
        val maxX = points.maxOfOrNull { it.second } ?: 0f
        val minX = points.minOfOrNull { it.second } ?: 0f
        val maxY = points.maxOfOrNull { it.third } ?: 0f
        val minY = points.minOfOrNull { it.third } ?: 0f

        val maxCoordinate = maxOf(maxX - minX, maxY - minY)
        return if (maxCoordinate > 0) minOf(width, height) / (2 * maxCoordinate) else 1f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y
            checkPoint(touchX, touchY)
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun checkPoint(
        touchX: Float,
        touchY: Float,
    ) {
        val viewModel =
            ViewModelProvider(context as FragmentActivity).get(TriangleViewModel::class.java)
        val scale = calculateScale(points)
        val centerX = width / 2
        val centerY = height / 2

        val actualX = (touchX - centerX) / scale
        val actualY = (centerY - touchY) / scale

        val touchPoint = Triple("chạm", actualX, actualY)

        val pointMap = points.associateBy { it.first }
        val trianglePoints =
            listOf(
                pointMap["X"],
                pointMap["Y"],
                pointMap["Z"],
            ).filterNotNull()

        if (trianglePoints.size == 3) {
            val result =
                viewModel.checkPointInTriangle(touchPoint.first, touchPoint, trianglePoints)
            Snackbar.make(this, result, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2
        val scale = calculateScale(points)

        canvas.drawLine(centerX, 0f, centerX, height, paint)
        canvas.drawLine(0f, centerY, width, centerY, paint)

        val pointMap = points.associateBy { it.first }
        val pointX = pointMap["X"]
        val pointY = pointMap["Y"]
        val pointZ = pointMap["Z"]

        if (pointX != null && pointY != null && pointZ != null) {
            val scaledX = pointX.second * scale
            val scaledY = pointX.third * scale
            val scaledYx = pointY.second * scale
            val scaledYy = pointY.third * scale
            val scaledZx = pointZ.second * scale
            val scaledZy = pointZ.third * scale

            canvas.drawLine(
                centerX + scaledX,
                centerY - scaledY,
                centerX + scaledYx,
                centerY - scaledYy,
                linePaint,
            )
            canvas.drawLine(
                centerX + scaledYx,
                centerY - scaledYy,
                centerX + scaledZx,
                centerY - scaledZy,
                linePaint,
            )
            canvas.drawLine(
                centerX + scaledZx,
                centerY - scaledZy,
                centerX + scaledX,
                centerY - scaledY,
                linePaint,
            )
        }

        points.forEach { (name, x, y) ->
            val scaledX = x * scale
            val scaledY = y * scale
            if (name == "A") {
                canvas.drawCircle(centerX + scaledX, centerY - scaledY, 10f, redPaint)
                canvas.drawText(
                    "$name(${decimalFormat.format(x)};${decimalFormat.format(y)})",
                    centerX + scaledX + 15,
                    centerY - scaledY - 15,
                    redPaint,
                )
            } else {
                canvas.drawCircle(centerX + scaledX, centerY - scaledY, 10f, paint)
                canvas.drawText(
                    "$name(${decimalFormat.format(x)};${decimalFormat.format(y)})",
                    centerX + scaledX + 15,
                    centerY - scaledY - 15,
                    paint,
                )
            }
        }
    }

    fun setPoints(points: List<Triple<String, Float, Float>>) {
        this.points = points
        invalidate()
    }
}
