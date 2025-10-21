package `in`.hridayan.concretecalc.concrete.mix_design.presentation.components.canvas

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000.CurvePoint
import `in`.hridayan.concretecalc.core.presentation.components.text.AutoResizeableText


@SuppressLint("DefaultLocale")
@Composable
fun WaterCementGraph(curves: List<List<CurvePoint>>) {
    val curveColors = listOf(
        Color.Red, Color.Green, Color.Blue
    )
    val curveLabels = listOf("Curve 1 (OPC 33)", "Curve 2 (OPC 43)", "Curve 3 (OPC 53)")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Relationship Between Free Water-Cement Ratio\nand 28-Day Compressive Strength",
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(8.dp)
        ) {
            val onSurfaceColor = MaterialTheme.colorScheme.onSurface

            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Graph bounds
                val xMin = 0.25f
                val xMax = 0.65f
                val yMin = 0f
                val yMax = 80f

                // Padding inside canvas
                val paddingLeft = 80f
                val paddingBottom = 60f
                val paddingRight = 30f
                val paddingTop = 30f

                val graphWidth = width - paddingLeft - paddingRight
                val graphHeight = height - paddingTop - paddingBottom

                fun mapX(x: Double): Float =
                    (paddingLeft + ((x - xMin) / (xMax - xMin)) * graphWidth).toFloat()

                fun mapY(y: Double): Float =
                    (height - paddingBottom - ((y - yMin) / (yMax - yMin)) * graphHeight).toFloat()

                // Draw grid lines and labels
                val xSteps = listOf(0.25, 0.30, 0.35, 0.40, 0.45, 0.50, 0.55, 0.60, 0.65)
                val ySteps = (0..80 step 10).toList()

                val gridColor = Color.LightGray.copy(alpha = 0.5f)

                // Vertical grid lines (X-axis divisions)
                xSteps.forEach { xVal ->
                    val x = mapX(xVal)
                    drawLine(
                        color = gridColor,
                        start = Offset(x, paddingTop),
                        end = Offset(x, height - paddingBottom),
                        strokeWidth = 1f
                    )
                }

                // Horizontal grid lines (Y-axis divisions)
                ySteps.forEach { yVal ->
                    val y = mapY(yVal.toDouble())
                    drawLine(
                        color = gridColor,
                        start = Offset(paddingLeft, y),
                        end = Offset(width - paddingRight, y),
                        strokeWidth = 1f
                    )
                }

                // Draw Axes
                val axisColor = Color.Black
                drawLine(
                    color = axisColor,
                    start = Offset(paddingLeft, paddingTop),
                    end = Offset(paddingLeft, height - paddingBottom),
                    strokeWidth = 2f
                )
                drawLine(
                    color = axisColor,
                    start = Offset(paddingLeft, height - paddingBottom),
                    end = Offset(width - paddingRight, height - paddingBottom),
                    strokeWidth = 2f
                )

                // Label the X-axis
                xSteps.forEach { xVal ->
                    drawContext.canvas.nativeCanvas.drawText(
                        String.format("%.2f", xVal),
                        mapX(xVal) - 15,
                        height - paddingBottom / 3,
                        Paint().apply {
                            color = onSurfaceColor.toArgb()
                            textSize = 28f
                        }
                    )
                }

                // Label the Y-axis
                ySteps.forEach { yVal ->
                    drawContext.canvas.nativeCanvas.drawText(
                        yVal.toString(),
                        paddingLeft - 60,
                        mapY(yVal.toDouble()) + 8,
                        Paint().apply {
                            color = onSurfaceColor.toArgb()
                            textSize = 28f
                        }
                    )
                }

                // Axis Titles
                drawContext.canvas.nativeCanvas.drawText(
                    "Free Water-Cement Ratio",
                    (width / 2) - 120,
                    height + 30,
                    Paint().apply {
                        color = onSurfaceColor.toArgb()
                        textSize = 32f
                        isFakeBoldText = true
                    }
                )

                drawContext.canvas.nativeCanvas.save()
                drawContext.canvas.nativeCanvas.rotate(-90f, 30f, height / 2)
                drawContext.canvas.nativeCanvas.drawText(
                    "28-Day Compressive Strength (N/mm²)",
                    height / 3,
                    30f,
                    Paint().apply {
                        color = onSurfaceColor.toArgb()
                        textSize = 32f
                        isFakeBoldText = true
                    }
                )
                drawContext.canvas.nativeCanvas.restore()

                curves.forEachIndexed { index, curve ->
                    val path = Path()
                    curve.forEachIndexed { i, point ->
                        val x = mapX(point.wcRatio)
                        val y = mapY(point.targetStrength)
                        if (i == 0) path.moveTo(x, y)
                        else path.lineTo(x, y)
                    }
                    drawPath(
                        path = path,
                        color = curveColors[index],
                        style = Stroke(width = 4f, cap = StrokeCap.Round)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            curveLabels.forEachIndexed { index, label ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(curveColors[index])
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AutoResizeableText(text = label, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
