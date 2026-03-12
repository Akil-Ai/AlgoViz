package com.example.algoviz.domain.engine.algorithms

import com.example.algoviz.domain.engine.AlgorithmVisualizer
import com.example.algoviz.domain.engine.VisualizationState
import com.example.algoviz.domain.engine.VisualizationStep

class BinarySearchVisualizer : AlgorithmVisualizer {

    @Suppress("UNCHECKED_CAST")
    override fun visualize(initialData: Any): List<VisualizationStep> {
        val payload = initialData as? Pair<List<Int>, Int>
            ?: throw IllegalArgumentException("Binary Search expects Pair<List<Int>, Int> where Int is the target.")

        val array = payload.first.sorted() // Binary search requires a sorted array
        val target = payload.second
        val steps = mutableListOf<VisualizationStep>()

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(array = array),
                explanation = "Initializing Binary Search for target $target. Array must be sorted."
            )
        )

        var left = 0
        var right = array.size - 1
        var found = false

        while (left <= right) {
            val mid = left + (right - left) / 2

            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = array,
                        comparingIndices = listOf(mid),
                        sortedIndices = setOf(left, right) // Just reusing sortedIndices color as bounds pointers
                    ),
                    explanation = "Calculating midpoint at index $mid (${array[mid]}). Left bound is $left, right bound is $right."
                )
            )

            if (array[mid] == target) {
                steps.add(
                    VisualizationStep(
                        state = VisualizationState.ArrayState(
                            array = array,
                            sortedIndices = setOf(mid) // Highlight found element green
                        ),
                        explanation = "Target $target found at index $mid!"
                    )
                )
                found = true
                break
            }

            if (array[mid] < target) {
                steps.add(
                    VisualizationStep(
                        state = VisualizationState.ArrayState(
                            array = array,
                            comparingIndices = listOf(mid)
                        ),
                        explanation = "${array[mid]} is less than $target. The target must be in the right half."
                    )
                )
                left = mid + 1
            } else {
                steps.add(
                    VisualizationStep(
                        state = VisualizationState.ArrayState(
                            array = array,
                            comparingIndices = listOf(mid)
                        ),
                        explanation = "${array[mid]} is greater than $target. The target must be in the left half."
                    )
                )
                right = mid - 1
            }
        }

        if (!found) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(array = array),
                    explanation = "The bounds crossed. Target $target does not exist in the array."
                )
            )
        }

        return steps
    }
}
