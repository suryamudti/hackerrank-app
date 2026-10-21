package com.hackerrank.app.data.seed

import com.hackerrank.app.domain.model.CheatsheetCategory
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import com.hackerrank.app.domain.model.Difficulty

object CheatsheetSeedData {
    fun getComplexityRules(): List<ComplexityRule> =
        listOf(
            ComplexityRule(
                constraint = "N <= 10",
                maxTimeComplexity = "O(N!) or O(N^2 * 2^N)",
                algorithms = "Brute force backtracking, Permutations, TSP",
            ),
            ComplexityRule(
                constraint = "N <= 20",
                maxTimeComplexity = "O(2^N)",
                algorithms = "Subsets generation, Bitmask DP, Meet-in-the-middle",
            ),
            ComplexityRule(
                constraint = "N <= 100",
                maxTimeComplexity = "O(N^3)",
                algorithms = "Floyd-Warshall, 3 nested loops, Matrix multiplication",
            ),
            ComplexityRule(
                constraint = "N <= 1,000",
                maxTimeComplexity = "O(N^2)",
                algorithms = "All-pairs comparison, 2D DP table, Bubble / Selection sort",
            ),
            ComplexityRule(
                constraint = "N <= 100,000",
                maxTimeComplexity = "O(N log N) or O(N)",
                algorithms = "Merge/Quick Sort, Binary Search, Sliding Window, Heap, Monotonic Stack",
            ),
            ComplexityRule(
                constraint = "N <= 1,000,000",
                maxTimeComplexity = "O(N)",
                algorithms = "Single pass scan, Prefix sum, Two pointers, Hash map, Hash set",
            ),
            ComplexityRule(
                constraint = "N >= 1,000,000,000",
                maxTimeComplexity = "O(log N) or O(1)",
                algorithms = "Binary Search on answer range, GCD / Euclidean, Math formulas, Bit tricks",
            ),
        )

