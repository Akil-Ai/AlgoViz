package com.example.algoviz.domain.engine.algorithms

import com.example.algoviz.domain.engine.AlgorithmVisualizer
import com.example.algoviz.domain.engine.VisualizationState
import com.example.algoviz.domain.engine.VisualizationStep

class LinearSearchVisualizer : AlgorithmVisualizer {

    @Suppress("UNCHECKED_CAST")
    override fun visualize(initialData: Any): List<VisualizationStep> {
        val payload = initialData as? Pair<List<Int>, Int>
            ?: throw IllegalArgumentException("Linear Search expects Pair<List<Int>, Int> where Int is the target.")

        val array = payload.first
        val target = payload.second
        val steps = mutableListOf<VisualizationStep>()

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(array = array),
                explanation = "Initializing Linear Search to find target value: $target."
            )
        )

        var found = false
        for (i in array.indices) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = array,
                        comparingIndices = listOf(i)
                    ),
                    explanation = "Checking index $i. Is ${array[i]} equal to $target?"
                )
            )

            if (array[i] == target) {
                steps.add(
                    VisualizationStep(
                        state = VisualizationState.ArrayState(
                            array = array,
                            sortedIndices = setOf(i) // Green highlight
                        ),
                        explanation = "Target $target found at index $i!"
                    )
                )
                found = true
                break
            }
        }

        if (!found) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(array = array),
                    explanation = "Reached the end of the array. Target $target was not found."
                )
            )
        }

        return steps
    }
}
