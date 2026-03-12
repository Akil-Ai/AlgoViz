package com.example.algoviz.domain.engine

object AlgorithmDataProvider {

    val algorithmInfoMap = mapOf(
        "bubble_sort" to AlgorithmInfo(
            title = "Bubble Sort",
            description = "Bubble Sort is a simple comparison-based sorting algorithm. It repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order. The pass through the list is repeated until the list is sorted.",
            steps = listOf(
                "Start at the beginning of the array.",
                "Compare the first two elements. If the first is greater than the second, swap them.",
                "Move to the next pair of elements and do the same.",
                "Continue to the end of the array. After the first pass, the largest element 'bubbles' up to the end.",
                "Repeat the entire process for the remaining elements until no more swaps are needed."
            ),
            timeComplexity = "Worst: O(n²), Best: O(n) (if already sorted)",
            spaceComplexity = "O(1) Auxiliary Space",
            useCases = "Due to its poor O(n²) efficiency, it is rarely used in real-world large-scale applications. It does however remain a great educational tool for grasping array swapping conceptually.",
            youtubeVideoId = "xli_FI7CuzA" // Bro Code Bubble Sort
        ),
        "merge_sort" to AlgorithmInfo(
            title = "Merge Sort",
            description = "Merge Sort is a highly efficient, stable, divide-and-conquer sorting algorithm. It works by recursively breaking down a list into smaller sublists until each sublist consists of a single element, and then merging those sublists in a manner that results exactly into a newly sorted list.",
            steps = listOf(
                "Divide the unsorted list into n sublists, each containing one element (a list of one element is considered sorted).",
                "Repeatedly merge sublists to produce new sorted sublists until there is only one sorted list remaining. This will be the sorted list."
            ),
            timeComplexity = "Worst: O(n log n), Best: O(n log n)",
            spaceComplexity = "O(n) - Requires extra memory for the temporary arrays",
            useCases = "Excellent for sorting linked lists because it requires no random access. Widely used as the standard system sort in many languages (like Java's Collections.sort for objects) due to its mathematical stability.",
            youtubeVideoId = "bOk35XmHPKs" // Bro Code Merge Sort
        ),
        "quick_sort" to AlgorithmInfo(
            title = "Quick Sort",
            description = "Quick Sort is another Divide and Conquer algorithm. It picks an element as a pivot and partitions the given array around the picked pivot. There are many different versions of quickSort that pick pivot in different ways (first, last, random, median).",
            steps = listOf(
                "Pick a pivot element.",
                "Partition the array such that all elements smaller than the pivot are to its left, and all elements greater are to its right.",
                "Recursively apply the same process to the left and right sub-arrays."
            ),
            timeComplexity = "Worst: O(n²) (rare), Average: O(n log n)",
            spaceComplexity = "O(log n) Call Stack Space",
            useCases = "Often the fastest practical sorting algorithm for in-memory arrays because its inner loop can be efficiently implemented on most architectures, and it operates entirely in-place.",
            youtubeVideoId = "Vtckgz38QHs" // Bro Code Quick Sort
        ),
        "heap_sort" to AlgorithmInfo(
            title = "Heap Sort",
            description = "Heap sort is a comparison-based sorting technique based on a Binary Heap data structure. It can be thought of as an improved selection sort where it divides its input into a sorted and an unsorted region, and it iteratively shrinks the unsorted region by extracting the largest element and moving that to the sorted region.",
            steps = listOf(
                "Build a max heap from the input data.",
                "At this point, the largest item is stored at the root of the heap. Replace it with the last item of the heap followed by reducing the size of heap by 1.",
                "Heapify the root of the tree.",
                "Repeat until the size of the heap is greater than 1."
            ),
            timeComplexity = "Worst: O(n log n), Best: O(n log n)",
            spaceComplexity = "O(1) In-place",
            useCases = "Used when memory is tightly constrained or when a guaranteed worst-case O(n log n) is required without massive allocations (like in embedded systems).",
            youtubeVideoId = "2DmK_H7IdTo" // Michael Sambol Heap Sort
        ),
        "binary_search" to AlgorithmInfo(
            title = "Binary Search",
            description = "Binary Search is a search algorithm that finds the position of a target value within a sorted array. It compares the target value to the middle element of the array. If they are not equal, the half in which the target cannot lie is eliminated and the search continues on the remaining half.",
            steps = listOf(
                "Compare target to the middle element.",
                "If the target matches the middle element, we return the mid index.",
                "If target is greater, the target can only lie in the right half subarray after the mid element. Recalculate the boundaries and mid.",
                "If target is smaller, search the left half."
            ),
            timeComplexity = "Worst: O(log n), Best: O(1)",
            spaceComplexity = "O(1) Iterative, O(log n) Recursive",
            useCases = "Massively useful for fast lookups in large, sorted databases or dictionaries. Reduces searching 1,000,000 items to just ~20 comparisons.",
            youtubeVideoId = "vqwGwaDHaHA" // Bro Code Binary Search
        ),
        "linear_search" to AlgorithmInfo(
            title = "Linear Search",
            description = "Linear search is the simplest search algorithm. It sequentially checks each element of the list until a match is found or the whole list has been searched.",
            steps = listOf(
                "Start from the leftmost element of array and one by one compare the target with each element.",
                "If the target matches with an element, return the index.",
                "If target doesn't match with any of elements, return -1."
            ),
            timeComplexity = "Worst: O(n), Best: O(1)",
            spaceComplexity = "O(1)",
            useCases = "Best for searching small lists, or completely unsorted data where pre-sorting is impossible or unfeasible.",
            youtubeVideoId = "C46QfTjVCNU" // Bro Code Linear Search
        ),
        "bfs" to AlgorithmInfo(
            title = "Breadth-First Search (BFS)",
            description = "BFS is an algorithm for traversing or searching tree or graph data structures. It starts at the tree root (or some arbitrary node of a graph) and explores all of the neighbor nodes at the present depth prior to moving on to the nodes at the next depth level.",
            steps = listOf(
                "Start by putting the selected starting node into a Queue.",
                "If the Queue is not empty, evaluate the first node.",
                "Put all of the node's unvisited children directly into the Queue.",
                "Mark the node as searched.",
                "Repeat until the Queue is empty."
            ),
            timeComplexity = "O(V + E) where V is vertices and E is edges",
            spaceComplexity = "O(V) for the Queue",
            useCases = "Finding the shortest path in unweighted networks, calculating peer-to-peer distances in social networks, and web crawlers.",
            youtubeVideoId = "pcKY4hjDrxk" // Bro Code BFS
        ),
        "dfs" to AlgorithmInfo(
            title = "Depth-First Search (DFS)",
            description = "DFS is an algorithm for traversing trees or graphs. The algorithm starts at the root node and explores as far as possible along each branch before backtracking.",
            steps = listOf(
                "Start by putting the selected starting node into a Stack (or using the Recursive call stack).",
                "If the Stack is not empty, pop the top node.",
                "Push all of the node's unvisited children into the Stack.",
                "Mark the node as searched and repeat."
            ),
            timeComplexity = "O(V + E)",
            spaceComplexity = "O(V) for the Stack / Memory Recursion",
            useCases = "Finding paths in mazes, topological sorting, and dependency resolutions (like analyzing package dependencies or build systems).",
            youtubeVideoId = "bIA8HEJVdBA" // Bro Code DFS
        ),
        "dijkstra" to AlgorithmInfo(
            title = "Dijkstra's Algorithm",
            description = "Dijkstra's essentially finds the shortest path between nodes in a graph, which may represent road networks. It maintains a set of unvisited nodes and iteratively builds the shortest path to each node using a Min-Priority Queue to evaluate the closest unknown node first.",
            steps = listOf(
                "Assign to every node a tentative distance value: set it to zero for our initial node and to infinity for all other nodes.",
                "Set the initial node as current. Mark all other nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.",
                "For the current node, consider all of its unvisited neighbors and calculate their tentative distances through the current node. Compare the newly calculated tentative distance to the current assigned value and assign the smaller one.",
                "When we are done considering all of the unvisited neighbors of the current node, mark the current node as visited and remove it from the unvisited set.",
                "Extract the unvisited node with the smallest tentative distance, set it as the new 'current node', and go back to step 3."
            ),
            timeComplexity = "O((V + E) log V)",
            spaceComplexity = "O(V)",
            useCases = "Google Maps route finding, IP routing protocols (OSPF), and calculating the mathematical optimal path across a complex network with weighted edges.",
            youtubeVideoId = "XB4MIexjvY0" // Abdul Bari Dijkstra
        ),
        "bst_insert" to AlgorithmInfo(
            title = "Binary Search Tree Insertion",
            description = "A Binary Search Tree (BST) is a rooted binary tree whose internal nodes each store a key greater than all the keys in the node's left subtree and less than those in its right subtree. Insertion requires maintaining this mathematical invariant.",
            steps = listOf(
                "Start at the root node.",
                "If the new key is less than the current node's key, go down the left sub-tree.",
                "If the new key is greater than the current node's key, go down the right sub-tree.",
                "When exactly an empty (null) spot is reached, construct a new node and insert it there."
            ),
            timeComplexity = "Worst: O(n) (unbalanced), Average: O(log n)",
            spaceComplexity = "O(1) Iterative, O(log n) Recursive",
            useCases = "Dynamic maps and sets where elements need to be perpetually added and immediately retrieved in a naturally sorted configuration without recreating massive contiguous arrays.",
            youtubeVideoId = "r_n3gDjc0Wc" // Abdul Bari BST
        )
    )
}
