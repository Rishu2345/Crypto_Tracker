package com.buildsol.cryptotracker.presentation.coinDetails.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.buildsol.cryptotracker.domain.model.ChartPoint
import com.buildsol.cryptotracker.ui.theme.priceColors

@Composable
fun PriceChangeGraph(
    dataPoints: List<ChartPoint>,
    modifier: Modifier = Modifier,
    graphColor: Color = defaultTrendColor(dataPoints)
) {
    if (dataPoints.isEmpty()) return


    val animatable = remember(dataPoints) { Animatable(0f) }
    LaunchedEffect(dataPoints) {
        animatable.snapTo(0f)
        animatable.animateTo(1f, animationSpec = tween(durationMillis = 15000))
    }
    val animationProgress = animatable.value

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val minX = dataPoints.first().timestamp
        val maxX = dataPoints.last().timestamp
        val minY = dataPoints.minOf { it.value }
        val maxY = dataPoints.maxOf { it.value }

        val xRange = (maxX - minX).takeIf { it != 0L } ?: 1L
        val yRange = (maxY - minY).takeIf { it != 0.0 } ?: 1.0

        val graphOffset: (ChartPoint) -> Offset = { point ->
            val fractionX = (point.timestamp - minX).toFloat() / xRange.toFloat()
            val fractionY = (point.value - minY).toFloat() / yRange.toFloat()
            Offset(
                x = fractionX * canvasWidth,
                y = (1f - fractionY) * canvasHeight
            )
        }

        val linePath = Path()
        dataPoints.forEachIndexed { index, point ->
            val offset = graphOffset(point)
            if (index == 0)
                linePath.moveTo(offset.x, offset.y)
            else
                linePath.lineTo(offset.x, offset.y)
        }

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(linePath, false)

        val animatedLinePath = Path()
        pathMeasure.getSegment(
            startDistance = 0f,
            stopDistance = pathMeasure.length * animationProgress,
            destination = animatedLinePath,
            startWithMoveTo = true
        )
        val fillPath = Path().apply {
            addPath(animatedLinePath)
            val lastPoint = graphOffset(dataPoints.last())
            val firstPoint = graphOffset(dataPoints.first())
            val revealedEndX = firstPoint.x + (lastPoint.x - firstPoint.x) * animationProgress
            lineTo(revealedEndX, canvasHeight)
            lineTo(firstPoint.x, canvasHeight)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(graphColor.copy(alpha = 0.25f), Color.Transparent)
            )
        )

        drawPath(
            path = animatedLinePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
private fun defaultTrendColor(dataPoints: List<ChartPoint>): Color {
    val colors = MaterialTheme.priceColors
    if (dataPoints.size < 2) return colors.chartLinePositive
    val trendingUp = dataPoints.last().value >= dataPoints.first().value
    return if (trendingUp) colors.chartLinePositive else colors.chartLineNegative
}

@Preview(showBackground = true)
@Composable
private fun PriceChangeGraphPreview() {
    val sampleData = listOf(
        ChartPoint(0, 100.0),
        ChartPoint(1, 120.0),
        ChartPoint(2, 110.0),
        ChartPoint(3, 140.0),
        ChartPoint(4, 130.0),
        ChartPoint(5, 170.0),
        ChartPoint(6, 160.0),
        ChartPoint(7, 200.0),
        ChartPoint(8, 180.0),
        ChartPoint(9, 220.0)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
    ) {
        PriceChangeGraph(
            dataPoints = sampleData,
            modifier = Modifier.fillMaxSize()
        )
    }
}