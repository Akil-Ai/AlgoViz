package com.example.algoviz.domain.engine.algorithms

import com.example.algoviz.domain.engine.AlgorithmVisualizer
import com.example.algoviz.domain.engine.VisualizationState
import com.example.algoviz.domain.engine.VisualizationStep

class QuickSortVisualizer : AlgorithmVisualizer {

    @Suppress("UNCHECKED_CAST")
    override fun visualize(initialData: Any): List<VisualizationStep> {
        val array = initialData as? List<Int>
            ?: throw IllegalArgumentException("Quick Sort expects List<Int>")

        val steps = mutableListOf<VisualizationStep>()
        val arr = array.toIntArray()

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(array = arr.toList()),
                explanation = "Initializing Quick Sort."
            )
        )

        quickSort(arr, 0, arr.size - 1, steps)

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(
                    array = arr.toList(),
                    sortedIndices = arr.indices.toSet()
                ),
                explanation = "Quick Sort is complete. The array is fully sorted."
            )
        )

        return steps
    }

    private fun quickSort(arr: IntArray, low: Int, high: Int, steps: MutableList<VisualizationStep>) {
        if (low < high) {
            val pivotIndex = partition(arr, low, high, steps)
            
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        sortedIndices = setOf(pivotIndex)
                    ),
                    explanation = "Pivot ${arr[pivotIndex]} is now in its correct sorted position."
                )
            )

            quickSort(arr, low, pivotIndex - 1, steps)
            quickSort(arr, pivotIndex + 1, high, steps)
        }
    }

    private fun partition(arr: IntArray, low: Int, high: Int, steps: MutableList<VisualizationStep>): Int {
        val pivot = arr[high]
        var i = low - 1

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(
                    array = arr.toList(),
                    comparingIndices = listOf(high)
                ),
                explanation = "Selecting $pivot as the pivot element."
            )
        )

        for (j in low until high) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(j, high)
                    ),
                    explanation = "Comparing ${arr[j]} with pivot $pivot."
                )
            )

            if (arr[j] < pivot) {
                i++
                if (i != j) {
                    val temp = arr[i]
                    arr[i] = arr[j]
                    arr[j] = temp
                    
                    steps.add(
                        VisualizationStep(
                            state = VisualizationState.ArrayState(
                                array = arr.toList(),
                                comparingIndices = listOf(i, j),
                                swapped = true
                            ),
                            explanation = "${arr[i]} is less than $pivot. Swapping with the element at index $i."
                        )
                    )
                }
            }
        }

        val temp = arr[i + 1]
        arr[i + 1] = arr[high]
        arr[high] = temp

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(
                    array = arr.toList(),
                    comparingIndices = listOf(i + 1, high),
                    swapped = true
                ),
                explanation = "Moving the pivot $pivot into its final place by swapping with ${arr[high]}."
            )
        )

        return i + 1
    }
}
