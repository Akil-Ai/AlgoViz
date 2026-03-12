package com.example.algoviz.domain.engine.algorithms

import com.example.algoviz.domain.engine.AlgorithmVisualizer
import com.example.algoviz.domain.engine.VisualizationState
import com.example.algoviz.domain.engine.VisualizationStep

class HeapSortVisualizer : AlgorithmVisualizer {

    @Suppress("UNCHECKED_CAST")
    override fun visualize(initialData: Any): List<VisualizationStep> {
        val array = initialData as? List<Int>
            ?: throw IllegalArgumentException("Heap Sort expects List<Int>")

        val steps = mutableListOf<VisualizationStep>()
        val arr = array.toIntArray()
        val n = arr.size

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(array = arr.toList()),
                explanation = "Initializing Heap Sort. Step 1: Build a Max Heap."
            )
        )

        // Build max heap
        for (i in n / 2 - 1 downTo 0) {
            heapify(arr, n, i, steps)
        }

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(array = arr.toList()),
                explanation = "Max Heap built successfully. The largest element is now at the root (index 0)."
            )
        )

        // Extract elements from heap one by one
        val sortedSet = mutableSetOf<Int>()
        for (i in n - 1 downTo 1) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(0, i),
                        sortedIndices = sortedSet.toSet()
                    ),
                    explanation = "Swapping the root element ${arr[0]} (max) with the last element ${arr[i]}."
                )
            )

            // Swap current root with end
            val temp = arr[0]
            arr[0] = arr[i]
            arr[i] = temp
            
            sortedSet.add(i)

            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(0, i),
                        swapped = true,
                        sortedIndices = sortedSet.toSet()
                    ),
                    explanation = "${arr[i]} is now in its sorted position at the end of the reduced heap."
                )
            )

            // Call max heapify on the reduced heap
            heapify(arr, i, 0, steps, sortedSet)
        }
        
        sortedSet.add(0)

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(
                    array = arr.toList(),
                    sortedIndices = sortedSet.toSet()
                ),
                explanation = "Heap Sort is complete. The array is fully sorted."
            )
        )

        return steps
    }

    private fun heapify(arr: IntArray, n: Int, i: Int, steps: MutableList<VisualizationStep>, sortedSet: Set<Int> = emptySet()) {
        var largest = i
        val left = 2 * i + 1
        val right = 2 * i + 2

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(
                    array = arr.toList(),
                    comparingIndices = listOf(i),
                    sortedIndices = sortedSet
                ),
                explanation = "Heapifying at root node index $i (${arr[i]})."
            )
        )

        if (left < n && arr[left] > arr[largest]) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(largest, left),
                        sortedIndices = sortedSet
                    ),
                    explanation = "Left child ${arr[left]} is greater than current largest ${arr[largest]}."
                )
            )
            largest = left
        }

        if (right < n && arr[right] > arr[largest]) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(largest, right),
                        sortedIndices = sortedSet
                    ),
                    explanation = "Right child ${arr[right]} is greater than current largest ${arr[largest]}."
                )
            )
            largest = right
        }

        if (largest != i) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(i, largest),
                        swapped = true,
                        sortedIndices = sortedSet
                    ),
                    explanation = "Swapping root ${arr[i]} with the largest child ${arr[largest]}."
                )
            )

            val swap = arr[i]
            arr[i] = arr[largest]
            arr[largest] = swap

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest, steps, sortedSet)
        }
    }
}
