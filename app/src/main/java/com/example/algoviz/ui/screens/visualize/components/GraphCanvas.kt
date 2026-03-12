package com.example.algoviz.ui.screens.visualize.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algoviz.domain.engine.VisualizationState
import com.example.algoviz.domain.engine.VisualizationStep
import com.example.algoviz.ui.theme.DeepNavy
import com.example.algoviz.ui.theme.InfoBlue
import com.example.algoviz.ui.theme.MintAccent
import com.example.algoviz.ui.theme.OrangeAccent
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GraphCanvas(
    step: VisualizationStep?,
    modifier: Modifier = Modifier
) {
    if (step == null || step.state !is VisualizationState.GraphState) return
    val graphState = step.state as VisualizationState.GraphState

    val textMeasurer = rememberTextMeasurer()
    val onSurface = MaterialTheme.colorScheme.onSurface
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    // Pre-calculate node positions in a circle for simplicity out-of-the-box
    // In a production app, we could use a force-directed layout algorithm.
    val nodePositions = remember(graphState.nodes.size) {
        mutableMapOf<Int, Offset>()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2, height / 2)
        val radiusMultiplier = minOf(width, height) * 0.4f
        val nodeRadius = 24.dp.toPx()

        // Distribute nodes evenly in a circle if not already calculated
        if (nodePositions.isEmpty() || nodePositions.size != graphState.nodes.size) {
            val angleStep = (2 * Math.PI) / graphState.nodes.size
            graphState.nodes.forEachIndexed { index, node ->
                val angle = index * angleStep - (Math.PI / 2) // Start at top
                val x = center.x + (radiusMultiplier * cos(angle)).toFloat()
                val y = center.y + (radiusMultiplier * sin(angle)).toFloat()
                nodePositions[node.id] = Offset(x, y)
            }
        }

        // Draw Edges first so they are underneath nodes
        graphState.edges.forEach { edge ->
            val fromPos = nodePositions[edge.from] ?: return@forEach
            val toPos = nodePositions[edge.to] ?: return@forEach

            val pair = Pair(edge.from, edge.to)
            val reversePair = Pair(edge.to, edge.from)
            val isActive = graphState.activePathEdges.contains(pair) || 
                          (!edge.isDirected && graphState.activePathEdges.contains(reversePair))

            val edgeColor = if (isActive) OrangeAccent else surfaceVariant.copy(alpha = 0.5f)
            val strokeWidth = if (isActive) 4.dp.toPx() else 2.dp.toPx()

            drawLine(
                color = edgeColor,
                start = fromPos,
                end = toPos,
                strokeWidth = strokeWidth
            )

            // Draw edge weight if applicable
            if (edge.weight != null) {
                val midX = (fromPos.x + toPos.x) / 2
                val midY = (fromPos.y + toPos.y) / 2
                
                val weightText = textMeasurer.measure(
                    text = edge.weight,
                    style = TextStyle(color = MintAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                )
                
                drawText(
                    textLayoutResult = weightText,
                    topLeft = Offset(midX - (weightText.size.width / 2), midY - (weightText.size.height / 2))
                )
            }
        }

        // Draw Nodes
        graphState.nodes.forEach { node ->
            val pos = nodePositions[node.id] ?: return@forEach

            val isCurrent = node.id == graphState.currentNode
            val isVisited = graphState.visitedNodes.contains(node.id)

            val nodeColor = when {
                isCurrent -> OrangeAccent
                isVisited -> MintAccent
                else -> surfaceVariant
            }

            drawCircle(
                color = nodeColor,
                radius = nodeRadius,
                center = pos
            )

            val textResult = textMeasurer.measure(
                text = node.value,
                style = TextStyle(
                    color = if (nodeColor == surfaceVariant) onSurface else DeepNavy, 
                    fontSize = 18.sp, 
                    fontWeight = FontWeight.Bold
                )
            )

            drawText(
                textLayoutResult = textResult,
                topLeft = Offset(pos.x - (textResult.size.width / 2), pos.y - (textResult.size.height / 2))
            )
        }
    }
}
