package com.example.algoviz.domain.engine.algorithms

import com.example.algoviz.domain.engine.AlgorithmVisualizer
import com.example.algoviz.domain.engine.VisualizationState
import com.example.algoviz.domain.engine.VisualizationStep

class MergeSortVisualizer : AlgorithmVisualizer {

    @Suppress("UNCHECKED_CAST")
    override fun visualize(initialData: Any): List<VisualizationStep> {
        val array = initialData as? List<Int>
            ?: throw IllegalArgumentException("Merge Sort expects List<Int>")

        val steps = mutableListOf<VisualizationStep>()
        val arr = array.toIntArray()
        
        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(array = arr.toList()),
                explanation = "Initializing Merge Sort with an array of ${arr.size} elements."
            )
        )

        mergeSort(arr, 0, arr.size - 1, steps)

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(
                    array = arr.toList(),
                    sortedIndices = arr.indices.toSet()
                ),
                explanation = "Merge Sort is complete. The array is fully sorted."
            )
        )

        return steps
    }

    private fun mergeSort(arr: IntArray, left: Int, right: Int, steps: MutableList<VisualizationStep>) {
        if (left < right) {
            val middle = left + (right - left) / 2

            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = (left..right).toList()
                    ),
                    explanation = "Dividing the subarray from index $left to $right at midpoint $middle."
                )
            )

            mergeSort(arr, left, middle, steps)
            mergeSort(arr, middle + 1, right, steps)
            merge(arr, left, middle, right, steps)
        }
    }

    private fun merge(arr: IntArray, left: Int, middle: Int, right: Int, steps: MutableList<VisualizationStep>) {
        val n1 = middle - left + 1
        val n2 = right - middle

        val leftArr = IntArray(n1)
        val rightArr = IntArray(n2)

        for (i in 0 until n1) leftArr[i] = arr[left + i]
        for (j in 0 until n2) rightArr[j] = arr[middle + 1 + j]

        var i = 0
        var j = 0
        var k = left

        steps.add(
            VisualizationStep(
                state = VisualizationState.ArrayState(
                    array = arr.toList(),
                    comparingIndices = (left..right).toList()
                ),
                explanation = "Merging two sorted subarrays: ${leftArr.toList()} and ${rightArr.toList()} back into the main array from index $left to $right."
            )
        )

        while (i < n1 && j < n2) {
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(left + i, middle + 1 + j)
                    ),
                    explanation = "Comparing left subarray element ${leftArr[i]} with right subarray element ${rightArr[j]}."
                )
            )

            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i]
                i++
            } else {
                arr[k] = rightArr[j]
                j++
            }
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(k),
                        swapped = true
                    ),
                    explanation = "Placed ${arr[k]} at index $k."
                )
            )
            k++
        }

        while (i < n1) {
            arr[k] = leftArr[i]
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(k)
                    ),
                    explanation = "Copying remaining element ${arr[k]} from the left subarray."
                )
            )
            i++
            k++
        }

        while (j < n2) {
            arr[k] = rightArr[j]
            steps.add(
                VisualizationStep(
                    state = VisualizationState.ArrayState(
                        array = arr.toList(),
                        comparingIndices = listOf(k)
                    ),
                    explanation = "Copying remaining element ${arr[k]} from the right subarray."
                )
            )
            j++
            k++
        }
    }
}
