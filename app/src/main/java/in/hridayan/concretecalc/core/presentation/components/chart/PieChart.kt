package `in`.hridayan.concretecalc.core.presentation.components.chart

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.hridayan.concretecalc.core.presentation.model.PieChartData

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    data: List<PieChartData>
) {
    val total = data.sumOf { it.value.toDouble() }.toFloat()

    var startAngle = -90f

    Canvas(modifier = modifier) {
        data.forEach { item ->
            val sweepAngle = (item.value / total) * 360f
            drawArc(
                color = item.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}