    fun getCheatsheets(): List<CheatsheetItem> =
        listOf(
            CheatsheetItem(
                id = "cs_two_pointers",
                title = "Two Pointers Technique",
                category = CheatsheetCategory.TWO_POINTERS_SLIDING_WINDOW,
                difficulty = Difficulty.EASY,
                summary = "Use two pointers moving in opposite directions or at differing speeds to avoid nested loops on linear structures.",
                whenToUse =
                    listOf(
                        "Array or string is sorted (or sorting beforehand is acceptable)",
                        "Finding a pair or triplet summing to a target (Two Sum II, 3Sum)",
                        "Checking palindromes (comparing start and end characters)",
                        "In-place compaction or element removal (Move Zeroes, Remove Duplicates)",
                    ),
                keyTricks =
                    listOf(
                        "Sort the array first if element order does not affect the answer",
                        "Skip identical elements after finding a match to avoid duplicate solutions",
                        "Keep bounds tight: usually 'while (left < right)' for opposite ends",
                    ),
                codeTemplate =
                    """fun twoPointersOpposite(nums: IntArray, target: Int): IntArray {
    var left = 0
    var right = nums.size - 1
    while (left < right) {
        val sum = nums[left] + nums[right]
        when {
            sum == target -> return intArrayOf(left, right)
            sum < target -> left++
            else -> right--
        }
    }
    return intArrayOf(-1, -1)
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p001", "p006", "p007", "p015"),
            ),
            CheatsheetItem(
                id = "cs_sliding_window",
                title = "Sliding Window Pattern",
                category = CheatsheetCategory.TWO_POINTERS_SLIDING_WINDOW,
                difficulty = Difficulty.MEDIUM,
                summary = "Maintain a dynamic continuous subsegment [left, right] to evaluate range properties in a single linear pass.",
                whenToUse =
                    listOf(
                        "Contiguous subarray or substring optimization problems",
                        "Longest substring without repeating characters / with at most K distinct",
                        "Minimum length subarray with sum >= target",
                        "Fixed window: maximum sum of any subarray of size K",
                    ),
                keyTricks =
                    listOf(
                        "Expand 'right' pointer to include elements until window constraint is violated",
                        "Shrink 'left' pointer while window is invalid to restore valid state",
                        "Use IntArray(128) instead of HashMap for ASCII character frequency counts",
                        "Window length is always computed as: (right - left + 1)",
                    ),
                codeTemplate =
                    """fun lengthOfLongestSubstring(s: String): Int {
    val charIndex = IntArray(128) { -1 }
    var maxLen = 0
    var left = 0
    for (right in s.indices) {
        val c = s[right].code
        if (charIndex[c] >= left) {
            left = charIndex[c] + 1
        }
        charIndex[c] = right
        maxLen = maxOf(maxLen, right - left + 1)
    }
    return maxLen
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(min(N, M))",
                relatedProblemIds = listOf("p002", "p016", "p077"),
            ),
            CheatsheetItem(
                id = "cs_binary_search",
                title = "Binary Search & Search on Answer",
                category = CheatsheetCategory.BINARY_SEARCH,
                difficulty = Difficulty.MEDIUM,
                summary = "Repeatedly halve a search space on sorted inputs or on any monotonic predicate condition isPossible(x).",
                whenToUse =
                    listOf(
                        "Array is sorted or rotated sorted (Search in Rotated Sorted Array)",
                        "Finding insertion position (Search Insert Position)",
                        "Minimax optimization ('minimize the maximum capacity', 'split array largest sum')",
                        "Search space is bounded between [low, high] and feasibility is monotonic",
                    ),
                keyTricks =
                    listOf(
                        "Calculate midpoint safely: 'val mid = left + (right - left) / 2' to avoid integer overflow",
                        "Search on answer: if isPossible(mid) is true, try smaller/larger based on min/max goal",
                        "Invariant: when loop terminates with 'left <= right', left points to first valid insertion point",
                    ),
                codeTemplate =
                    """fun binarySearch(nums: IntArray, target: Int): Int {
    var left = 0
    var right = nums.size - 1
    while (left <= right) {
        val mid = left + (right - left) / 2
        when {
            nums[mid] == target -> return mid
            nums[mid] < target -> left = mid + 1
            else -> right = mid - 1
        }
    }
    return -1
}""",
                timeComplexity = "O(log N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p011", "p069", "p070", "p073", "p074"),
            ),
            CheatsheetItem(
                id = "cs_fast_slow_pointers",
                title = "Fast & Slow Pointers (Floyd's Cycle)",
                category = CheatsheetCategory.TWO_POINTERS_SLIDING_WINDOW,
                difficulty = Difficulty.EASY,
                summary = "Advance pointers at different speeds (1 step vs 2 steps) to detect loops or locate the middle of a linked structure.",
                whenToUse =
                    listOf(
                        "Detecting cycles in a linked list or finite state sequence",
                        "Finding the entry point of a loop in linked list",
                        "Locating the middle node of a linked list in a single pass",
                        "Happy Number verification (detecting endless loop)",
                    ),
                keyTricks =
                    listOf(
                        "Always guard fast pointer with: 'while (fast != null && fast.next != null)'",
                        "To find cycle start: after collision, move one pointer to head; advance both at speed 1 until they meet",
                        "Middle of list: slow will be at exact midpoint when fast reaches end",
                    ),
                codeTemplate =
                    """fun hasCycle(head: ListNode?): Boolean {
    var slow = head
    var fast = head
    while (fast != null && fast.next != null) {
        slow = slow?.next
        fast = fast.next?.next
        if (slow == fast) return true
    }
    return false
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p026", "p029", "p080"),
            ),
            CheatsheetItem(
                id = "cs_monotonic_stack",
                title = "Monotonic Stack Pattern",
                category = CheatsheetCategory.STACKS_QUEUES,
                difficulty = Difficulty.MEDIUM,
                summary = "Keep elements ordered strictly increasing or decreasing to resolve nearest greater/smaller element queries in O(N).",
                whenToUse =
                    listOf(
                        "Next Greater Element / Previous Greater Element",
                        "Daily Temperatures (days to wait for warmer weather)",
                        "Largest Rectangle in Histogram",
                        "Trapping Rain Water",
                    ),
                keyTricks =
                    listOf(
                        "Store indices in stack rather than values: enables calculating distance (i - stack.peek())",
                        "For Next Greater: maintain a decreasing stack; pop while top < current value",
                        "Every element pushed and popped at most once -> guarantees strict O(N) runtime",
                    ),
                codeTemplate =
                    """fun dailyTemperatures(temps: IntArray): IntArray {
    val result = IntArray(temps.size)
    val stack = ArrayDeque<Int>() // stores indices
    for (i in temps.indices) {
        while (stack.isNotEmpty() && temps[i] > temps[stack.last()]) {
            val prevIndex = stack.removeLast()
            result[prevIndex] = i - prevIndex
        }
        stack.addLast(i)
    }
    return result
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p033", "p034", "p037", "p039"),
            ),
            CheatsheetItem(
                id = "cs_prefix_sum_hashmap",
                title = "Prefix Sum + Hash Map",
                category = CheatsheetCategory.ARRAYS_STRINGS,
                difficulty = Difficulty.MEDIUM,
                summary = "Store running cumulative sums in a Map to reduce range-sum and subarray constraints from O(N^2) to O(N).",
                whenToUse =
                    listOf(
                        "Subarray Sum Equals K (with positive and negative integers)",
                        "Contiguous array with equal number of 0s and 1s",
                        "Subarray sums divisible by K",
                        "Continuous range sum queries",
                    ),
                keyTricks =
                    listOf(
                        "Always prime the map: 'map[0] = 1' (for count) or 'map[0] = -1' (for index distance)",
                        "Equation: if prefixSum[j] - prefixSum[i] = K, then prefixSum[i] = prefixSum[j] - K",
                        "Replace 0 with -1 when counting equal occurrences of two elements",
                    ),
                codeTemplate =
                    """fun subarraySum(nums: IntArray, k: Int): Int {
    var count = 0
    var currentSum = 0
    val prefixMap = mutableMapOf(0 to 1) // sum -> frequency
    for (num in nums) {
        currentSum += num
        count += prefixMap.getOrDefault(currentSum - k, 0)
        prefixMap[currentSum] = prefixMap.getOrDefault(currentSum, 0) + 1
    }
    return count
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p001", "p002", "p005"),
            ),
            CheatsheetItem(
                id = "cs_bfs_shortest_path",
                title = "Breadth-First Search (BFS) for Shortest Path",
                category = CheatsheetCategory.TREES_GRAPHS,
                difficulty = Difficulty.MEDIUM,
                summary = "Explore level-by-level using a queue to guarantee the minimum number of steps or edges in unweighted graphs.",
                whenToUse =
                    listOf(
                        "Shortest path in unweighted graph or grid (e.g. Word Ladder, Maze Runner)",
                        "Level-order tree traversal",
                        "Connected components & flood fill (Number of Islands, Rotting Oranges)",
                        "Multi-source BFS (nearest 0 in binary matrix)",
                    ),
                keyTricks =
                    listOf(
                        "Mark visited IMMEDIATELY when pushing to queue, never when popping, to prevent exponential duplicate pushes",
                        "Snap current level size 'val levelSize = queue.size' before looping each step",
                        "Use 4-directional delta array: 'val dirs = arrayOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)'",
                    ),
                codeTemplate =
                    """fun numIslands(grid: Array<CharArray>): Int {
    if (grid.isEmpty()) return 0
    var islands = 0
    val rows = grid.size
    val cols = grid[0].size
    val dirs = arrayOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (grid[r][c] == '1') {
                islands++
                grid[r][c] = '0'
                val queue = ArrayDeque<Pair<Int, Int>>()
                queue.addLast(r to c)
                while (queue.isNotEmpty()) {
                    val (cr, cc) = queue.removeFirst()
                    for ((dr, dc) in dirs) {
                        val nr = cr + dr
                        val nc = cc + dc
                        if (nr in 0 until rows && nc in 0 until cols && grid[nr][nc] == '1') {
                            grid[nr][nc] = '0'
                            queue.addLast(nr to nc)
                        }
                    }
                }
            }
        }
    }
    return islands
}""",
                timeComplexity = "O(V + E) or O(R * C)",
                spaceComplexity = "O(V) or O(min(R, C))",
                relatedProblemIds = listOf("p046", "p052", "p055", "p057"),
            ),
            CheatsheetItem(
                id = "cs_dfs_backtracking",
                title = "DFS & Backtracking Template",
                category = CheatsheetCategory.BACKTRACKING_GREEDY,
                difficulty = Difficulty.HARD,
                summary = "Systematically explore all decision paths recursively and undo state mutations upon returning.",
                whenToUse =
                    listOf(
                        "Generating all Subsets, Combinations, or Permutations",
                        "Word Search in grid",
                        "Combination Sum",
                        "Constraint satisfaction: N-Queens, Sudoku Solver",
                    ),
                keyTricks =
                    listOf(
                        "Follow standard recipe: 1. Base check -> 2. Candidate loop -> 3. Choose -> 4. Recurse -> 5. Un-choose",
                        "To handle duplicates: sort input array and skip with 'if (i > start && nums[i] == nums[i-1]) continue'",
                        "Remember to make a copy when adding to result: 'result.add(ArrayList(current))'",
                    ),
                codeTemplate =
                    """fun subsets(nums: IntArray): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    fun backtrack(start: Int, current: MutableList<Int>) {
        result.add(ArrayList(current))
        for (i in start until nums.size) {
            current.add(nums[i])      // choose
            backtrack(i + 1, current) // explore
            current.removeAt(current.lastIndex) // un-choose
        }
    }
    backtrack(0, mutableListOf())
    return result
}""",
                timeComplexity = "O(2^N * N)",
                spaceComplexity = "O(N) recursion stack",
                relatedProblemIds = listOf("p054", "p084", "p085", "p086", "p087"),
            ),
            CheatsheetItem(
                id = "cs_dynamic_programming_knapsack",
                title = "0/1 Knapsack & 1D State Compression",
                category = CheatsheetCategory.DYNAMIC_PROGRAMMING,
                difficulty = Difficulty.MEDIUM,
                summary = "Solve optimization problems with overlapping subproblems, compressing 2D tables into 1D space.",
                whenToUse =
                    listOf(
                        "0/1 Knapsack (item can be picked at most once)",
                        "Partition Equal Subset Sum",
                        "Target Sum",
                        "Coin Change (unbounded variant)",
                    ),
                keyTricks =
                    listOf(
                        "For 0/1 knapsack: traverse capacity backwards 'downTo weight' to ensure each item is picked at most once",
                        "For unbounded knapsack (reusable items): traverse capacity forward 'until W'",
                        "Base case: dp[0] = 0 or dp[0] = 1 depending on whether counting ways or optimizing value",
                    ),
                codeTemplate =
                    """fun canPartition(nums: IntArray): Boolean {
    val totalSum = nums.sum()
    if (totalSum % 2 != 0) return false
    val target = totalSum / 2
    val dp = BooleanArray(target + 1)
    dp[0] = true

    for (num in nums) {
        for (w in target downTo num) {
            dp[w] = dp[w] || dp[w - num]
        }
    }
    return dp[target]
}""",
                timeComplexity = "O(N * Target)",
                spaceComplexity = "O(Target)",
                relatedProblemIds = listOf("p061", "p064", "p066", "p067"),
            ),
            CheatsheetItem(
                id = "cs_top_k_heap",
                title = "Top-K Elements with PriorityQueue (Heap)",
                category = CheatsheetCategory.STACKS_QUEUES,
                difficulty = Difficulty.MEDIUM,
                summary = "Maintain a bounded Min-Heap of size K to find top K elements in O(N log K) without full array sorting.",
                whenToUse =
                    listOf(
                        "Kth Largest Element in an Array / Stream",
                        "Top K Frequent Elements / Words",
                        "Find K Closest Points to Origin",
                        "Merge K Sorted Lists",
                    ),
                keyTricks =
                    listOf(
                        "To find K largest elements, use a MIN-HEAP: push item, if size > K, remove smallest",
                        "To find K smallest elements, use a MAX-HEAP: if size > K, remove largest",
                        "Kotlin's PriorityQueue<T>() is Min-Heap by default; use reverseOrder() for Max-Heap",
                    ),
                codeTemplate =
                    """fun findKthLargest(nums: IntArray, k: Int): Int {
    val minHeap = java.util.PriorityQueue<Int>()
    for (num in nums) {
        minHeap.offer(num)
        if (minHeap.size > k) {
            minHeap.poll()
        }
    }
    return minHeap.peek()
}""",
                timeComplexity = "O(N log K)",
                spaceComplexity = "O(K)",
                relatedProblemIds = listOf("p027", "p035"),
            ),
            CheatsheetItem(
                id = "cs_bit_manipulation",
                title = "Bit Manipulation Hacks",
                category = CheatsheetCategory.BIT_MATH,
                difficulty = Difficulty.EASY,
                summary = "Manipulate integers at the binary bit level for lightning-fast O(1) checks and operations.",
                whenToUse =
                    listOf(
                        "Single Number (all elements appear twice except one)",
                        "Counting 1-bits (Hamming Weight)",
                        "Power of Two check",
                        "Bitmask representing subsets of up to 30 elements",
                    ),
                keyTricks =
                    listOf(
                        "'n and (n - 1)' clears the lowest set bit (Brian Kernighan algorithm)",
                        "'n and (n - 1) == 0 && n > 0' checks if n is a power of 2",
                        "'x xor x = 0' and 'x xor 0 = x' eliminates duplicate pairs in O(1) space",
                        "'1 shl i' checks or sets the i-th bit",
                    ),
                codeTemplate =
                    """fun singleNumber(nums: IntArray): Int {
    var result = 0
    for (num in nums) {
        result = result xor num
    }
    return result
}

fun countBits(n: Int): Int {
    var count = 0
    var temp = n
    while (temp != 0) {
        temp = temp and (temp - 1) // clears lowest set bit
        count++
    }
    return count
}""",
                timeComplexity = "O(1) to O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p079", "p091", "p092", "p093", "p095"),
            ),
            CheatsheetItem(
                id = "cs_interval_merging",
                title = "Interval Scheduling & Merging",
                category = CheatsheetCategory.ARRAYS_STRINGS,
                difficulty = Difficulty.MEDIUM,
                summary = "Sort intervals by start (or end) time to collapse overlapping intervals and find scheduling conflicts.",
                whenToUse =
                    listOf(
                        "Merge Overlapping Intervals",
                        "Insert Interval into non-overlapping list",
                        "Meeting Rooms / Minimum Conference Rooms required",
                        "Non-overlapping Intervals count",
                    ),
                keyTricks =
                    listOf(
                        "Sort intervals by start time: 'intervals.sortBy { it[0] }'",
                        "Two intervals [a, b] and [c, d] overlap if and only if c <= b",
                        "Merged range is [a, maxOf(b, d)]",
                    ),
                codeTemplate =
                    """fun mergeIntervals(intervals: Array<IntArray>): Array<IntArray> {
    if (intervals.isEmpty()) return emptyArray()
    intervals.sortBy { it[0] }
    val result = mutableListOf<IntArray>()
    var current = intervals[0]

    for (i in 1 until intervals.size) {
        val next = intervals[i]
        if (next[0] <= current[1]) {
            current[1] = maxOf(current[1], next[1])
        } else {
            result.add(current)
            current = next
        }
    }
    result.add(current)
    return result.toTypedArray()
}""",
                timeComplexity = "O(N log N)",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p075"),
            ),
            CheatsheetItem(
                id = "cs_topological_sort",
                title = "Topological Sort & Cycle Detection (Kahn's)",
                category = CheatsheetCategory.TREES_GRAPHS,
                difficulty = Difficulty.MEDIUM,
                summary = "Compute vertex in-degrees and peel away 0-in-degree nodes via BFS to detect directed cycles and schedule dependencies.",
                whenToUse =
                    listOf(
                        "Course Schedule (prerequisite dependencies)",
                        "Build systems / compilation dependency ordering",
                        "Detecting directed graph cycles",
                        "Alien Dictionary language ordering",
                    ),
                keyTricks =
                    listOf(
                        "Count processed nodes; if processedCount != numCourses, graph contains a directed cycle!",
                        "Array of in-degrees: 'val inDegree = IntArray(n)'",
                        "Enqueue all nodes with inDegree == 0 initially",
                    ),
                codeTemplate =
                    """fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    val inDegree = IntArray(numCourses)
    val adj = Array(numCourses) { mutableListOf<Int>() }
    for (p in prerequisites) {
        adj[p[1]].add(p[0])
        inDegree[p[0]]++
    }

