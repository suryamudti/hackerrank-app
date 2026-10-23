package com.hackerrank.app.data.seed

import com.hackerrank.app.domain.model.CategoryGuide
import com.hackerrank.app.domain.model.CheatsheetItem
import com.hackerrank.app.domain.model.ComplexityRule
import com.hackerrank.app.domain.model.Difficulty
import com.hackerrank.app.domain.model.ProblemCategory

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

    fun getCategoryGuides(): Map<ProblemCategory, CategoryGuide> =
        mapOf(
            ProblemCategory.ARRAYS to
                CategoryGuide(
                    category = ProblemCategory.ARRAYS,
                    title = "Arrays Cheatsheet Guide",
                    coreConcept = "Contiguous memory layout provides O(1) index access. Leverage pointers, prefix accumulation, and in-place swapping to avoid O(N) auxiliary space.",
                    timeSpaceTargets = "Time: O(N) single pass or O(N log N) with sorting. Space: O(1) in-place or O(N) for prefix sums.",
                    essentialKotlinTools =
                        listOf(
                            "IntArray / LongArray (avoids auto-boxing overhead)",
                            "nums.indices / nums.withIndex()",
                            "nums.sort() / nums.sortDescending()",
                            "java.util.Arrays.binarySearch(nums, target)",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Empty array or single element array",
                            "All duplicate elements or all negative numbers",
                            "Integer overflow during sum accumulation (prefer Long)",
                            "Modifying array indices during iteration",
                        ),
                    topPatterns =
                        listOf(
                            "Two Pointers (converging & same direction)",
                            "Prefix Sum & Difference Array",
                            "Kadane's Algorithm for max contiguous subarray",
                            "In-Place Array State Negation (values in [1..N])",
                        ),
                ),
            ProblemCategory.STRINGS to
                CategoryGuide(
                    category = ProblemCategory.STRINGS,
                    title = "Strings Cheatsheet Guide",
                    coreConcept = "Strings are immutable sequences of characters. Avoid repeated concatenation (+) which creates O(N^2) memory churn; use StringBuilder or char arrays.",
                    timeSpaceTargets = "Time: O(N) with frequency counters or sliding window. Space: O(1) auxiliary if alphabet is ASCII (26 or 128 elements).",
                    essentialKotlinTools =
                        listOf(
                            "StringBuilder(capacity) for mutable construction",
                            "IntArray(26) { 0 } for lowercase frequency tracking",
                            "s.toCharArray() for in-place index manipulation",
                            "Char.isLetterOrDigit() / Char.lowercaseChar()",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Empty string \"\" or single character",
                            "Case sensitivity and non-alphanumeric punctuation",
                            "Even vs odd palindrome centers",
                            "String index out of bounds on s.substring(start, end)",
                        ),
                    topPatterns =
                        listOf(
                            "Sliding Window (Variable & Fixed size)",
                            "Expand Around Center for Palindromes",
                            "Character Frequency Counter / Anagram Matching",
                            "Prefix Match & Trie structure",
                        ),
                ),
            ProblemCategory.LINKED_LISTS to
                CategoryGuide(
                    category = ProblemCategory.LINKED_LISTS,
                    title = "Linked Lists Cheatsheet Guide",
                    coreConcept = "Non-contiguous nodes connected via pointers. Always maintain a dummy head node to eliminate head deletion/insertion edge cases.",
                    timeSpaceTargets = "Time: O(N) single or double pass. Space: O(1) auxiliary space (pointer rewiring in-place).",
                    essentialKotlinTools =
                        listOf(
                            "Dummy head idiom: val dummy = ListNode(0).apply { next = head }",
                            "Three-pointer reversal: prev, curr, next",
                            "Two-pointer runner: slow = slow?.next, fast = fast.next?.next",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Null head or single-node list",
                            "Cycles causing infinite traversal loops",
                            "Losing pointer references before reassignment",
                            "Failing to terminate the tail pointer with null",
                        ),
                    topPatterns =
                        listOf(
                            "Dummy Head Insertion & Merging",
                            "Fast & Slow Pointers (Floyd's Tortoise & Hare)",
                            "In-Place Iterative Reversal",
                            "Split in Halves for Merge Sort or Palindrome Check",
                        ),
                ),
            ProblemCategory.STACKS_QUEUES to
                CategoryGuide(
                    category = ProblemCategory.STACKS_QUEUES,
                    title = "Stacks & Queues Cheatsheet Guide",
                    coreConcept = "LIFO (Stack) for nested, recursive, or previous-greater structures; FIFO (Queue) for level-order and sequential processing.",
                    timeSpaceTargets = "Time: O(1) push, pop, peek; O(N) amortized across all elements. Space: O(N) auxiliary.",
                    essentialKotlinTools =
                        listOf(
                            "ArrayDeque<T>() (fastest for both Stack and Queue)",
                            "stack.addLast(x) / stack.removeLast() (LIFO stack)",
                            "queue.addLast(x) / queue.removeFirst() (FIFO queue)",
                            "stack.lastOrNull() / queue.firstOrNull()",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Popping from an empty stack/queue",
                            "Unmatched closing brackets without prior opening brackets",
                            "Storing values vs storing indices in monotonic stacks",
                            "Queue size changes dynamically during BFS without level snapshot",
                        ),
                    topPatterns =
                        listOf(
                            "Monotonic Stack (Next Greater / Smaller Element)",
                            "Parentheses & Balanced Bracket Matching",
                            "Expression Evaluation & RPN",
                            "Monotonic Deque for Sliding Window Maximum",
                        ),
                ),
            ProblemCategory.TREES to
                CategoryGuide(
                    category = ProblemCategory.TREES,
                    title = "Trees Cheatsheet Guide",
                    coreConcept = "Connected acyclic hierarchical graph. Recursive DFS handles subtree results bottom-up; BFS handles level-by-level relationships.",
                    timeSpaceTargets = "Time: O(N) visiting each node once. Space: O(H) recursion stack where H is tree height (log N balanced, N skewed).",
                    essentialKotlinTools =
                        listOf(
                            "Recursive DFS with null base check: if (node == null) return ...",
                            "Level-order BFS: snapshot queue size: val size = queue.size",
                            "BST In-order Traversal yields strictly sorted values",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Null root node",
                            "Skewed degenerate tree (like a linked list) causing stack overflow",
                            "BST validity with node value equal to Long.MIN_VALUE or Long.MAX_VALUE",
                            "Duplicate node values in BST definition",
                        ),
                    topPatterns =
                        listOf(
                            "Depth-First Search (Pre-order, In-order, Post-order)",
                            "Breadth-First Search (Level-Order Snapshot)",
                            "Lowest Common Ancestor (Binary Tree & BST)",
                            "Subtree Property Aggregation (Height, Diameter, Path Sum)",
                        ),
                ),
            ProblemCategory.GRAPHS to
                CategoryGuide(
                    category = ProblemCategory.GRAPHS,
                    title = "Graphs Cheatsheet Guide",
                    coreConcept = "Vertices connected by edges, which can be directed/undirected and cyclic/acyclic. Always guard traversals with visited tracking.",
                    timeSpaceTargets = "Time: O(V + E) for BFS/DFS; O(E log V) for Dijkstra; O(alpha(V)) for Union-Find. Space: O(V + E) adjacency list.",
                    essentialKotlinTools =
                        listOf(
                            "Adjacency list: Array(n) { mutableListOf<Int>() }",
                            "BooleanArray(n) or HashSet<Int> for visited tracking",
                            "IntArray(n) for in-degree counting in Kahn's algorithm",
                            "PriorityQueue<Pair<Int, Int>> for Dijkstra's shortest path",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Disconnected components / isolated nodes",
                            "Cycles in directed graphs (detect via 3-color DFS or in-degree)",
                            "Marking visited on pop instead of on push (causes queue explosion)",
                            "Self-loops and multiple edges between identical nodes",
                        ),
                    topPatterns =
                        listOf(
                            "BFS Shortest Path in Unweighted Graphs",
                            "Topological Sort (Kahn's BFS In-Degree)",
                            "Disjoint Set Union (Union-Find with Path Compression)",
                            "Dijkstra's Algorithm for Weighted Graphs",
                        ),
                ),
            ProblemCategory.DYNAMIC_PROGRAMMING to
                CategoryGuide(
                    category = ProblemCategory.DYNAMIC_PROGRAMMING,
                    title = "Dynamic Programming Cheatsheet Guide",
                    coreConcept = "Solve complex problems by breaking them into overlapping subproblems with optimal substructure. Store answers to avoid recomputation.",
                    timeSpaceTargets = "Time: O(states * transitions). Space: optimize 2D DP grids to 1D arrays when only previous row is needed.",
                    essentialKotlinTools =
                        listOf(
                            "1D IntArray(n + 1) { defaultValue }",
                            "2D Array(m + 1) { IntArray(n + 1) }",
                            "Rolling array / Two-variable optimization (prev, curr)",
                            "kotlin.math.max / kotlin.math.min",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Base cases: dp[0] = 0 or dp[0] = 1",
                            "Loop direction: 0/1 Knapsack MUST iterate backwards to prevent reuse",
                            "Unreachable states: initialize with infinity, avoid overflow when adding",
                            "Off-by-one indexing between 0-indexed strings and 1-indexed DP tables",
                        ),
                    topPatterns =
                        listOf(
                            "1D Linear / Knapsack (Bounded vs Unbounded)",
                            "2D Grid & Two Strings (LCS, Edit Distance)",
                            "Longest Increasing Subsequence (Patience Sorting O(N log N))",
                            "State Machine & Interval DP",
                        ),
                ),
            ProblemCategory.SORTING_SEARCHING to
                CategoryGuide(
                    category = ProblemCategory.SORTING_SEARCHING,
                    title = "Sorting & Searching Cheatsheet Guide",
                    coreConcept = "Monotonic properties allow binary search over indices or feasible answer spaces. Sorting transforms unstructured inputs into predictable order.",
                    timeSpaceTargets = "Time: O(log N) for binary search, O(N log N) for comparison sorts, O(N) expected for Quickselect. Space: O(1) auxiliary.",
                    essentialKotlinTools =
                        listOf(
                            "mid = low + (high - low) / 2 (avoids integer overflow)",
                            "intervals.sortWith(compareBy({ it[0] }, { it[1] }))",
                            "PriorityQueue<Int>() for K-th selection",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Target not found: handling boundary conditions correctly",
                            "Duplicates in rotated sorted array breaking O(log N) guarantee",
                            "Single element array or empty range [low > high]",
                            "Overlapping intervals with identical endpoints (start == end)",
                        ),
                    topPatterns =
                        listOf(
                            "Binary Search on Sorted Arrays (Lower & Upper Bounds)",
                            "Binary Search on Answer Space (Monotonic condition check)",
                            "Interval Merging & Sweep-Line",
                            "Quickselect for K-th Order Statistics",
                        ),
                ),
            ProblemCategory.HASH_BASED to
                CategoryGuide(
                    category = ProblemCategory.HASH_BASED,
                    title = "Hash-Based Cheatsheet Guide",
                    coreConcept = "Trade O(N) memory for O(1) amortized lookup and insertion. Store complements, running frequencies, or prefix states.",
                    timeSpaceTargets = "Time: O(1) average lookup/insert; O(N) overall pass. Space: O(N) unique elements.",
                    essentialKotlinTools =
                        listOf(
                            "mutableMapOf<K, V>() / HashMap<K, V>()",
                            "mutableSetOf<T>() / HashSet<T>()",
                            "map.getOrDefault(key, defaultValue)",
                            "map.computeIfAbsent(key) { mutableListOf() }",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Mutating keys after inserting into hash map (breaks hash code)",
                            "Prefix sum hash map forgetting map[0] = 1 initial state",
                            "Hash collision worst case O(N) if bad distribution",
                            "Memory overhead of boxed types vs primitive arrays",
                        ),
                    topPatterns =
                        listOf(
                            "Two Sum Complement Lookup in Single Pass",
                            "Prefix Sum with Frequency Map (Subarray Sum equals K)",
                            "Group Anagrams / Canonical Signature Hashing",
                            "Sliding Window with Hash Frequency Verification",
                        ),
                ),
            ProblemCategory.RECURSION_MATH to
                CategoryGuide(
                    category = ProblemCategory.RECURSION_MATH,
                    title = "Recursion & Math Cheatsheet Guide",
                    coreConcept = "Express solutions in terms of base case and inductive step. Mathematical invariants (GCD, modular arithmetic, parity) prune brute force.",
                    timeSpaceTargets = "Time: O(log N) for math/GCD; O(2^N) or O(N!) for combinatorial search. Space: O(recursion depth).",
                    essentialKotlinTools =
                        listOf(
                            "Euclidean GCD: tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)",
                            "Fast Power: binary exponentiation in O(log N)",
                            "Backtracking: choose -> explore -> unchoose",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Base case missing or unreachable causing StackOverflowError",
                            "Integer overflow with negative power n = Int.MIN_VALUE",
                            "Modulo arithmetic with negative values: (a % m + m) % m",
                            "Duplicate permutations when input contains duplicate elements",
                        ),
                    topPatterns =
                        listOf(
                            "Backtracking Permutations, Subsets & Combinations",
                            "Euclidean GCD & Modular Arithmetic",
                            "Fast Binary Exponentiation (Pow(x, n))",
                            "Sieve of Eratosthenes & Prime Factorization",
                        ),
                ),
            ProblemCategory.BIT_MANIPULATION to
                CategoryGuide(
                    category = ProblemCategory.BIT_MANIPULATION,
                    title = "Bit Manipulation Cheatsheet Guide",
                    coreConcept = "Operate directly on the binary representation of integers. Bitwise operations execute in a single CPU cycle with zero extra memory.",
                    timeSpaceTargets = "Time: O(1) operations; O(bits) loop (max 32 or 64). Space: O(1) strictly in-place.",
                    essentialKotlinTools =
                        listOf(
                            "Bitwise operators: and, or, xor, inv(), shl, shr, ushr",
                            "java.lang.Integer.bitCount(n) / n.countOneBits()",
                            "Lowest set bit isolation: x and -x",
                            "Remove lowest set bit: x and (x - 1)",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Operator precedence: (x and 1) == 0 (Kotlin 'and' has lower precedence than '==')",
                            "Signed 32-bit integer overflow when shifting >= 32 bits (use 1L shl i)",
                            "Negative numbers represented in two's complement",
                            "Bitwise right shift: 'shr' preserves sign, 'ushr' fills with zero",
                        ),
                    topPatterns =
                        listOf(
                            "XOR Invariants (x xor x = 0, x xor 0 = x)",
                            "Bitmask State Representation for Subsets (0 until (1 shl N))",
                            "Power of 2 Validation ((x > 0) && (x and (x - 1) == 0))",
                            "Brian Kernighan's Bit Counting Algorithm",
                        ),
                ),
            ProblemCategory.MISCELLANEOUS to
                CategoryGuide(
                    category = ProblemCategory.MISCELLANEOUS,
                    title = "Miscellaneous & Greedy Cheatsheet Guide",
                    coreConcept = "Greedy choice property: making locally optimal decisions leads to global optimum when problem exhibits optimal substructure and matroid properties.",
                    timeSpaceTargets = "Time: O(N) or O(N log N) after sorting. Space: O(1) to O(N) heap storage.",
                    essentialKotlinTools =
                        listOf(
                            "PriorityQueue<Int>() (Min-Heap by default)",
                            "PriorityQueue<Int>(compareByDescending { it }) (Max-Heap)",
                            "kotlin.math.max / kotlin.math.min",
                        ),
                    criticalEdgeCases =
                        listOf(
                            "Assuming greedy works without mathematical proof (often requires DP)",
                            "Two-heap imbalance when tracking running medians",
                            "Negative values or zeros in ratio-based greedy sorting",
                            "Floating-point precision issues when comparing fractions",
                        ),
                    topPatterns =
                        listOf(
                            "Greedy Interval Scheduling (Earliest finish time first)",
                            "Two Heaps Pattern (Running Median of Data Stream)",
                            "Boyer-Moore Majority Vote Algorithm (O(N) time, O(1) space)",
                            "Jump Game Reachability & Greedy Gas Station Tour",
                        ),
                ),
        )

    fun getCheatsheets(): List<CheatsheetItem> =
        listOf(
            // ═══════════════ 1. ARRAYS ═══════════════
            CheatsheetItem(
                id = "cs_arrays_two_pointers",
                title = "Two Pointers (Opposite & Same Direction)",
                category = ProblemCategory.ARRAYS,
                difficulty = Difficulty.EASY,
                summary = "Maintain two index pointers moving towards each other or in lockstep to avoid nested loops.",
                whenToUse =
                    listOf(
                        "Sorted array pair search (Two Sum II, 3Sum)",
                        "In-place filtering and element removal (Remove Duplicates)",
                        "Container with most water, trapping rainwater",
                    ),
                keyTricks =
                    listOf(
                        "If array is sorted and sum < target, increment left pointer; if sum > target, decrement right pointer",
                        "For deduplication in-place, maintain a write pointer 'k' and read pointer 'i'",
                        "Beware of infinite loops: ensure at least one pointer moves forward on every branch",
                    ),
                codeTemplate =
                    """fun twoSumSorted(nums: IntArray, target: Int): IntArray {
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
    return intArrayOf()
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p001", "p006", "p007", "p011"),
            ),
            CheatsheetItem(
                id = "cs_arrays_prefix_sum",
                title = "Prefix Sum & Difference Arrays",
                category = ProblemCategory.ARRAYS,
                difficulty = Difficulty.MEDIUM,
                summary = "Precompute cumulative sums so any contiguous subarray sum query [L, R] can be answered in O(1) time.",
                whenToUse =
                    listOf(
                        "Answering multiple range sum queries on static arrays",
                        "Subarray sum equals K problems (with HashMap)",
                        "Range updates on arrays with a difference array",
                    ),
                keyTricks =
                    listOf(
                        "Build prefix array of size N + 1 where prefix[0] = 0. Then sum[L..R] = prefix[R + 1] - prefix[L]",
                        "Use LongArray when sum of elements can exceed 2 * 10^9 to prevent integer overflow",
                        "For range increment [L, R] by V: diff[L] += V, diff[R + 1] -= V, then prefix sum",
                    ),
                codeTemplate =
                    """class PrefixSum(nums: IntArray) {
    private val prefix = LongArray(nums.size + 1)
    init {
        for (i in nums.indices) {
            prefix[i + 1] = prefix[i] + nums[i]
        }
    }
    fun query(l: Int, r: Int): Long = prefix[r + 1] - prefix[l]
}""",
                timeComplexity = "O(N) build, O(1) query",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p001", "p005"),
            ),
            CheatsheetItem(
                id = "cs_arrays_kadane",
                title = "Kadane's Algorithm (Max Subarray Sum)",
                category = ProblemCategory.ARRAYS,
                difficulty = Difficulty.MEDIUM,
                summary = "Single-pass algorithm to find the maximum sum contiguous subarray in linear time.",
                whenToUse =
                    listOf(
                        "Finding the maximum or minimum contiguous subarray sum",
                        "Circular subarray maximum sum (max(Kadane, total - minKadane))",
                        "Buy and sell stock single transaction variant",
                    ),
                keyTricks =
                    listOf(
                        "At each index i: currentSum = maxOf(nums[i], currentSum + nums[i])",
                        "Never initialize maxSum to 0 if all elements can be negative: initialize to nums[0]",
                        "Reset currentSum to nums[i] whenever extending prior subarray hurts the sum",
                    ),
                codeTemplate =
                    """fun maxSubArray(nums: IntArray): Int {
    var currentSum = nums[0]
    var maxSum = nums[0]
    for (i in 1 until nums.size) {
        currentSum = maxOf(nums[i], currentSum + nums[i])
        maxSum = maxOf(maxSum, currentSum)
    }
    return maxSum
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p002"),
            ),
            CheatsheetItem(
                id = "cs_arrays_in_place_cyclic",
                title = "In-Place Cyclic State & Rotation",
                category = ProblemCategory.ARRAYS,
                difficulty = Difficulty.MEDIUM,
                summary = "Use elements as indices or three reversals to achieve modifications with O(1) extra space.",
                whenToUse =
                    listOf(
                        "Elements are in range [1..N] and you need to find duplicates or missing numbers",
                        "Rotate array right by k steps in-place",
                        "Product of array except self without division",
                    ),
                keyTricks =
                    listOf(
                        "For values in [1..N], negate nums[abs(x) - 1] to mark visited; if already negative, x is duplicate",
                        "To rotate right by k: k = k % n; reverse(0, n-1); reverse(0, k-1); reverse(k, n-1)",
                        "Always take modulo k % n before rotating to avoid redundant cycles",
                    ),
                codeTemplate =
                    """fun rotate(nums: IntArray, k: Int) {
    val n = nums.size
    val shift = k % n
    fun rev(from: Int, to: Int) {
        var l = from; var r = to
        while (l < r) { val t = nums[l]; nums[l++] = nums[r]; nums[r--] = t }
    }
    rev(0, n - 1)
    rev(0, shift - 1)
    rev(shift, n - 1)
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p003", "p004", "p005"),
            ),
            // ═══════════════ 2. STRINGS ═══════════════
            CheatsheetItem(
                id = "cs_strings_sliding_window",
                title = "Sliding Window on Strings",
                category = ProblemCategory.STRINGS,
                difficulty = Difficulty.MEDIUM,
                summary = "Expand a right boundary to include characters, then shrink a left boundary once an invariant is violated.",
                whenToUse =
                    listOf(
                        "Longest substring without repeating characters",
                        "Find all anagrams of a pattern in a text",
                        "Minimum window substring containing all target characters",
                    ),
                keyTricks =
                    listOf(
                        "Use IntArray(128) or IntArray(26) instead of Map for frequency counter to eliminate object allocations",
                        "Maintain a 'matched' variable to avoid comparing full arrays on every shift",
                        "Expand right to find a valid window, contract left to find the optimal/minimal window",
                    ),
                codeTemplate =
                    """fun lengthOfLongestSubstring(s: String): Int {
    val lastSeen = IntArray(128) { -1 }
    var maxLen = 0
    var left = 0
    for (right in s.indices) {
        val charCode = s[right].code
        if (lastSeen[charCode] >= left) {
            left = lastSeen[charCode] + 1
        }
        lastSeen[charCode] = right
        maxLen = maxOf(maxLen, right - left + 1)
    }
    return maxLen
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1) (128 ASCII array)",
                relatedProblemIds = listOf("p013", "p016", "p017"),
            ),
            CheatsheetItem(
                id = "cs_strings_palindrome_expansion",
                title = "Palindrome Center Expansion",
                category = ProblemCategory.STRINGS,
                difficulty = Difficulty.MEDIUM,
                summary = "Expand outwards from all 2N - 1 potential centers (odd-length and even-length) in O(1) auxiliary space.",
                whenToUse =
                    listOf(
                        "Longest palindromic substring",
                        "Counting total palindromic substrings",
                        "Valid palindrome validation after removing at most 1 character",
                    ),
                keyTricks =
                    listOf(
                        "Every palindrome has a center: single char for odd lengths, pair of chars for even lengths",
                        "Expand while left >= 0 && right < s.length && s[left] == s[right]",
                        "Use subSequence or record (bestStart, bestLen) instead of creating temporary substrings during expansion",
                    ),
                codeTemplate =
                    """fun longestPalindrome(s: String): String {
    if (s.length < 2) return s
    var start = 0
    var maxLen = 0
    fun expand(l: Int, r: Int) {
        var left = l; var right = r
        while (left >= 0 && right < s.length && s[left] == s[right]) {
            left--; right++
        }
        val len = right - left - 1
        if (len > maxLen) { start = left + 1; maxLen = len }
    }
    for (i in s.indices) {
        expand(i, i)     // Odd length
        expand(i, i + 1) // Even length
    }
    return s.substring(start, start + maxLen)
}""",
                timeComplexity = "O(N^2)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p014", "p015"),
            ),
            CheatsheetItem(
                id = "cs_strings_frequency_hash",
                title = "Character Frequency & Anagram Fingerprints",
                category = ProblemCategory.STRINGS,
                difficulty = Difficulty.EASY,
                summary = "Group anagrams or verify string permutations by computing identical canonical frequency keys.",
                whenToUse =
                    listOf(
                        "Valid anagram check between two strings",
                        "Group anagrams from a list of words",
                        "Ransom note and permutation verification",
                    ),
                keyTricks =
                    listOf(
                        "For lowercase English letters: two strings are anagrams if their 26-element IntArrays match",
                        "To group anagrams: use sorted string or String(charCountArray) as HashMap key",
                        "Early exit: if strings have different lengths, they cannot be anagrams",
                    ),
                codeTemplate =
                    """fun groupAnagrams(strs: Array<String>): List<List<String>> {
    val groups = mutableMapOf<String, MutableList<String>>()
    for (s in strs) {
        val count = IntArray(26)
        for (c in s) count[c - 'a']++
        val key = buildString {
            for (c in count) append('#').append(c)
        }
        groups.computeIfAbsent(key) { mutableListOf() }.add(s)
    }
    return groups.values.toList()
}""",
                timeComplexity = "O(N * K) where K is max string length",
                spaceComplexity = "O(N * K)",
                relatedProblemIds = listOf("p016", "p017"),
            ),
            // ═══════════════ 3. LINKED LISTS ═══════════════
            CheatsheetItem(
                id = "cs_linked_lists_dummy_head",
                title = "Dummy Head & In-Place Reversal",
                category = ProblemCategory.LINKED_LISTS,
                difficulty = Difficulty.EASY,
                summary = "Prepend a sentinel dummy node to eliminate edge cases when inserting, deleting, or merging head nodes.",
                whenToUse =
                    listOf(
                        "Merging two sorted linked lists",
                        "Removing N-th node from end of list",
                        "Reversing a singly linked list in-place",
                    ),
                keyTricks =
                    listOf(
                        "Always return dummy.next at the end of the method",
                        "In three-pointer reversal: save next = curr.next before severing curr.next = prev",
                        "Advance prev = curr and curr = next on each step",
                    ),
                codeTemplate =
                    """class ListNode(var `val`: Int, var next: ListNode? = null)

fun reverseList(head: ListNode?): ListNode? {
    var prev: ListNode? = null
    var curr = head
    while (curr != null) {
        val next = curr.next
        curr.next = prev
        prev = curr
        curr = next
    }
    return prev
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p025", "p027", "p028", "p029"),
            ),
            CheatsheetItem(
                id = "cs_linked_lists_fast_slow",
                title = "Fast & Slow Pointers (Floyd's Tortoise & Hare)",
                category = ProblemCategory.LINKED_LISTS,
                difficulty = Difficulty.MEDIUM,
                summary = "Advance slow pointer by 1 and fast pointer by 2 to detect cycles and locate middle nodes in a single pass.",
                whenToUse =
                    listOf(
                        "Linked list cycle detection",
                        "Finding the starting node of a cycle",
                        "Locating the exact middle of a list (e.g. for Palindrome or Merge Sort)",
                    ),
                keyTricks =
                    listOf(
                        "Loop condition: while (fast != null && fast.next != null)",
                        "If cycle exists, pointers meet; reset slow to head and move both at speed 1 to meet at cycle entrance",
                        "When fast reaches null, slow is at exact middle: n/2 for odd, second middle for even",
                    ),
                codeTemplate =
                    """fun hasCycle(head: ListNode?): Boolean {
    var slow = head
    var fast = head
    while (fast?.next != null) {
        slow = slow?.next
        fast = fast.next?.next
        if (slow === fast) return true
    }
    return false
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p026", "p030", "p031"),
            ),
            // ═══════════════ 4. STACKS & QUEUES ═══════════════
            CheatsheetItem(
                id = "cs_stacks_monotonic",
                title = "Monotonic Stack Pattern",
                category = ProblemCategory.STACKS_QUEUES,
                difficulty = Difficulty.MEDIUM,
                summary = "Maintain elements in strictly increasing or decreasing order inside a stack to resolve nearest greater/smaller queries in O(N).",
                whenToUse =
                    listOf(
                        "Next Greater Element for each item in an array",
                        "Daily Temperatures (days until warmer weather)",
                        "Largest Rectangle in Histogram & Trapping Rain Water",
                    ),
                keyTricks =
                    listOf(
                        "Store indices instead of values in the stack so distance and bounds are immediately accessible",
                        "Pop while stack is non-empty and nums[stack.last()] < nums[i] (for next greater element)",
                        "Every element is pushed once and popped at most once: amortized O(1) per element",
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
                relatedProblemIds = listOf("p035", "p036", "p037", "p038"),
            ),
            CheatsheetItem(
                id = "cs_stacks_parentheses_matching",
                title = "Parentheses & Expression Parsing",
                category = ProblemCategory.STACKS_QUEUES,
                difficulty = Difficulty.EASY,
                summary = "Use a LIFO stack to match closing brackets with their most recent open bracket and evaluate expressions.",
                whenToUse =
                    listOf(
                        "Valid parentheses string verification",
                        "Evaluate Reverse Polish Notation (RPN)",
                        "Decode string with nested bracket multipliers",
                    ),
                keyTricks =
                    listOf(
                        "Use a map of closing-to-opening brackets: mapOf(')' to '(', '}' to '{', ']' to '[')",
                        "If closing bracket encountered: stack must be non-empty and stack.removeLast() must match",
                        "At the end of iteration, valid expression requires stack.isEmpty()",
                    ),
                codeTemplate =
                    """fun isValidParentheses(s: String): Boolean {
    val stack = ArrayDeque<Char>()
    val pairs = mapOf(')' to '(', '}' to '{', ']' to '[')
    for (c in s) {
        if (c in pairs) {
            if (stack.isEmpty() || stack.removeLast() != pairs[c]) return false
        } else {
            stack.addLast(c)
        }
    }
    return stack.isEmpty()
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p035", "p038"),
            ),
            // ═══════════════ 5. TREES ═══════════════
            CheatsheetItem(
                id = "cs_trees_dfs_validation",
                title = "Tree DFS & BST Validity Check",
                category = ProblemCategory.TREES,
                difficulty = Difficulty.MEDIUM,
                summary = "Traverse subtrees recursively while propagating valid upper and lower value bounds.",
                whenToUse =
                    listOf(
                        "Validating a Binary Search Tree (BST)",
                        "Maximum depth and diameter of a binary tree",
                        "Path sum verification from root to leaf",
                    ),
                keyTricks =
                    listOf(
                        "BST validation requires propagating (minBound, maxBound) down the recursive calls",
                        "Do not only compare a node with its direct parent: left child of right child must exceed original root",
                        "Use Long.MIN_VALUE and Long.MAX_VALUE bounds to handle Int.MIN_VALUE node values",
                    ),
                codeTemplate =
                    """class TreeNode(var `val`: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun isValidBST(root: TreeNode?): Boolean {
    fun validate(node: TreeNode?, minVal: Long, maxVal: Long): Boolean {
        if (node == null) return true
        if (node.`val` <= minVal || node.`val` >= maxVal) return false
        return validate(node.left, minVal, node.`val`.toLong()) &&
               validate(node.right, node.`val`.toLong(), maxVal)
    }
    return validate(root, Long.MIN_VALUE, Long.MAX_VALUE)
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(H) recursion stack",
                relatedProblemIds = listOf("p045", "p046", "p047", "p048"),
            ),
            CheatsheetItem(
                id = "cs_trees_bfs_level_order",
                title = "Tree BFS (Level-Order Snapshot)",
                category = ProblemCategory.TREES,
                difficulty = Difficulty.EASY,
                summary = "Process tree nodes level by level using a queue with snapshot sizing.",
                whenToUse =
                    listOf(
                        "Binary tree level order traversal",
                        "Right-side view or zigzag level order",
                        "Minimum depth of binary tree (earliest leaf reached)",
                    ),
                keyTricks =
                    listOf(
                        "Snapshot queue size at the start of each level: val size = queue.size",
                        "Loop repeat(size) { val node = queue.removeFirst() } to process only current level",
                        "Last node in repeat(size) loop represents right-most visible node at that level",
                    ),
                codeTemplate =
                    """fun levelOrder(root: TreeNode?): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    if (root == null) return result
    val queue = ArrayDeque<TreeNode>().apply { addLast(root) }
    while (queue.isNotEmpty()) {
        val levelSize = queue.size
        val currentLevel = mutableListOf<Int>()
        repeat(levelSize) {
            val node = queue.removeFirst()
            currentLevel.add(node.`val`)
            node.left?.let { queue.addLast(it) }
            node.right?.let { queue.addLast(it) }
        }
        result.add(currentLevel)
    }
    return result
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(W) where W is max tree width",
                relatedProblemIds = listOf("p049", "p050", "p051"),
            ),
            CheatsheetItem(
                id = "cs_trees_lowest_common_ancestor",
                title = "Lowest Common Ancestor (LCA)",
                category = ProblemCategory.TREES,
                difficulty = Difficulty.MEDIUM,
                summary = "Find the deepest node that has both target nodes as descendants in binary trees or BSTs.",
                whenToUse =
                    listOf(
                        "Lowest Common Ancestor in Binary Tree",
                        "Lowest Common Ancestor in Binary Search Tree",
                        "Distance between two nodes in a tree",
                    ),
                keyTricks =
                    listOf(
                        "In general binary tree: if left and right subtrees both return non-null, current node is the LCA",
                        "In BST: if both p and q < root.val go left; if both > root.val go right; otherwise root is the split point",
                    ),
                codeTemplate =
                    """fun lowestCommonAncestor(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode? {
    if (root == null || root === p || root === q) return root
    val left = lowestCommonAncestor(root.left, p, q)
    val right = lowestCommonAncestor(root.right, p, q)
    return when {
        left != null && right != null -> root
        left != null -> left
        else -> right
    }
}""",
                timeComplexity = "O(N) for BT, O(log N) for BST",
                spaceComplexity = "O(H)",
                relatedProblemIds = listOf("p053", "p054", "p055"),
            ),
            // ═══════════════ 6. GRAPHS ═══════════════
            CheatsheetItem(
                id = "cs_graphs_bfs_matrix",
                title = "Graph BFS & Grid Island Traversal",
                category = ProblemCategory.GRAPHS,
                difficulty = Difficulty.MEDIUM,
                summary = "Explore 4-directional grids or unweighted adjacency graphs to find shortest paths and connected components.",
                whenToUse =
                    listOf(
                        "Number of Islands (flood fill / connected components)",
                        "Shortest path in a maze or unweighted grid",
                        "Rotting oranges (multi-source BFS)",
                    ),
                keyTricks =
                    listOf(
                        "CRITICAL: mark cell as visited immediately upon ENQUEUEING, not when popping (prevents duplicate queueing / TLE)",
                        "Use directional offsets: val dirs = arrayOf(intArrayOf(0,1), intArrayOf(0,-1), intArrayOf(1,0), intArrayOf(-1,0))",
                        "In-bounds helper: r in 0 until rows && c in 0 until cols",
                    ),
                codeTemplate =
                    """fun numIslands(grid: Array<CharArray>): Int {
    var count = 0
    val rows = grid.size; val cols = grid[0].size
    fun dfs(r: Int, c: Int) {
        if (r !in 0 until rows || c !in 0 until cols || grid[r][c] != '1') return
        grid[r][c] = '0' // Sink island in-place
        dfs(r + 1, c); dfs(r - 1, c); dfs(r, c + 1); dfs(r, c - 1)
    }
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (grid[r][c] == '1') { count++; dfs(r, c) }
        }
    }
    return count
}""",
                timeComplexity = "O(V + E) or O(R * C)",
                spaceComplexity = "O(R * C) recursion / queue",
                relatedProblemIds = listOf("p057", "p058", "p059", "p060"),
            ),
            CheatsheetItem(
                id = "cs_graphs_topological_sort",
                title = "Topological Sort (Kahn's Algorithm)",
                category = ProblemCategory.GRAPHS,
                difficulty = Difficulty.MEDIUM,
                summary = "Linear ordering of vertices in a Directed Acyclic Graph (DAG) using in-degrees and BFS.",
                whenToUse =
                    listOf(
                        "Course Schedule prerequisites",
                        "Task dependency build pipelines",
                        "Detecting cycles in directed graphs (if processed count < V)",
                    ),
                keyTricks =
                    listOf(
                        "Compute inDegree array of size V: inDegree[v]++ for edge (u -> v)",
                        "Enqueue all nodes with inDegree == 0",
                        "When popping u, decrement neighbor inDegree[v]--; if inDegree[v] == 0, enqueue v",
                    ),
                codeTemplate =
                    """fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    val inDegree = IntArray(numCourses)
    val adj = Array(numCourses) { mutableListOf<Int>() }
    for (p in prerequisites) {
        val course = p[0]; val prereq = p[1]
        adj[prereq].add(course)
        inDegree[course]++
    }
    val queue = ArrayDeque<Int>()
    for (i in 0 until numCourses) if (inDegree[i] == 0) queue.addLast(i)
    var count = 0
    while (queue.isNotEmpty()) {
        val curr = queue.removeFirst()
        count++
        for (neighbor in adj[curr]) {
            if (--inDegree[neighbor] == 0) queue.addLast(neighbor)
        }
    }
    return count == numCourses
}""",
                timeComplexity = "O(V + E)",
                spaceComplexity = "O(V + E)",
                relatedProblemIds = listOf("p059", "p061", "p062"),
            ),
            CheatsheetItem(
                id = "cs_graphs_union_find",
                title = "Disjoint Set Union (Union-Find)",
                category = ProblemCategory.GRAPHS,
                difficulty = Difficulty.MEDIUM,
                summary = "Maintain partition of elements into disjoint sets with path compression and rank union in near O(1) amortized time.",
                whenToUse =
                    listOf(
                        "Detecting cycles in undirected graphs (Redundant Connection)",
                        "Counting connected components dynamically",
                        "Kruskal's Minimum Spanning Tree",
                    ),
                keyTricks =
                    listOf(
                        "Path compression in find(): parent[x] = find(parent[x]) flattens tree structure",
                        "Union by rank: attach shorter tree under root of taller tree",
                        "If find(u) == find(v) before union, adding edge (u, v) creates a cycle!",
                    ),
                codeTemplate =
                    """class UnionFind(n: Int) {
    val parent = IntArray(n) { it }
    val rank = IntArray(n) { 1 }
    var count = n

    fun find(x: Int): Int {
        if (parent[x] != x) parent[x] = find(parent[x])
        return parent[x]
    }
    fun union(x: Int, y: Int): Boolean {
        val rootX = find(x); val rootY = find(y)
        if (rootX == rootY) return false
        if (rank[rootX] < rank[rootY]) parent[rootX] = rootY
        else if (rank[rootX] > rank[rootY]) parent[rootY] = rootX
        else { parent[rootY] = rootX; rank[rootX]++ }
        count--
        return true
    }
}""",
                timeComplexity = "O(alpha(N)) ≈ O(1) amortized",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p064", "p065", "p066"),
            ),
            // ═══════════════ 7. DYNAMIC PROGRAMMING ═══════════════
            CheatsheetItem(
                id = "cs_dp_1d_knapsack",
                title = "1D DP & 0/1 Knapsack Space Optimization",
                category = ProblemCategory.DYNAMIC_PROGRAMMING,
                difficulty = Difficulty.MEDIUM,
                summary = "Break down combinatorial choices into 1D tabulation, optimizing memory from O(N*W) to O(W).",
                whenToUse =
                    listOf(
                        "Coin Change (minimum coins & total ways)",
                        "0/1 Knapsack & Partition Equal Subset Sum",
                        "House Robber & Climbing Stairs",
                    ),
                keyTricks =
                    listOf(
                        "0/1 Knapsack (each item at most once): loop target backward 'for (w in target downTo itemWeight)'",
                        "Unbounded Knapsack / Coin Change (items can repeat): loop target forward 'for (w in coin..amount)'",
                        "Initialize minimum dp array with amount + 1 (infinity sentinel) and dp[0] = 0",
                    ),
                codeTemplate =
                    """fun coinChange(coins: IntArray, amount: Int): Int {
    val max = amount + 1
    val dp = IntArray(amount + 1) { max }
    dp[0] = 0
    for (coin in coins) {
        for (w in coin..amount) {
            dp[w] = minOf(dp[w], dp[w - coin] + 1)
        }
    }
    return if (dp[amount] > amount) -1 else dp[amount]
}""",
                timeComplexity = "O(N * W)",
                spaceComplexity = "O(W)",
                relatedProblemIds = listOf("p067", "p068", "p071", "p072"),
            ),
            CheatsheetItem(
                id = "cs_dp_2d_strings",
                title = "2D Grid DP (LCS & Edit Distance)",
                category = ProblemCategory.DYNAMIC_PROGRAMMING,
                difficulty = Difficulty.HARD,
                summary = "Compare two sequences along a 2D matrix where dp[i][j] represents solution for prefixes s1[0..i-1] and s2[0..j-1].",
                whenToUse =
                    listOf(
                        "Longest Common Subsequence (LCS)",
                        "Edit Distance (Levenshtein Distance)",
                        "Unique Paths in a Grid",
                    ),
                keyTricks =
                    listOf(
                        "Size matrix (m + 1) x (n + 1) to simplify base cases representing empty string prefixes",
                        "If characters match s1[i-1] == s2[j-1]: dp[i][j] = dp[i-1][j-1] + 1",
                        "If mismatch: dp[i][j] = maxOf(dp[i-1][j], dp[i][j-1])",
                    ),
                codeTemplate =
                    """fun longestCommonSubsequence(text1: String, text2: String): Int {
    val m = text1.length; val n = text2.length
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
                spaceComplexity = "O(M * N) (or O(min(M,N)) with rolling array)",
                relatedProblemIds = listOf("p070", "p073", "p074"),
            ),
            CheatsheetItem(
                id = "cs_dp_lis_binary_search",
                title = "Longest Increasing Subsequence (Patience Sorting)",
                category = ProblemCategory.DYNAMIC_PROGRAMMING,
                difficulty = Difficulty.MEDIUM,
                summary = "Compute length of Longest Increasing Subsequence in O(N log N) using binary search on tails.",
                whenToUse =
                    listOf(
                        "Longest Increasing Subsequence (LIS)",
                        "Russian Doll Envelopes",
                        "Minimum number of increasing subsequences to cover array",
                    ),
                keyTricks =
                    listOf(
                        "Maintain array 'tails' where tails[i] stores smallest tail element of all increasing subsequences of length i + 1",
                        "For each x in nums: binary search for x in tails; if not found, insertion point replaces first element >= x",
                        "If x > all elements in tails, append x to extend LIS length",
                    ),
                codeTemplate =
                    """fun lengthOfLIS(nums: IntArray): Int {
    val tails = IntArray(nums.size)
    var len = 0
    for (x in nums) {
        var l = 0; var r = len
        while (l < r) {
            val mid = l + (r - l) / 2
            if (tails[mid] < x) l = mid + 1 else r = mid
        }
        tails[l] = x
        if (l == len) len++
    }
    return len
}""",
                timeComplexity = "O(N log N)",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p069", "p075"),
            ),
            // ═══════════════ 8. SORTING & SEARCHING ═══════════════
            CheatsheetItem(
                id = "cs_search_binary_on_answer",
                title = "Binary Search on Answer Range",
                category = ProblemCategory.SORTING_SEARCHING,
                difficulty = Difficulty.MEDIUM,
                summary = "When the problem asks for minimum maximum or optimal threshold, binary search the feasible range [low..high].",
                whenToUse =
                    listOf(
                        "Capacity to Ship Packages within D Days",
                        "Koko Eating Bananas (minimum eating speed)",
                        "Split Array Largest Sum",
                    ),
                keyTricks =
                    listOf(
                        "Identify the monotonic feasibility predicate canAchieve(mid): if valid for mid, all values > mid are valid",
                        "Calculate mid safely: val mid = low + (high - low) / 2",
                        "Search range is bounded by [minPossibleAnswer, maxPossibleAnswer]",
                    ),
                codeTemplate =
                    """fun shipWithinDays(weights: IntArray, days: Int): Int {
    var low = weights.maxOrNull() ?: 0
    var high = weights.sum()
    fun canShip(capacity: Int): Boolean {
        var neededDays = 1; var currentLoad = 0
        for (w in weights) {
            if (currentLoad + w > capacity) { neededDays++; currentLoad = 0 }
            currentLoad += w
        }
        return neededDays <= days
    }
    while (low < high) {
        val mid = low + (high - low) / 2
        if (canShip(mid)) high = mid else low = mid + 1
    }
    return low
}""",
                timeComplexity = "O(N * log(high - low))",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p079", "p080", "p081", "p082"),
            ),
            CheatsheetItem(
                id = "cs_sort_intervals_merge",
                title = "Interval Merging & Sweep-Line",
                category = ProblemCategory.SORTING_SEARCHING,
                difficulty = Difficulty.MEDIUM,
                summary = "Sort intervals by start time to linearize overlapping relationships and merge sequentially.",
                whenToUse =
                    listOf(
                        "Merge overlapping intervals",
                        "Meeting Rooms (maximum concurrent rooms)",
                        "Insert interval into sorted disjoint list",
                    ),
                keyTricks =
                    listOf(
                        "Sort intervals: intervals.sortWith(compareBy { it[0] })",
                        "If current.start <= prev.end: overlap detected! Update prev.end = maxOf(prev.end, current.end)",
                        "Otherwise, no overlap: append current interval as new baseline",
                    ),
                codeTemplate =
                    """fun mergeIntervals(intervals: Array<IntArray>): Array<IntArray> {
    if (intervals.isEmpty()) return emptyArray()
    intervals.sortWith(compareBy { it[0] })
    val merged = mutableListOf<IntArray>()
    var current = intervals[0]
    for (i in 1 until intervals.size) {
        val next = intervals[i]
        if (next[0] <= current[1]) {
            current[1] = maxOf(current[1], next[1])
        } else {
            merged.add(current)
            current = next
        }
    }
    merged.add(current)
    return merged.toTypedArray()
}""",
                timeComplexity = "O(N log N)",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p083", "p084", "p085", "p086"),
            ),
            // ═══════════════ 9. HASH-BASED ═══════════════
            CheatsheetItem(
                id = "cs_hash_two_sum_complement",
                title = "Two Sum Complement Lookup",
                category = ProblemCategory.HASH_BASED,
                difficulty = Difficulty.EASY,
                summary = "Check for existence of (target - current) in a HashMap during a single forward pass.",
                whenToUse =
                    listOf(
                        "Two Sum and element pair matching",
                        "Finding duplicate elements within K distance",
                        "Checking if two elements difference equals target",
                    ),
                keyTricks =
                    listOf(
                        "Lookup complement BEFORE inserting current element to avoid matching the element with itself",
                        "Store value -> index when problem asks for indices",
                    ),
                codeTemplate =
                    """fun twoSum(nums: IntArray, target: Int): IntArray {
    val map = HashMap<Int, Int>()
    for (i in nums.indices) {
        val complement = target - nums[i]
        map[complement]?.let { return intArrayOf(it, i) }
        map[nums[i]] = i
    }
    return intArrayOf()
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p087", "p088"),
            ),
            CheatsheetItem(
                id = "cs_hash_prefix_count",
                title = "Prefix Sum + Frequency Map",
                category = ProblemCategory.HASH_BASED,
                difficulty = Difficulty.MEDIUM,
                summary = "Combine prefix sums with hash count to find total subarrays summing to K in O(N) time.",
                whenToUse =
                    listOf(
                        "Subarray sum equals K",
                        "Continuous subarray sum divisible by K",
                        "Longest subarray with equal 0s and 1s (convert 0 to -1)",
                    ),
                keyTricks =
                    listOf(
                        "MANDATORY: seed map with map[0] = 1 (or map[0] = -1 for index tracking) to count subarrays starting from index 0",
                        "At each index: currentSum += num; count += map.getOrDefault(currentSum - k, 0)",
                        "Store running prefix counts, never re-iterate over past prefix arrays",
                    ),
                codeTemplate =
                    """fun subarraySum(nums: IntArray, k: Int): Int {
    val prefixCounts = HashMap<Int, Int>().apply { put(0, 1) }
    var runningSum = 0
    var totalSubarrays = 0
    for (num in nums) {
        runningSum += num
        totalSubarrays += prefixCounts.getOrDefault(runningSum - k, 0)
        prefixCounts[runningSum] = prefixCounts.getOrDefault(runningSum, 0) + 1
    }
    return totalSubarrays
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p089", "p090", "p091"),
            ),
            // ═══════════════ 10. RECURSION & MATH ═══════════════
            CheatsheetItem(
                id = "cs_math_backtracking_subsets",
                title = "Backtracking (Subsets & Permutations)",
                category = ProblemCategory.RECURSION_MATH,
                difficulty = Difficulty.MEDIUM,
                summary = "Exhaustively generate configurations using Choose -> Explore -> Unchoose backtracking pattern.",
                whenToUse =
                    listOf(
                        "Generating all subsets (power set) or combinations",
                        "Permutations of an array",
                        "N-Queens, Sudoku, Word Search",
                    ),
                keyTricks =
                    listOf(
                        "Always add a copy of the path to result: result.add(ArrayList(path))",
                        "Unchoose: path.removeAt(path.size - 1) before backtracking to parent state",
                        "To handle duplicates: sort array first and skip if (i > start && nums[i] == nums[i-1]) continue",
                    ),
                codeTemplate =
                    """fun subsets(nums: IntArray): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    val path = mutableListOf<Int>()
    fun backtrack(start: Int) {
        result.add(ArrayList(path))
        for (i in start until nums.size) {
            path.add(nums[i])      // Choose
            backtrack(i + 1)       // Explore
            path.removeAt(path.size - 1) // Unchoose
        }
    }
    backtrack(0)
    return result
}""",
                timeComplexity = "O(2^N * N)",
                spaceComplexity = "O(N) recursion stack",
                relatedProblemIds = listOf("p093", "p094"),
            ),
            CheatsheetItem(
                id = "cs_math_fast_exp_gcd",
                title = "Euclidean GCD & Binary Exponentiation",
                category = ProblemCategory.RECURSION_MATH,
                difficulty = Difficulty.MEDIUM,
                summary = "Compute greatest common divisors in O(log(min(a,b))) and powers x^N in O(log N).",
                whenToUse =
                    listOf(
                        "Computing GCD / LCM of numbers",
                        "Pow(x, n) with positive and negative powers",
                        "Modular exponentiation (x^n mod m)",
                    ),
                keyTricks =
                    listOf(
                        "Euclidean GCD: gcd(a, b) = if (b == 0) a else gcd(b, a % b)",
                        "When n is negative in Pow(x, n): convert n to Long before negating because -Int.MIN_VALUE overflows",
                        "Square base and halve power when N is even: x^N = (x^2)^(N/2)",
                    ),
                codeTemplate =
                    """fun myPow(x: Double, n: Int): Double {
    var p = n.toLong()
    var base = x
    if (p < 0) { base = 1.0 / base; p = -p }
    var ans = 1.0
    while (p > 0) {
        if (p % 2 == 1L) ans *= base
        base *= base
        p /= 2
    }
    return ans
}""",
                timeComplexity = "O(log N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p095", "p096"),
            ),
            // ═══════════════ 11. BIT MANIPULATION ═══════════════
            CheatsheetItem(
                id = "cs_bit_xor_single_number",
                title = "XOR Invariants & Single Number",
                category = ProblemCategory.BIT_MANIPULATION,
                difficulty = Difficulty.EASY,
                summary = "XORing identical numbers cancels them out (x xor x = 0), leaving only unpaired numbers.",
                whenToUse =
                    listOf(
                        "Finding the unique single element where every other element appears twice",
                        "Finding two non-duplicate numbers in an array of pairs",
                        "Detecting the missing number in range [0..N]",
                    ),
                keyTricks =
                    listOf(
                        "Properties: x xor 0 = x; x xor x = 0; associative & commutative",
                        "Bitwise precedence: always parenthesize (x and 1) == 0",
                        "Isolate lowest set bit: val lowest = x and -x",
                    ),
                codeTemplate =
                    """fun singleNumber(nums: IntArray): Int {
    var result = 0
    for (num in nums) {
        result = result xor num
    }
    return result
}""",
                timeComplexity = "O(N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p097", "p098"),
            ),
            CheatsheetItem(
                id = "cs_bit_powers_and_counting",
                title = "Bit Counting & Power of 2 Tricks",
                category = ProblemCategory.BIT_MANIPULATION,
                difficulty = Difficulty.EASY,
                summary = "Use binary bit clearing expressions like x and (x - 1) to test powers of two and count set bits.",
                whenToUse =
                    listOf(
                        "Number of 1 Bits (Hamming Weight)",
                        "Power of Two verification",
                        "Bitmask iteration over 2^N subsets",
                    ),
                keyTricks =
                    listOf(
                        "x and (x - 1) zeroes the least significant 1-bit",
                        "Power of 2 test: (x > 0) && ((x and (x - 1)) == 0)",
                        "Check if i-th bit is set: (mask and (1 shl i)) != 0",
                    ),
                codeTemplate =
                    """fun hammingWeight(n: Int): Int {
    var count = 0
    var num = n
    while (num != 0) {
        num = num and (num - 1) // Removes lowest 1-bit
        count++
    }
    return count
}""",
                timeComplexity = "O(k) where k is number of 1-bits (<= 32)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p097", "p098"),
            ),
            // ═══════════════ 12. MISCELLANEOUS ═══════════════
            CheatsheetItem(
                id = "cs_misc_greedy_intervals",
                title = "Greedy Activity Selection",
                category = ProblemCategory.MISCELLANEOUS,
                difficulty = Difficulty.MEDIUM,
                summary = "Greedily select intervals with the earliest finish time to maximize non-overlapping selections.",
                whenToUse =
                    listOf(
                        "Non-overlapping intervals (minimum removals)",
                        "Gas Station circuit tour",
                        "Jump Game reachability",
                    ),
                keyTricks =
                    listOf(
                        "Sort intervals by end time (it[1]) for activity selection",
                        "Keep track of last valid end time; discard any interval starting before lastEnd",
                        "Verify greedy choice property: locally optimal choice must lead to global optimum",
                    ),
                codeTemplate =
                    """fun eraseOverlapIntervals(intervals: Array<IntArray>): Int {
    if (intervals.isEmpty()) return 0
    intervals.sortWith(compareBy { it[1] }) // Sort by end time
    var count = 0
    var prevEnd = intervals[0][1]
    for (i in 1 until intervals.size) {
        if (intervals[i][0] < prevEnd) {
            count++ // Remove overlapping interval
        } else {
            prevEnd = intervals[i][1]
        }
    }
    return count
}""",
                timeComplexity = "O(N log N)",
                spaceComplexity = "O(1)",
                relatedProblemIds = listOf("p099"),
            ),
            CheatsheetItem(
                id = "cs_misc_two_heaps_median",
                title = "Two Heaps Pattern (Running Median)",
                category = ProblemCategory.MISCELLANEOUS,
                difficulty = Difficulty.HARD,
                summary = "Partition data stream into a Max-Heap (lower half) and a Min-Heap (upper half) to retrieve median in O(1).",
                whenToUse =
                    listOf(
                        "Find median from continuous data stream",
                        "Sliding window median",
                        "Balancing running percentiles",
                    ),
                keyTricks =
                    listOf(
                        "Max-Heap stores lower half: PriorityQueue<Int>(compareByDescending { it })",
                        "Min-Heap stores upper half: PriorityQueue<Int>()",
                        "Balance condition: small.size == large.size || small.size == large.size + 1",
                        "Median is small.peek() if odd count, else (small.peek() + large.peek()) / 2.0",
                    ),
                codeTemplate =
                    """class MedianFinder() {
    private val small = java.util.PriorityQueue<Int>(compareByDescending { it })
    private val large = java.util.PriorityQueue<Int>()

    fun addNum(num: Int) {
        small.offer(num)
        large.offer(small.poll())
        if (small.size < large.size) {
            small.offer(large.poll())
        }
    }

    fun findMedian(): Double {
        return if (small.size > large.size) {
            small.peek().toDouble()
        } else {
            (small.peek() + large.peek()) / 2.0
        }
    }
}""",
                timeComplexity = "O(log N) insert, O(1) findMedian",
                spaceComplexity = "O(N)",
                relatedProblemIds = listOf("p100"),
            ),
        )
}
