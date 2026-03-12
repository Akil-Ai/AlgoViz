package com.example.algoviz.domain.engine

/**
 * Represents a single discrete step in an algorithm visualization.
 * 
 * @property state The structural state of the data (Array, Graph, Tree).
 * @property explanation A human-readable plain text explanation of what is happening in this step.
 */
data class VisualizationStep(
    val state: VisualizationState,
    val explanation: String = ""
)

/**
 * Interface for all algorithm visualizers.
 */
interface AlgorithmVisualizer {
    /**
     * Generates a sequence of steps mapping the algorithm's execution based on an initial state.
     */
    fun visualize(initialData: Any): List<VisualizationStep>
}
