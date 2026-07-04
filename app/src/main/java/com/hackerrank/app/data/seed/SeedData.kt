package com.hackerrank.app.data.seed

import com.hackerrank.app.data.local.entity.DataStructureEntity
import com.hackerrank.app.data.local.entity.QuizQuestionEntity
import com.hackerrank.app.data.local.entity.UserProfileEntity
import com.hackerrank.app.data.local.entity.UserProgressEntity

object SeedData {

    fun getStructures(): List<DataStructureEntity> = listOf(
        /* ────────────── LINEAR ────────────── */
        DataStructureEntity(
            id = "array", name = "Array", slug = "array", category = "Linear",
            explanation = "An array is a linear data structure that stores elements in contiguous memory locations. Each element can be accessed directly by its index, making lookup extremely fast. Arrays have a fixed size in most languages but dynamic arrays (like Kotlin's ArrayList or Java's ArrayList) can grow as needed.\n\nKey characteristics:\n• Contiguous memory allocation\n• Zero-based indexing\n• Homogeneous elements (same data type)\n• Constant-time O(1) access by index",
            complexityJson = """{"Access":"O(1)","Search":"O(n)","Insert":"O(n)","Delete":"O(n)","Space":"O(n)"}""",
            whenToUseJson = """["When you need fast indexed access to elements","When the data size is known or predictable in advance","When you need memory efficiency (no overhead per element)","When iterating through elements sequentially"]""",
            diagramRes = null, difficulty = "Easy",
            codeExample = """fun main() {
    // Fixed-size array
    val arr = intArrayOf(1, 2, 3, 4, 5)
    println(arr[0]) // O(1) access → 1

    // Dynamic list
    val list = mutableListOf(1, 2, 3)
    list.add(4)
    list.removeAt(0)
    println(list) // [2, 3, 4]
}"""
        ),
        DataStructureEntity(
            id = "linked_list", name = "Linked List", slug = "linked-list", category = "Linear",
            explanation = "A linked list is a linear data structure where elements (nodes) are stored non-contiguously, connected via pointers. Each node contains data and a reference to the next node. Singly linked lists have a single next pointer; doubly linked lists have both next and prev pointers; circular linked lists connect the tail back to the head.\n\nKey characteristics:\n• Dynamic size (grows/shrinks at runtime)\n• No memory wastage (allocated per element)\n• Sequential access only (no direct indexing)\n• Efficient insertions/deletions at head",
            complexityJson = """{"Access":"O(n)","Search":"O(n)","Insert (Head)":"O(1)","Insert (Tail)":"O(n)","Delete":"O(1)*","Space":"O(n)"}""",
            whenToUseJson = """["When you need frequent insertions/deletions at the beginning","When the data size changes frequently and unpredictably","When implementing stacks, queues, or adjacency lists for graphs","When memory fragmentation is a concern with arrays"]""",
            diagramRes = null, difficulty = "Medium",
            codeExample = """class Node(val data: Int, var next: Node? = null)

class LinkedList {
    var head: Node? = null

    fun insertAtHead(data: Int) {
        val newNode = Node(data)
        newNode.next = head
        head = newNode
    }

    fun printAll() {
        var curr = head
        while (curr != null) {
            print("${'$'}{curr.data} → ")
            curr = curr.next
        }
        println("null")
    }
}"""
        ),
        DataStructureEntity(
            id = "stack", name = "Stack", slug = "stack", category = "Linear",
            explanation = "A stack is a linear data structure that follows the Last-In-First-Out (LIFO) principle. Elements are added (pushed) and removed (popped) from the top only. Think of a stack of plates — you can only take the top plate off.\n\nKey characteristics:\n• LIFO ordering\n• Operations: push (add), pop (remove), peek (view top)\n• Can be implemented using arrays or linked lists\n• Used extensively in function call stacks and expression evaluation",
            complexityJson = """{"Push":"O(1)","Pop":"O(1)","Peek":"O(1)","Search":"O(n)","Space":"O(n)"}""",
            whenToUseJson = """["When you need LIFO (last-in-first-out) behavior","For undo/redo functionality in editors","For parsing expressions (balanced parentheses, postfix evaluation)","For backtracking algorithms (DFS, maze solving)","For function call management (call stack)"]""",
            diagramRes = null, difficulty = "Easy",
            codeExample = """fun main() {
    val stack = ArrayDeque<Int>()
    stack.addLast(1) // push
    stack.addLast(2)
    stack.addLast(3)
    println(stack.last())    // peek → 3
    println(stack.removeLast()) // pop → 3
    println(stack) // [1, 2]
}"""
        ),
        DataStructureEntity(
            id = "queue", name = "Queue", slug = "queue", category = "Linear",
            explanation = "A queue is a linear data structure that follows the First-In-First-Out (FIFO) principle. Elements are added at the rear (enqueue) and removed from the front (dequeue). Think of a line at a ticket counter — the first person in line is served first.\n\nVariations include Circular Queue (reuses space), Priority Queue (elements ordered by priority), and Deque (double-ended, insert/remove from both ends).",
            complexityJson = """{"Enqueue":"O(1)","Dequeue":"O(1)","Peek":"O(1)","Search":"O(n)","Space":"O(n)"}""",
            whenToUseJson = """["When you need FIFO (first-in-first-out) behavior","For breadth-first search (BFS) in graphs","For task scheduling and job queues","For buffering data streams","For handling requests in order (print queue, message queue)"]""",
            diagramRes = null, difficulty = "Easy",
            codeExample = """fun main() {
    val queue = ArrayDeque<Int>()
    queue.addLast(1) // enqueue
    queue.addLast(2)
    queue.addLast(3)
    println(queue.first())    // peek → 1
    println(queue.removeFirst()) // dequeue → 1
    println(queue) // [2, 3]
}"""
        ),

        /* ────────────── TREES ────────────── */
        DataStructureEntity(
            id = "binary_tree", name = "Binary Tree", slug = "binary-tree", category = "Trees",
            explanation = "A binary tree is a hierarchical data structure where each node has at most two children, referred to as the left child and right child. The topmost node is called the root. Nodes with no children are called leaves.\n\nKey characteristics:\n• Each node has at most 2 children\n• Strictly hierarchical (no cycles)\n• Depth = number of edges from root to node\n• Height = max depth of any node\n• Full binary tree: every node has 0 or 2 children\n• Complete binary tree: all levels filled except possibly the last\n• Perfect binary tree: all internal nodes have 2 children, all leaves at same level",
            complexityJson = """{"Search (unsorted)":"O(n)","Insert":"O(n)","Delete":"O(n)","Traversal":"O(n)","Space":"O(n)"}""",
            whenToUseJson = """["When data has a hierarchical relationship","For representing file systems, organization charts","For expression trees (compilers, calculators)","As the foundation for BST, AVL, and Heap structures","For Huffman encoding (compression algorithms)"]""",
            diagramRes = null, difficulty = "Medium",
            codeExample = """class TreeNode(val value: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

fun inOrder(node: TreeNode?) {
    if (node == null) return
    inOrder(node.left)
    print("${'$'}{node.value} ")
    inOrder(node.right)
}"""
        ),
        DataStructureEntity(
            id = "bst", name = "Binary Search Tree", slug = "binary-search-tree", category = "Trees",
            explanation = "A Binary Search Tree (BST) is a binary tree with a special ordering property: for every node, all values in its left subtree are less than the node's value, and all values in its right subtree are greater. This property enables efficient searching.\n\nKey characteristics:\n• Left subtree contains values < node\n• Right subtree contains values > node\n• No duplicate values (typically)\n• In-order traversal yields sorted order\n• Performance depends on tree balance — worst case O(n) if skewed",
            complexityJson = """{"Search (avg)":"O(log n)","Search (worst)":"O(n)","Insert (avg)":"O(log n)","Delete (avg)":"O(log n)","Space":"O(n)"}""",
            whenToUseJson = """["When you need fast search, insert, and delete operations","When you need to maintain a sorted collection dynamically","For implementing maps and sets","For solving range query problems","When the data doesn't have a predictable insertion order"]""",
            diagramRes = null, difficulty = "Medium",
            codeExample = """class BST {
    var root: TreeNode? = null

    fun insert(value: Int) {
        root = insertRec(root, value)
    }

    private fun insertRec(node: TreeNode?, value: Int): TreeNode {
        if (node == null) return TreeNode(value)
        if (value < node.value)
            node.left = insertRec(node.left, value)
        else if (value > node.value)
            node.right = insertRec(node.right, value)
        return node
    }

    fun search(value: Int): Boolean {
        var curr = root
        while (curr != null) {
            when {
                value == curr.value -> return true
                value < curr.value -> curr = curr.left
                else -> curr = curr.right
            }
        }
        return false
    }
}"""
        ),
        DataStructureEntity(
            id = "avl_tree", name = "AVL Tree", slug = "avl-tree", category = "Trees",
            explanation = "An AVL tree is a self-balancing Binary Search Tree where the height difference (balance factor) between the left and right subtrees of any node is at most 1. After every insertion or deletion, rotations are performed to maintain balance, guaranteeing O(log n) operations.\n\nBalance factor = height(left) - height(right). Allowed values: -1, 0, 1.\nRotations: Left-Left (right rotate), Right-Right (left rotate), Left-Right (left-right rotate), Right-Left (right-left rotate).",
            complexityJson = """{"Search":"O(log n)","Insert":"O(log n)","Delete":"O(log n)","Space":"O(n)"}""",
            whenToUseJson = """["When you need guaranteed O(log n) search performance","When insertion order is unpredictable and could cause BST skew","For database indexing systems","For memory-constrained systems where worst-case matters","When building sorted sets/maps with guaranteed performance"]""",
            diagramRes = null, difficulty = "Hard",
            codeExample = """class AVLNode(val value: Int) {
    var left: AVLNode? = null
    var right: AVLNode? = null
    var height: Int = 1

    fun balanceFactor(): Int =
        (left?.height ?: 0) - (right?.height ?: 0)
}"""
        ),
        DataStructureEntity(
            id = "heap", name = "Heap", slug = "heap", category = "Trees",
            explanation = "A heap is a specialized complete binary tree that satisfies the heap property. In a Max-Heap, the parent node is always greater than or equal to its children (the largest element is at the root). In a Min-Heap, the parent is always smaller than or equal to its children (the smallest element is at the root).\n\nKey characteristics:\n• Complete binary tree (all levels filled left-to-right)\n• Heap property: parent ≥ children (max-heap) or parent ≤ children (min-heap)\n• Typically implemented using arrays\n• Insert and extract operations are O(log n)\n• Heapify builds a heap in O(n)",
            complexityJson = """{"Insert":"O(log n)","Extract Max/Min":"O(log n)","Peek":"O(1)","Heapify":"O(n)","Space":"O(n)"}""",
            whenToUseJson = """["For implementing priority queues","For finding the k-th largest/smallest element","For heap sort (O(n log n) sorting)","For scheduling algorithms (priority-based)","For streaming median calculation"]""",
            diagramRes = null, difficulty = "Medium",
            codeExample = """fun main() {
    // Min-Heap using PriorityQueue
    val minHeap = PriorityQueue<Int>()
    minHeap.add(5)
    minHeap.add(3)
    minHeap.add(7)
    println(minHeap.poll()) // 3 (smallest)

    // Max-Heap
    val maxHeap = PriorityQueue<Int>(reverseOrder())
    maxHeap.add(5)
    maxHeap.add(3)
    maxHeap.add(7)
    println(maxHeap.poll()) // 7 (largest)
}"""
        ),
        DataStructureEntity(
            id = "trie", name = "Trie (Prefix Tree)", slug = "trie", category = "Trees",
            explanation = "A trie (pronounced 'try') is a tree-like data structure that stores strings by splitting them into characters. Each node represents a single character, and paths from the root to leaf nodes form complete words. All descendants of a node share the same prefix.\n\nKey characteristics:\n• Each node represents one character\n• Root represents empty string\n• Nodes can have up to 26 children (for lowercase English)\n• Word end marker to distinguish prefixes from complete words\n• Space-inefficient but extremely fast prefix lookups\n• Also called a digital tree or radix tree",
            complexityJson = """{"Insert":"O(m)","Search":"O(m)","Prefix Search":"O(m)","Delete":"O(m)","Space":"O(n × m)"}""",
            whenToUseJson = """["For autocomplete and search suggestions","For spell checking and dictionary implementations","For IP routing (longest prefix matching)","For solving word games (Boggle, Scrabble)","For text pattern matching and predictive text input"]""",
            diagramRes = null, difficulty = "Hard",
            codeExample = """class TrieNode {
    val children = Array<TrieNode?>(26) { null }
    var isEndOfWord = false
}

class Trie {
    val root = TrieNode()

    fun insert(word: String) {
        var node = root
        for (c in word) {
            val idx = c - 'a'
            if (node.children[idx] == null)
                node.children[idx] = TrieNode()
            node = node.children[idx]!!
        }
        node.isEndOfWord = true
    }

    fun search(word: String): Boolean {
        val node = findNode(word)
        return node?.isEndOfWord == true
    }

    fun startsWith(prefix: String): Boolean =
        findNode(prefix) != null

    private fun findNode(str: String): TrieNode? {
        var node = root
        for (c in str) {
            val idx = c - 'a'
            node = node.children[idx] ?: return null
        }
        return node
    }
}"""
        ),

        /* ────────────── GRAPHS ────────────── */
        DataStructureEntity(
            id = "graph", name = "Graph", slug = "graph", category = "Graphs",
            explanation = "A graph is a non-linear data structure consisting of vertices (nodes) and edges (connections between nodes). Graphs model pairwise relationships between objects. They can be directed (edges have direction) or undirected (edges are bidirectional), weighted (edges have costs) or unweighted.\n\nKey representations:\n• Adjacency Matrix: O(V²) space, O(1) edge lookup\n• Adjacency List: O(V+E) space, O(degree(v)) edge lookup\n\nCommon traversals:\n• BFS (Breadth-First Search): Uses queue, finds shortest path in unweighted graph\n• DFS (Depth-First Search): Uses stack/recursion, explores deep paths first",
            complexityJson = """{"BFS":"O(V + E)","DFS":"O(V + E)","Edge Lookup (Matrix)":"O(1)","Edge Lookup (List)":"O(degree)","Space (Matrix)":"O(V²)","Space (List)":"O(V + E)"}""",
            whenToUseJson = """["For modeling networks (social, computer, transportation)","For finding shortest paths (maps, GPS navigation)","For dependency resolution (build systems, package managers)","For web crawling and page ranking","For recommendation systems (user-item relationships)"]""",
            diagramRes = null, difficulty = "Medium",
            codeExample = """class Graph(val vertices: Int) {
    private val adj = Array(vertices) { mutableListOf<Int>() }

    fun addEdge(u: Int, v: Int) {
        adj[u].add(v)
        adj[v].add(u) // undirected
    }

    fun bfs(start: Int) {
        val visited = BooleanArray(vertices)
        val queue = ArrayDeque<Int>()
        visited[start] = true
        queue.addLast(start)
        while (queue.isNotEmpty()) {
            val v = queue.removeFirst()
            print("${'$'}v ")
            for (n in adj[v]) {
                if (!visited[n]) {
                    visited[n] = true
                    queue.addLast(n)
                }
            }
        }
    }
}"""
        ),
        DataStructureEntity(
            id = "weighted_graph", name = "Weighted Graph", slug = "weighted-graph", category = "Graphs",
            explanation = "A weighted graph extends the standard graph by assigning a numerical weight to each edge, representing cost, distance, capacity, or any other metric. Edge weights enable optimization problems like finding the shortest or cheapest path.\n\nDijkstra's algorithm finds the shortest path from a source to all vertices in a weighted graph with non-negative weights. It uses a priority queue to greedily select the closest unvisited vertex. Bellman-Ford handles negative weights but is slower.",
            complexityJson = """{"Dijkstra":"O((V+E) log V)","Bellman-Ford":"O(V × E)","Space":"O(V + E)"}""",
            whenToUseJson = """["For GPS navigation (shortest path with road distances)","For network routing (minimizing latency or hop cost)","For airline route planning (minimizing cost or time)","For project scheduling (critical path method)","For any optimization problem with costs on connections"]""",
            diagramRes = null, difficulty = "Hard",
            codeExample = """data class Edge(val to: Int, val weight: Int)

fun dijkstra(graph: List<List<Edge>>, source: Int): IntArray {
    val dist = IntArray(graph.size) { Int.MAX_VALUE }
    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
    dist[source] = 0
    pq.add(0 to source)
    while (pq.isNotEmpty()) {
        val (d, u) = pq.poll()
        if (d > dist[u]) continue
        for (e in graph[u]) {
            val nd = d + e.weight
            if (nd < dist[e.to]) {
                dist[e.to] = nd
                pq.add(nd to e.to)
            }
        }
    }
    return dist
}"""
        ),
        DataStructureEntity(
            id = "graph_algorithms", name = "Graph Algorithms", slug = "graph-algorithms", category = "Graphs",
            explanation = "Beyond BFS and DFS, several fundamental graph algorithms solve common problems:\n\n• Topological Sort: Linear ordering of DAG vertices where every edge goes from earlier to later vertex. Used for scheduling.\n• Minimum Spanning Tree (Kruskal's/Prim's): Connects all vertices with minimum total edge weight.\n• Strongly Connected Components (Kosaraju's/Tarjan's): Finds mutually reachable vertex groups in directed graphs.\n• Union-Find (Disjoint Set): Tracks connected components efficiently.",
            complexityJson = """{"Topological Sort":"O(V + E)","MST (Kruskal's)":"O(E log E)","SCC (Kosaraju)":"O(V + E)","Cycle Detection":"O(V + E)"}""",
            whenToUseJson = """["For task scheduling with dependencies (topological sort)","For designing minimum-cost networks (MST)","For detecting cycles in dependencies","For finding strongly connected clusters in social networks","For solving dynamic connectivity problems"]""",
            diagramRes = null, difficulty = "Hard",
            codeExample = """// Topological Sort (Kahn's Algorithm)
fun topologicalSort(graph: Map<Int, List<Int>>): List<Int> {
    val inDegree = mutableMapOf<Int, Int>()
    graph.keys.forEach { inDegree[it] = 0 }
    graph.forEach { (_, neighbors) ->
        neighbors.forEach { inDegree[it] = inDegree.getOrDefault(it, 0) + 1 }
    }
    val queue = ArrayDeque<Int>()
    inDegree.filter { it.value == 0 }.keys.forEach { queue.addLast(it) }
    val result = mutableListOf<Int>()
    while (queue.isNotEmpty()) {
        val u = queue.removeFirst()
        result.add(u)
        graph[u]?.forEach { v ->
            inDegree[v] = inDegree[v]!! - 1
            if (inDegree[v] == 0) queue.addLast(v)
        }
    }
    return result
}"""
        ),

        /* ────────────── HASH-BASED ────────────── */
        DataStructureEntity(
            id = "hash_table", name = "Hash Table", slug = "hash-table", category = "Hash-Based",
            explanation = "A hash table (hash map) is a data structure that maps keys to values using a hash function. The hash function computes an index into an array of buckets where the desired value is stored. Good hash functions distribute keys uniformly to minimize collisions.\n\nCollision resolution strategies:\n• Separate Chaining: Each bucket stores a linked list of entries\n• Open Addressing: Linear probing, quadratic probing, double hashing\n\nKey characteristics:\n• Average-case O(1) for all operations\n• Worst-case O(n) with poor hash function or many collisions\n• Unordered (no guaranteed iteration order)\n• Space overhead for buckets",
            complexityJson = """{"Search (avg)":"O(1)","Search (worst)":"O(n)","Insert (avg)":"O(1)","Delete (avg)":"O(1)","Space":"O(n)"}""",
            whenToUseJson = """["When you need fast key-value lookups","For implementing caches and memoization","For counting frequencies (word counts, votes)","For deduplication (checking if element exists)","For database indexing and query optimization"]""",
            diagramRes = null, difficulty = "Medium",
            codeExample = """fun main() {
    val map = mutableMapOf<String, Int>()
    map["apple"] = 5
    map["banana"] = 3
    map["orange"] = 7

    println(map["apple"])   // 5
    println(map.containsKey("grape")) // false

    for ((key, value) in map) {
        println("${'$'}key → ${'$'}value")
    }
}"""
        ),
        DataStructureEntity(
            id = "hash_set", name = "Hash Set", slug = "hash-set", category = "Hash-Based",
            explanation = "A hash set is a collection of unique elements implemented using a hash table (values only, no associated keys). It provides O(1) average-time add, remove, and contains operations. The primary purpose of a set is to efficiently track the presence or absence of elements.\n\nKey characteristics:\n• No duplicate elements allowed\n• Unordered (no guaranteed iteration order)\n• Based on hash table internally\n• Efficient union, intersection, and difference operations\n• Can usually contain null (implementation dependent)",
            complexityJson = """{"Add":"O(1)*","Remove":"O(1)*","Contains":"O(1)*","Union":"O(n + m)","Intersection":"O(min(n, m))","Space":"O(n)"}""",
            whenToUseJson = """["When you need to ensure element uniqueness","For fast membership testing (is element present?)","For removing duplicates from a collection","For mathematical set operations (union, intersection)","For tracking visited nodes in graph traversal"]""",
            diagramRes = null, difficulty = "Easy",
            codeExample = """fun main() {
    val set = mutableSetOf(1, 2, 3, 3, 4)
    println(set) // [1, 2, 3, 4] — no duplicates

    set.add(5)
    println(3 in set) // true
    println(10 in set) // false

    val setA = setOf(1, 2, 3, 4)
    val setB = setOf(3, 4, 5, 6)
    println(setA intersect setB) // [3, 4]
    println(setA union setB) // [1, 2, 3, 4, 5, 6]
}"""
        ),

        /* ────────────── OTHER ────────────── */
        DataStructureEntity(
            id = "disjoint_set", name = "Disjoint Set (Union-Find)", slug = "disjoint-set", category = "Other",
            explanation = "A Disjoint Set (also called Union-Find) is a data structure that tracks a partition of a set into disjoint (non-overlapping) subsets. It supports two operations efficiently: find (which set does an element belong to?) and union (merge two sets).\n\nOptimizations:\n• Path Compression: Flattens the tree during find operations\n• Union by Rank: Attaches smaller tree under larger tree\n\nThese optimizations make operations nearly O(1) in practice (inverse Ackermann function).",
            complexityJson = """{"Find":"O(α(n))*","Union":"O(α(n))*","Connected":"O(α(n))*","Space":"O(n)"}""",
            whenToUseJson = """["For tracking connected components in a graph","For Kruskal's MST algorithm","For detecting cycles in undirected graphs","For social network friend group analysis","For image segmentation (pixel connectivity)"]""",
            diagramRes = null, difficulty = "Medium",
            codeExample = """class UnionFind(val n: Int) {
    private val parent = IntArray(n) { it }
    private val rank = IntArray(n)

    fun find(x: Int): Int {
        if (parent[x] != x)
            parent[x] = find(parent[x]) // path compression
        return parent[x]
    }

    fun union(x: Int, y: Int) {
        val px = find(x)
        val py = find(y)
        if (px == py) return
        when {
            rank[px] < rank[py] -> parent[px] = py
            rank[px] > rank[py] -> parent[py] = px
            else -> { parent[py] = px; rank[px]++ }
        }
    }

    fun connected(x: Int, y: Int) = find(x) == find(y)
}"""
        ),
        DataStructureEntity(
            id = "segment_tree", name = "Segment Tree", slug = "segment-tree", category = "Other",
            explanation = "A segment tree is a binary tree data structure used for storing intervals or segments. It allows querying which segments contain a given point efficiently. It's particularly useful for range queries (sum, minimum, maximum, etc.) with point updates.\n\nKey characteristics:\n• 4N space for an array of size N\n• Built recursively by dividing the array into halves\n• Range query and point update: O(log n)\n• Can be extended for range updates with lazy propagation\n• Can answer various associative operations (sum, min, max, XOR, GCD)",
            complexityJson = """{"Build":"O(n)","Range Query":"O(log n)","Point Update":"O(log n)","Range Update (lazy)":"O(log n)","Space":"O(n)"}""",
            whenToUseJson = """["For range sum/min/max queries with updates","For solving competitive programming range query problems","For calculating prefix sums with modifications","For interval scheduling and overlap detection","For computational geometry problems"]""",
            diagramRes = null, difficulty = "Hard",
            codeExample = """class SegmentTree(private val arr: IntArray) {
    private val tree = IntArray(arr.size * 4)

    fun build(node: Int, start: Int, end: Int) {
        if (start == end) tree[node] = arr[start]
        else {
            val mid = (start + end) / 2
            build(node * 2, start, mid)
            build(node * 2 + 1, mid + 1, end)
            tree[node] = tree[node * 2] + tree[node * 2 + 1]
        }
    }

    fun query(node: Int, start: Int, end: Int, l: Int, r: Int): Int {
        if (r < start || end < l) return 0
        if (l <= start && end <= r) return tree[node]
        val mid = (start + end) / 2
        return query(node * 2, start, mid, l, r) +
               query(node * 2 + 1, mid + 1, end, l, r)
    }
}"""
        )
    )