    val queue = ArrayDeque<Int>()
    for (i in 0 until numCourses) {
        if (inDegree[i] == 0) queue.addLast(i)
    }

    var count = 0
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        count++
        for (neighbor in adj[node]) {
            if (--inDegree[neighbor] == 0) {
                queue.addLast(neighbor)
            }
        }
    }
    return count == numCourses
}""",
                timeComplexity = "O(V + E)",
                spaceComplexity = "O(V + E)",
                relatedProblemIds = listOf("p053", "p056", "p058"),
            ),
            CheatsheetItem(
                id = "cs_tree_lca_traversal",
                title = "Tree Traversal & Lowest Common Ancestor (LCA)",
                category = CheatsheetCategory.TREES_GRAPHS,
                difficulty = Difficulty.MEDIUM,
                summary = "Synthesize information from left and right subtrees recursively to resolve ancestor queries and path metrics.",
                whenToUse =
                    listOf(
                        "Lowest Common Ancestor in Binary Tree or BST",
                        "Binary Tree Maximum Path Sum",
                        "Diameter of Binary Tree",
                        "Validate Binary Search Tree",
                    ),
                keyTricks =
                    listOf(
                        "In BST: if both targets < root.val go left; if both > root.val go right; else current root is LCA!",
                        "In general binary tree: if left != null && right != null, root is LCA; otherwise return whichever is non-null",
                        "Validate BST: always carry valid range (minVal, maxVal) down the recursion",
                    ),
                codeTemplate =
                    """fun lowestCommonAncestor(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode? {
    if (root == null || root == p || root == q) return root
    val left = lowestCommonAncestor(root.left, p, q)
    val right = lowestCommonAncestor(root.right, p, q)
    return when {
        left != null && right != null -> root
        left != null -> left
        else -> right
    }
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(H) recursion height",
                relatedProblemIds = listOf("p041", "p042", "p043", "p048", "p049", "p050"),
            ),
            CheatsheetItem(
                id = "cs_dutch_national_flag",
                title = "Dutch National Flag (3-Way Partitioning)",
                category = CheatsheetCategory.TWO_POINTERS_SLIDING_WINDOW,
                difficulty = Difficulty.MEDIUM,
                summary = "Partition an array into three distinct partitions (e.g. 0, 1, 2) in-place in a single pass using three pointers.",
                whenToUse =
                    listOf(
                        "Sort Colors (sorting 0s, 1s, 2s)",
                        "Quickselect / Quicksort with duplicate elements",
                        "Partitioning array around a pivot value into (< pivot, == pivot, > pivot)",
                    ),
                keyTricks =
                    listOf(
                        "Three pointers: low, mid, high",
                        "When swapping mid with low: increment both low++ and mid++",
                        "When swapping mid with high: ONLY decrement high--; do NOT increment mid because swapped element needs evaluation",
                    ),
                codeTemplate =
                    """fun sortColors(nums: IntArray) {
    var low = 0
    var mid = 0
    var high = nums.size - 1
    while (mid <= high) {
        when (nums[mid]) {
            0 -> {
                nums[low] = nums[mid].also { nums[mid] = nums[low] }
                low++
                mid++
            }
            1 -> mid++
            else -> {
                nums[high] = nums[mid].also { nums[mid] = nums[high] }
                high--
            }
        }
    }
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p008", "p076"),
            ),
            CheatsheetItem(
                id = "cs_dp_2d_subsequences",
                title = "2D DP - Subsequences & String Alignment",
                category = CheatsheetCategory.DYNAMIC_PROGRAMMING,
                difficulty = Difficulty.HARD,
                summary = "Compare prefixes of two strings using a 2D grid dp[i][j] representing the optimal solution for s1[0 until i] and s2[0 until j].",
                whenToUse =
                    listOf(
                        "Longest Common Subsequence (LCS)",
                        "Edit Distance (Levenshtein Distance)",
                        "Distinct Subsequences",
                        "Wildcard & Regular Expression Matching",
                    ),
                keyTricks =
                    listOf(
                        "Pad table to dp[m + 1][n + 1] where index 0 represents empty string prefix",
                        "If characters match: dp[i][j] = 1 + dp[i - 1][j - 1]",
                        "If mismatch: dp[i][j] = maxOf(dp[i - 1][j], dp[i][j - 1]) (for LCS) or minOf(insert, delete, replace) + 1 (for Edit Distance)",
                    ),
                codeTemplate =
                    """fun longestCommonSubsequence(text1: String, text2: String): Int {
    val m = text1.length
    val n = text2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 1..m) {
        for (j in 1..n) {
            dp[i][j] = if (text1[i - 1] == text2[j - 1]) {
                dp[i - 1][j - 1] + 1
            } else {
                maxOf(dp[i - 1][j], dp[i][j - 1])
            }
        }
    }
    return dp[m][n]
}""",
                timeComplexity = "O(M * N)",
                spaceComplexity = "O(M * N) or O(min(M, N))",
                relatedProblemIds = listOf("p062", "p063", "p065"),
            ),
        )
}
