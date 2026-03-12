package com.example.algoviz.ui.screens.visualize.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algoviz.domain.engine.TreeNode
import com.example.algoviz.domain.engine.VisualizationState
import com.example.algoviz.domain.engine.VisualizationStep
import com.example.algoviz.ui.theme.DeepNavy
import com.example.algoviz.ui.theme.InfoBlue
import com.example.algoviz.ui.theme.MintAccent
import com.example.algoviz.ui.theme.OrangeAccent
import kotlin.math.max

@Composable
fun TreeCanvas(
    step: VisualizationStep?,
    modifier: Modifier = Modifier
) {
    if (step == null || step.state !is VisualizationState.TreeState) return
    val treeState = step.state as VisualizationState.TreeState

    val textMeasurer = rememberTextMeasurer()
    val onSurface = MaterialTheme.colorScheme.onSurface
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val radius = 22.dp.toPx()
        val verticalSpacing = 60.dp.toPx()
        val topPadding = 40.dp.toPx()

        if (treeState.root != null) {
            val depth = maxOf(getTreeDepth(treeState.root), 1)
            // Draw edges first
            drawTreeEdges(
                node = treeState.root,
                x = width / 2,
                y = topPadding,
                xOffset = width / 4, // Initial horizontal spread
                yOffset = verticalSpacing,
                surfaceVariant = surfaceVariant
            )

            // Draw nodes
            drawTreeNodes(
                node = treeState.root,
                x = width / 2,
                y = topPadding,
                xOffset = width / 4,
                yOffset = verticalSpacing,
                radius = radius,
                state = treeState,
                onSurface = onSurface,
                surfaceVariant = surfaceVariant,
                textMeasurer = textMeasurer
            )
        }
    }
}

// Helper to determine depth
private fun getTreeDepth(node: TreeNode?): Int {
    if (node == null) return 0
    return 1 + max(getTreeDepth(node.left), getTreeDepth(node.right))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTreeEdges(
    node: TreeNode,
    x: Float,
    y: Float,
    xOffset: Float,
    yOffset: Float,
    surfaceVariant: Color
) {
    val nextY = y + yOffset
    val strokeWidth = 2.dp.toPx()
    val edgeColor = surfaceVariant.copy(alpha = 0.6f)

    if (node.left != null) {
        val nextX = x - xOffset
        drawLine(
            color = edgeColor,
            start = Offset(x, y),
            end = Offset(nextX, nextY),
            strokeWidth = strokeWidth
        )
        drawTreeEdges(node.left!!, nextX, nextY, xOffset / 2, yOffset, surfaceVariant)
    }

    if (node.right != null) {
        val nextX = x + xOffset
        drawLine(
            color = edgeColor,
            start = Offset(x, y),
            end = Offset(nextX, nextY),
            strokeWidth = strokeWidth
        )
        drawTreeEdges(node.right!!, nextX, nextY, xOffset / 2, yOffset, surfaceVariant)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTreeNodes(
    node: TreeNode,
    x: Float,
    y: Float,
    xOffset: Float,
    yOffset: Float,
    radius: Float,
    state: VisualizationState.TreeState,
    onSurface: Color,
    surfaceVariant: Color,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    // Determine color
    val isHighlighted = state.highlightedNodes.contains(node.id)
    val isComparing = state.comparingNode == node.id
    val isFound = state.foundNode == node.id

    val nodeColor = when {
        isFound -> MintAccent
        isComparing -> OrangeAccent
        isHighlighted -> InfoBlue
        else -> surfaceVariant
    }

    // Draw the circle
    drawCircle(
        color = nodeColor,
        radius = radius,
        center = Offset(x, y)
    )

    // Draw value text
    val textResult = textMeasurer.measure(
        text = node.value.toString(),
        style = TextStyle(
            color = if (nodeColor == surfaceVariant) onSurface else DeepNavy,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    )

    drawText(
        textLayoutResult = textResult,
        topLeft = Offset(x - (textResult.size.width / 2), y - (textResult.size.height / 2))
    )

    // Recursive calls
    val nextY = y + yOffset
    if (node.left != null) {
        drawTreeNodes(node.left!!, x - xOffset, nextY, xOffset / 2, yOffset, radius, state, onSurface, surfaceVariant, textMeasurer)
    }
    if (node.right != null) {
        drawTreeNodes(node.right!!, x + xOffset, nextY, xOffset / 2, yOffset, radius, state, onSurface, surfaceVariant, textMeasurer)
    }
}