    fun getQuizQuestions(): List<QuizQuestionEntity> = buildList {
        // ── Array Questions ──
        add(QuizQuestionEntity("q_array_1", "array", "What is the time complexity of accessing an element by index in an array?",
            """["O(1)","O(log n)","O(n)","O(n²)"]""", 0,
            "Array access by index is O(1) because elements are stored in contiguous memory locations, and the address is calculated directly as base + index * element_size."))
        add(QuizQuestionEntity("q_array_2", "array", "Which of the following is a key characteristic of an array?",
            """["Random access by index","Elements are stored non-contiguously","Each element has a pointer to the next","Dynamic size by default"]""", 0,
            "Arrays provide random access (O(1) by index) due to contiguous memory allocation."))
        add(QuizQuestionEntity("q_array_3", "array", "What is the time complexity of inserting an element at the beginning of an array?",
            """["O(1)","O(log n)","O(n)","O(n²)"]""", 2,
            "Inserting at the beginning requires shifting all existing elements one position to the right, which takes O(n) time."))
        add(QuizQuestionEntity("q_array_4", "array", "In most programming languages, arrays are:",
            """["Mutable by default","Immutable by default","Always fixed-size","Homogeneous (same data type)"]""", 3,
            "Arrays store elements of the same data type (homogeneous). Some languages offer dynamic arrays that can grow."))
        add(QuizQuestionEntity("q_array_5", "array", "What is the space complexity of an array of size n?",
            """["O(1)","O(log n)","O(n)","O(n²)"]""", 2,
            "An array of size n requires O(n) space to store n elements."))

        // ── Linked List Questions ──
        add(QuizQuestionEntity("q_ll_1", "linked_list", "What is the time complexity of inserting a node at the head of a singly linked list?",
            """["O(1)","O(log n)","O(n)","O(n²)"]""", 0,
            "Inserting at the head only requires updating the new node's next pointer to the current head and updating the head reference — both O(1) operations."))
        add(QuizQuestionEntity("q_ll_2", "linked_list", "A doubly linked list differs from a singly linked list in that it has:",
            """["A head pointer","A tail pointer","Both next and prev pointers","Faster search"]""", 2,
            "Doubly linked list nodes contain both 'next' and 'prev' pointers, enabling traversal in both directions."))
        add(QuizQuestionEntity("q_ll_3", "linked_list", "What is the time complexity of searching for an element in a linked list?",
            """["O(1)","O(log n)","O(n)","O(n²)"]""", 2,
            "Linked lists do not support random access. Finding an element requires traversing from the head sequentially, which takes O(n) time in the worst case."))
        add(QuizQuestionEntity("q_ll_4", "linked_list", "Which scenario is a linked list preferred over an array?",
            """["Random access is frequently needed","Frequent insertions/deletions at arbitrary positions","Memory efficiency is critical","The data size is fixed"]""", 1,
            "Linked lists excel when frequent insertions and deletions at arbitrary positions are needed, as they don't require shifting elements."))

        // ── Stack Questions ──
        add(QuizQuestionEntity("q_stack_1", "stack", "Which principle does a Stack follow?",
            """["FIFO (First-In-First-Out)","LIFO (Last-In-First-Out)","Priority-based","Round-robin"]""", 1,
            "A Stack follows LIFO — the last element added is the first one removed."))
        add(QuizQuestionEntity("q_stack_2", "stack", "Which data structure is commonly used for implementing the 'undo' feature in text editors?",
            """["Queue","Stack","Binary Tree","Hash Map"]""", 1,
            "The undo feature uses a stack. Each action is pushed onto the stack; undo pops the most recent action and reverses it."))
        add(QuizQuestionEntity("q_stack_3", "stack", "What is output of: push(1), push(2), pop(), push(3), peek()?",
            """["1","2","3","Error"]""", 2,
            "After push(1) and push(2), the stack is [1,2]. Pop removes 2. Push(3) adds 3 → [1,3]. Peek shows 3."))

        // ── Queue Questions ──
        add(QuizQuestionEntity("q_queue_1", "queue", "Which principle does a Queue follow?",
            """["LIFO (Last-In-First-Out)","FIFO (First-In-First-Out)","Priority-based","Random"]""", 1,
            "A Queue follows FIFO — the first element added is the first one removed."))
        add(QuizQuestionEntity("q_queue_2", "queue", "In BFS (Breadth-First Search), which data structure is used to track nodes to visit?",
            """["Stack","Queue","Priority Queue","Heap"]""", 1,
            "BFS uses a queue to process nodes level by level. Nodes are enqueued as discovered and dequeued for processing."))
        add(QuizQuestionEntity("q_queue_3", "queue", "What is a Deque?",
            """["A queue that only allows insertion at front","A double-ended queue allowing inserts/removes at both ends","A queue with priority ordering","A queue with limited capacity"]""", 1,
            "Deque (Double-Ended Queue) allows insertion and removal of elements from both the front and the rear."))

        // ── Binary Tree Questions ──
        add(QuizQuestionEntity("q_bt_1", "binary_tree", "What is the maximum number of children a binary tree node can have?",
            """["1","2","3","Unlimited"]""", 1,
            "A binary tree node can have at most 2 children (left and right)."))
        add(QuizQuestionEntity("q_bt_2", "binary_tree", "A node with no children is called a:",
            """["Root","Internal node","Leaf","Sibling"]""", 2,
            "A leaf node (or external node) has no children."))
        add(QuizQuestionEntity("q_bt_3", "binary_tree", "In which traversal order is the root visited first?",
            """["In-order","Pre-order","Post-order","Level-order"]""", 1,
            "Pre-order traversal visits the root first, then the left subtree, then the right subtree: Root → Left → Right."))

        // ── BST Questions ──
        add(QuizQuestionEntity("q_bst_1", "bst", "In a BST, for any node, values in the left subtree are:",
            """["Greater than the node","Less than the node","Equal to the node","Unordered"]""", 1,
            "The BST property: all values in the left subtree are less than the node's value."))
        add(QuizQuestionEntity("q_bst_2", "bst", "What traversal of a BST produces sorted order?",
            """["Pre-order","In-order","Post-order","Level-order"]""", 1,
            "In-order traversal (left → root → right) of a BST visits elements in ascending sorted order."))
        add(QuizQuestionEntity("q_bst_3", "bst", "What is the worst-case time complexity of search in a BST?",
            """["O(1)","O(log n)","O(n)","O(n²)"]""", 2,
            "In a skewed BST (all nodes in one branch), searching becomes O(n) — equivalent to a linked list."))

        // ── Heap Questions ──
        add(QuizQuestionEntity("q_heap_1", "heap", "In a Max-Heap, the root element is always the:",
            """["Smallest element","Largest element","Median element","Most recently added"]""", 1,
            "In a Max-Heap, the root is the largest element since every parent is ≥ its children."))
        add(QuizQuestionEntity("q_heap_2", "heap", "What is the time complexity to extract the min/max from a heap?",
            """["O(1)","O(log n)","O(n)","O(n log n)"]""", 1,
            "Extracting the min/max requires removing the root and heapifying, which takes O(log n)."))
        add(QuizQuestionEntity("q_heap_3", "heap", "A heap is a complete binary tree. This means:",
            """["All levels are completely filled","All levels except possibly the last are filled, left to right","Every node has exactly 2 children","The tree is balanced like an AVL"]""", 1,
            "A complete binary tree fills all levels from left to right; the last level may be partially filled but must fill from the left."))

        // ── Trie Questions ──
        add(QuizQuestionEntity("q_trie_1", "trie", "What is the primary use case for a Trie?",
            """["Sorting numbers","Prefix-based string search","Graph traversal","Image processing"]""", 1,
            "Tries are optimized for prefix-based string operations like autocomplete and spell checking."))
        add(QuizQuestionEntity("q_trie_2", "trie", "What is the time complexity of searching for a word of length m in a Trie?",
            """["O(1)","O(log m)","O(m)","O(m × alphabet_size)"]""", 2,
            "Searching a word of length m in a Trie requires traversing m characters, each requiring O(1) child lookup, so O(m) total."))

        // ── Graph Questions ──
        add(QuizQuestionEntity("q_graph_1", "graph", "Which graph traversal uses a queue?",
            """["DFS","BFS","Dijkstra's","Prim's"]""", 1,
            "BFS (Breadth-First Search) uses a queue to process vertices level by level."))
        add(QuizQuestionEntity("q_graph_2", "graph", "What is the space complexity of an adjacency matrix for a graph with V vertices?",
            """["O(V)","O(V + E)","O(V²)","O(E²)"]""", 2,
            "An adjacency matrix uses O(V²) space regardless of the number of edges."))
        add(QuizQuestionEntity("q_graph_3", "graph", "In an undirected graph, if there is an edge between u and v, then:",
            """["u is the parent of v","v is the parent of u","The edge is bidirectional","The edge has a direction from u to v"]""", 2,
            "In an undirected graph, edges are bidirectional — they connect vertices in both directions."))

        // ── Hash Table Questions ──
        add(QuizQuestionEntity("q_hash_1", "hash_table", "What is the average-case time complexity for search in a hash table?",
            """["O(1)","O(log n)","O(n)","O(n²)"]""", 0,
            "With a good hash function and proper load factor, hash tables provide O(1) average-case search."))
        add(QuizQuestionEntity("q_hash_2", "hash_table", "What happens when two keys hash to the same index?",
            """["The later key overwrites the earlier","It's called a collision","The hash table throws an error","The keys are merged"]""", 1,
            "When two keys hash to the same index, it's called a collision. Common resolution methods include chaining and open addressing."))
        add(QuizQuestionEntity("q_hash_3", "hash_table", "What is the load factor of a hash table?",
            """["The number of buckets","The ratio of filled buckets to total buckets","The hash function output size","The size of each key"]""", 1,
            "The load factor = number of entries / number of buckets. A higher load factor increases collision probability."))

        // ── Disjoint Set Questions ──
        add(QuizQuestionEntity("q_dsu_1", "disjoint_set", "What are the two primary operations of a Union-Find data structure?",
            """["Push and Pop","Add and Remove","Find and Union","Insert and Delete"]""", 2,
            "The two primary operations are Find (which set does an element belong to?) and Union (merge two sets)."))
        add(QuizQuestionEntity("q_dsu_2", "disjoint_set", "Which algorithm uses Union-Find to find MST?",
            """["Dijkstra's","Bellman-Ford","Kruskal's","Floyd-Warshall"]""", 2,
            "Kruskal's MST algorithm sorts edges by weight and uses Union-Find to detect cycles while adding edges."))

        // ── Segment Tree Questions ──
        add(QuizQuestionEntity("q_seg_1", "segment_tree", "What is the time complexity of a range query in a segment tree?",
            """["O(1)","O(log n)","O(n)","O(n log n)"]""", 1,
            "Segment tree range queries take O(log n) time by recursively combining results from relevant nodes."))
        add(QuizQuestionEntity("q_seg_2", "segment_tree", "Approximately how much space does a segment tree need for an array of size n?",
            """["O(n)","O(2n)","O(4n)","O(n²)"]""", 2,
            "A segment tree typically requires about 4n space to safely accommodate all nodes."))
    }

    fun getQuizQuestionsList(): List<QuizQuestionEntity> = getQuizQuestions()

    fun getDefaultProfile(): UserProfileEntity = UserProfileEntity()
}
