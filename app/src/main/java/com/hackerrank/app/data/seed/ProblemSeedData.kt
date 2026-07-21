package com.hackerrank.app.data.seed

import com.hackerrank.app.data.local.entity.ProblemEntity

object ProblemSeedData {
    fun getProblems(): List<ProblemEntity> =
        listOf(
            // ═══════════════ ARRAYS (12) ═══════════════
            ProblemEntity(
                "p001", "Two Sum",
                "Given an array of integers `nums` and an integer `target`, return indices of the two numbers that add up to target. You may assume each input has exactly one solution, and you may not use the same element twice.",
                "nums = [2, 7, 11, 15], target = 9", "[0, 1]",
                """fun twoSum(nums: IntArray, target: Int): IntArray {
    val map = mutableMapOf<Int, Int>()
    for ((i, num) in nums.withIndex()) {
        val complement = target - num
        if (map.containsKey(complement))
            return intArrayOf(map[complement]!!, i)
        map[num] = i
    }
    return intArrayOf()
}""",
                "Use a hash map to store each number's index as we iterate. For each element, check if its complement (target - num) already exists in the map. If yes, we found the pair. This gives O(n) time and O(n) space complexity.",
                "Easy", "Arrays", 1,
            ),
            ProblemEntity(
                "p002", "Maximum Subarray",
                "Given an integer array `nums`, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.",
                "nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]", "6",
                """fun maxSubArray(nums: IntArray): Int {
    var maxSum = nums[0]
    var currentSum = nums[0]
    for (i in 1 until nums.size) {
        currentSum = maxOf(nums[i], currentSum + nums[i])
        maxSum = maxOf(maxSum, currentSum)
    }
    return maxSum
}""",
                "Kadane's algorithm: iterate through the array, keeping a running sum. At each position, decide whether to start fresh at the current element or add to the existing subarray. Track the maximum seen. O(n) time, O(1) space.",
                "Medium", "Arrays", 2,
            ),
            ProblemEntity(
                "p003", "Rotate Array",
                "Given an integer array `nums`, rotate the array to the right by `k` steps, where k is non-negative.",
                "nums = [1, 2, 3, 4, 5, 6, 7], k = 3", "[5, 6, 7, 1, 2, 3, 4]",
                """fun rotate(nums: IntArray, k: Int) {
    val n = nums.size
    val shift = k % n
    nums.reverse()
    nums.reverse(0, shift)
    nums.reverse(shift, n)
}""",
                "Reverse the entire array, then reverse the first k elements, then reverse the rest. This three-reversal technique achieves O(n) time and O(1) extra space.",
                "Medium", "Arrays", 3,
            ),
            ProblemEntity(
                "p004", "Find All Duplicates in an Array",
                "Given an integer array `nums` of length n where all integers are in the range [1, n], find all elements that appear twice.",
                "nums = [4, 3, 2, 7, 8, 2, 3, 1]", "[2, 3]",
                """fun findDuplicates(nums: IntArray): List<Int> {
    val result = mutableListOf<Int>()
    for (num in nums) {
        val idx = kotlin.math.abs(num) - 1
        if (nums[idx] < 0) result.add(kotlin.math.abs(num))
        else nums[idx] = -nums[idx]
    }
    return result
}""",
                "Use the array itself as a hash map. Since values are in range [1,n], use each value as an index and negate the element at that index to mark it as seen. If already negative, it's a duplicate. O(n) time, O(1) space.",
                "Medium", "Arrays", 4,
            ),
            ProblemEntity(
                "p005", "Product of Array Except Self",
                "Given an integer array `nums`, return an array `answer` such that answer[i] is the product of all elements except nums[i]. Solve without division in O(n) time.",
                "nums = [1, 2, 3, 4]", "[24, 12, 8, 6]",
                """fun productExceptSelf(nums: IntArray): IntArray {
    val n = nums.size
    val result = IntArray(n) { 1 }
    var prefix = 1
    for (i in nums.indices) {
        result[i] = prefix
        prefix *= nums[i]
    }
    var suffix = 1
    for (i in nums.indices.reversed()) {
        result[i] *= suffix
        suffix *= nums[i]
    }
    return result
}""",
                "Compute prefix products from left to right, storing in result. Then compute suffix products from right to left, multiplying into result. Each position gets product of all elements left × all elements right. O(n) time, O(1) extra space.",
                "Medium", "Arrays", 5,
            ),
            ProblemEntity(
                "p006", "Merge Sorted Array",
                "Given two sorted integer arrays `nums1` and `nums2`, merge nums2 into nums1 as one sorted array. nums1 has enough space (size m+n) to hold additional elements from nums2.",
                "nums1 = [1, 2, 3, 0, 0, 0], m = 3, nums2 = [2, 5, 6], n = 3", "[1, 2, 2, 3, 5, 6]",
                """fun merge(nums1: IntArray, m: Int, nums2: IntArray, n: Int) {
    var p1 = m - 1
    var p2 = n - 1
    var p = m + n - 1
    while (p2 >= 0) {
        if (p1 >= 0 && nums1[p1] > nums2[p2])
            nums1[p--] = nums1[p1--]
        else
            nums1[p--] = nums2[p2--]
    }
}""",
                "Merge from the end of both arrays. Compare elements and place the larger one at the end of nums1. This avoids shifting elements and works in-place in O(m+n) time.",
                "Easy", "Arrays", 6,
            ),
            ProblemEntity(
                "p007", "Remove Duplicates from Sorted Array",
                "Given a sorted integer array `nums`, remove duplicates in-place so each element appears only once. Return the number of unique elements.",
                "nums = [1, 1, 2]", "2",
                """fun removeDuplicates(nums: IntArray): Int {
    if (nums.isEmpty()) return 0
    var k = 1
    for (i in 1 until nums.size) {
        if (nums[i] != nums[i - 1])
            nums[k++] = nums[i]
    }
    return k
}""",
                "Use two pointers: one (k) tracks the position to write the next unique element, and the other (i) scans the array. When a new value is found, write it at position k and increment. O(n) time, O(1) space.",
                "Easy", "Arrays", 7,
            ),
            ProblemEntity(
                "p008", "Move Zeroes",
                "Given an integer array `nums`, move all zeroes to the end while maintaining the relative order of non-zero elements. Do this in-place.",
                "nums = [0, 1, 0, 3, 12]", "[1, 3, 12, 0, 0]",
                """fun moveZeroes(nums: IntArray) {
    var pos = 0
    for (i in nums.indices) {
        if (nums[i] != 0)
            nums[pos++] = nums[i]
    }
    while (pos < nums.size)
        nums[pos++] = 0
}""",
                "Shift all non-zero elements to the front using a position pointer, then fill the remaining positions with zeroes. O(n) time, O(1) space.",
                "Easy", "Arrays", 8,
            ),
            ProblemEntity(
                "p009", "Best Time to Buy and Sell Stock",
                "Given an array `prices` where prices[i] is the price on day i, find the maximum profit from buying and selling one stock (buy before sell).",
                "prices = [7, 1, 5, 3, 6, 4]", "5",
                """fun maxProfit(prices: IntArray): Int {
    var minPrice = Int.MAX_VALUE
    var maxProfit = 0
    for (price in prices) {
        if (price < minPrice) minPrice = price
        else maxProfit = maxOf(maxProfit, price - minPrice)
    }
    return maxProfit
}""",
                "Track the minimum price seen so far. For each day, calculate profit if sold today and update max profit accordingly. O(n) time, O(1) space.",
                "Easy", "Arrays", 9,
            ),
            ProblemEntity(
                "p010", "Contains Duplicate",
                "Given an integer array `nums`, return true if any value appears at least twice, false if all are distinct.",
                "nums = [1, 2, 3, 1]", "true",
                """fun containsDuplicate(nums: IntArray): Boolean {
    val seen = mutableSetOf<Int>()
    for (num in nums) {
        if (num in seen) return true
        seen.add(num)
    }
    return false
}""",
                "Use a hash set to track seen elements. If an element is already in the set, return true. O(n) time, O(n) space.",
                "Easy", "Arrays", 10,
            ),
            ProblemEntity(
                "p011", "Find Minimum in Rotated Sorted Array",
                "Given a rotated sorted array of unique elements, find the minimum element in O(log n) time.",
                "nums = [3, 4, 5, 1, 2]", "1",
                """fun findMin(nums: IntArray): Int {
    var left = 0
    var right = nums.lastIndex
    while (left < right) {
        val mid = left + (right - left) / 2
        if (nums[mid] > nums[right]) left = mid + 1
        else right = mid
    }
    return nums[left]
}""",
                "Modified binary search. Compare the middle element with the right element — if mid > right, the pivot (minimum) is in the right half; otherwise it's in the left half. O(log n) time, O(1) space.",
                "Medium", "Arrays", 11,
            ),
            ProblemEntity(
                "p012", "Majority Element",
                "Given an array of size n, find the element that appears more than ⌊n/2⌋ times. Assume the array is non-empty and a majority always exists.",
                "nums = [3, 2, 3]", "3",
                """fun majorityElement(nums: IntArray): Int {
    var count = 0
    var candidate = 0
    for (num in nums) {
        if (count == 0) candidate = num
        count += if (num == candidate) 1 else -1
    }
    return candidate
}""",
                "Boyer-Moore voting algorithm: cancel each pair of different elements. The majority element will survive the cancellation. O(n) time, O(1) space.",
                "Easy", "Arrays", 12,
            ),
            // ═══════════════ STRINGS (12) ═══════════════
            ProblemEntity(
                "p013", "Reverse String",
                "Write a function that reverses a string in-place. The input is given as a `CharArray`.",
                "s = [\"h\",\"e\",\"l\",\"l\",\"o\"]", "[\"o\",\"l\",\"l\",\"e\",\"h\"]",
                """fun reverseString(s: CharArray) {
    var left = 0
    var right = s.lastIndex
    while (left < right) {
        val temp = s[left]
        s[left] = s[right]
        s[right] = temp
        left++; right--
    }
}""",
                "Two-pointer approach: swap the leftmost and rightmost characters, then move inward. O(n) time, O(1) space.",
                "Easy", "Strings", 13,
            ),
            ProblemEntity(
                "p014", "Valid Anagram",
                "Given two strings `s` and `t`, return true if t is an anagram of s, false otherwise.",
                "s = \"anagram\", t = \"nagaram\"", "true",
                """fun isAnagram(s: String, t: String): Boolean {
    if (s.length != t.length) return false
    val counts = IntArray(26)
    for (i in s.indices) {
        counts[s[i] - 'a']++
        counts[t[i] - 'a']--
    }
    return counts.all { it == 0 }
}""",
                "Count character frequencies using an array of size 26. Increment for s, decrement for t. If all counts are zero, they're anagrams. O(n) time, O(1) space.",
                "Easy", "Strings", 14,
            ),
            ProblemEntity(
                "p015", "Valid Palindrome",
                "Given a string `s`, determine if it's a palindrome considering only alphanumeric characters and ignoring case.",
                "s = \"A man, a plan, a canal: Panama\"", "true",
                """fun isPalindrome(s: String): Boolean {
    var left = 0
    var right = s.lastIndex
    while (left < right) {
        if (!s[left].isLetterOrDigit()) { left++; continue }
        if (!s[right].isLetterOrDigit()) { right--; continue }
        if (s[left].lowercase() != s[right].lowercase()) return false
        left++; right--
    }
    return true
}""",
                "Two-pointer approach: skip non-alphanumeric characters, then compare characters from both ends case-insensitively. O(n) time, O(1) space.",
                "Easy", "Strings", 15,
            ),
            ProblemEntity(
                "p016", "Longest Substring Without Repeating Characters",
                "Given a string `s`, find the length of the longest substring without repeating characters.",
                "s = \"abcabcbb\"", "3",
                """fun lengthOfLongestSubstring(s: String): Int {
    val map = mutableMapOf<Char, Int>()
    var maxLen = 0
    var left = 0
    for (right in s.indices) {
        if (s[right] in map)
            left = maxOf(left, map[s[right]]!! + 1)
        map[s[right]] = right
        maxLen = maxOf(maxLen, right - left + 1)
    }
    return maxLen
}""",
                "Sliding window with a hash map tracking the last index of each character. When a repeat is found, move the left pointer past the previous occurrence. O(n) time, O(k) space where k is the character set size.",
                "Medium", "Strings", 16,
            ),
            ProblemEntity(
                "p017", "Group Anagrams",
                "Given an array of strings `strs`, group the anagrams together. Return the groups in any order.",
                "strs = [\"eat\",\"tea\",\"tan\",\"ate\",\"nat\",\"bat\"]", "[[\"bat\"],[\"nat\",\"tan\"],[\"ate\",\"eat\",\"tea\"]]",
                """fun groupAnagrams(strs: Array<String>): List<List<String>> {
    val map = mutableMapOf<String, MutableList<String>>()
    for (s in strs) {
        val key = s.toCharArray().sorted().joinToString("")
        map.getOrPut(key) { mutableListOf() }.add(s)
    }
    return map.values.toList()
}""",
                "Sort each string to create a canonical key. All anagrams share the same sorted key. Group strings by their key using a hash map. O(n·k log k) time where k is the max string length.",
                "Medium", "Strings", 17,
            ),
            ProblemEntity(
                "p018", "Longest Palindromic Substring",
                "Given a string `s`, return the longest palindromic substring in s.",
                "s = \"babad\"", "\"bab\"",
                """fun longestPalindrome(s: String): String {
    var start = 0; var end = 0
    fun expand(l: Int, r: Int) {
        var left = l; var right = r
        while (left >= 0 && right < s.length && s[left] == s[right]) {
            if (right - left > end - start) { start = left; end = right }
            left--; right++
        }
    }
    for (i in s.indices) {
        expand(i, i)
        expand(i, i + 1)
    }
    return s.substring(start, end + 1)
}""",
                "Expand outward from each possible center (both single character and between characters). Track the longest palindrome found. O(n²) time, O(1) space.",
                "Medium", "Strings", 18,
            ),
            ProblemEntity(
                "p019", "String Compression",
                "Compress a string by replacing consecutive repeated characters with the character followed by the count. Only append count if > 1. Return the new length of the compressed char array.",
                "chars = [\"a\",\"a\",\"b\",\"b\",\"c\",\"c\",\"c\"]", "6",
                """fun compress(chars: CharArray): Int {
    var write = 0; var i = 0
    while (i < chars.size) {
        var j = i
        while (j < chars.size && chars[j] == chars[i]) j++
        chars[write++] = chars[i]
        if (j - i > 1) {
            for (c in (j - i).toString()) chars[write++] = c
        }
        i = j
    }
    return write
}""",
                "Two-pointer approach: read pointer (i) scans for groups, write pointer places compressed output. Count consecutive runs and write character + count. O(n) time, O(1) space.",
                "Medium", "Strings", 19,
            ),
            ProblemEntity(
                "p020", "Implement strStr()",
                "Return the index of the first occurrence of `needle` in `haystack`, or -1 if not present.",
                "haystack = \"sadbutsad\", needle = \"sad\"", "0",
                """fun strStr(haystack: String, needle: String): Int {
    if (needle.isEmpty()) return 0
    val n = haystack.length; val m = needle.length
    for (i in 0..n - m) {
        if (haystack.substring(i, i + m) == needle) return i
    }
    return -1
}""",
                "Slide a window of length m across haystack and compare substrings. O(n·m) time in worst case, but simple and practical for typical inputs. For production, KMP achieves O(n+m).",
                "Easy", "Strings", 20,
            ),
            ProblemEntity(
                "p021", "First Unique Character in a String",
                "Given a string `s`, find the first non-repeating character and return its index. If none exists, return -1.",
                "s = \"leetcode\"", "0",
                """fun firstUniqChar(s: String): Int {
    val counts = IntArray(26)
    for (c in s) counts[c - 'a']++
    for ((i, c) in s.withIndex()) {
        if (counts[c - 'a'] == 1) return i
    }
    return -1
}""",
                "First pass counts character frequencies. Second pass finds the first character with count 1. O(n) time, O(1) space.",
                "Easy", "Strings", 21,
            ),
            ProblemEntity(
                "p022", "Reverse Words in a String",
                "Given a string `s`, reverse the order of words. A word is a sequence of non-space characters. Remove extra spaces.",
                "s = \"the sky is blue\"", "\"blue is sky the\"",
                """fun reverseWords(s: String): String {
    return s.trim().split("\\s+".toRegex())
        .reversed().joinToString(" ")
}""",
                "Trim the string, split on whitespace, reverse the list of words, and join with single spaces. O(n) time, O(n) space.",
                "Medium", "Strings", 22,
            ),
            ProblemEntity(
                "p023", "Longest Common Prefix",
                "Find the longest common prefix string amongst an array of strings. Return empty string if none.",
                "strs = [\"flower\",\"flow\",\"flight\"]", "\"fl\"",
                """fun longestCommonPrefix(strs: Array<String>): String {
    if (strs.isEmpty()) return ""
    var prefix = strs[0]
    for (i in 1 until strs.size) {
        while (strs[i].indexOf(prefix) != 0)
            prefix = prefix.substring(0, prefix.length - 1)
        if (prefix.isEmpty()) return ""
    }
    return prefix
}""",
                "Start with the first string as the prefix. For each subsequent string, shorten the prefix until it matches the start. O(S) time where S is the sum of all characters.",
                "Easy", "Strings", 23,
            ),
            ProblemEntity(
                "p024", "Count and Say",
                "The count-and-say sequence is a sequence of strings. Each term describes the previous term: count consecutive digits and say them. Return the nth term. 1 <= n <= 30.",
                "n = 4", "\"1211\"",
                """fun countAndSay(n: Int): String {
    var result = "1"
    for (i in 2..n) {
        val sb = StringBuilder()
        var j = 0
        while (j < result.length) {
            var k = j
            while (k < result.length && result[k] == result[j]) k++
            sb.append(k - j).append(result[j])
            j = k
        }
        result = sb.toString()
    }
    return result
}""",
                "Build the sequence iteratively. For each term, group consecutive identical digits, append count then digit. Start with '1' and repeat n-1 times. O(2^n) time due to sequence growth.",
                "Medium", "Strings", 24,
            ),
            // ═══════════════ LINKED LISTS (8) ═══════════════
            ProblemEntity(
                "p025", "Reverse Linked List",
                "Reverse a singly linked list and return the new head.",
                "head = [1, 2, 3, 4, 5]", "[5, 4, 3, 2, 1]",
                """fun reverseList(head: ListNode?): ListNode? {
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
                "Iterative reversal: maintain prev, current, and next pointers. Reverse each node's next pointer to point to the previous node. O(n) time, O(1) space.",
                "Easy", "Linked Lists", 25,
            ),
            ProblemEntity(
                "p026", "Linked List Cycle Detection",
                "Given the head of a linked list, determine if it has a cycle.",
                "head = [3, 2, 0, -4], pos = 1", "true",
                """fun hasCycle(head: ListNode?): Boolean {
    var slow = head
    var fast = head
    while (fast?.next != null) {
        slow = slow?.next
        fast = fast.next?.next
        if (slow == fast) return true
    }
    return false
}""",
                "Floyd's cycle detection (tortoise and hare). Use two pointers moving at different speeds. If they meet, a cycle exists. O(n) time, O(1) space.",
                "Easy", "Linked Lists", 26,
            ),
            ProblemEntity(
                "p027", "Merge Two Sorted Lists",
                "Merge two sorted linked lists into one sorted list.",
                "list1 = [1, 2, 4], list2 = [1, 3, 4]", "[1, 1, 2, 3, 4, 4]",
                """fun mergeTwoLists(l1: ListNode?, l2: ListNode?): ListNode? {
    val dummy = ListNode(0)
    var curr = dummy
    var p1 = l1; var p2 = l2
    while (p1 != null && p2 != null) {
        if (p1.value < p2.value) {
            curr.next = p1; p1 = p1.next
        } else {
            curr.next = p2; p2 = p2.next
        }
        curr = curr.next!!
    }
    curr.next = p1 ?: p2
    return dummy.next
}""",
                "Use a dummy head node to simplify. Compare heads of both lists, attach the smaller, and advance. Attach remaining nodes when one list is exhausted. O(n+m) time, O(1) space.",
                "Easy", "Linked Lists", 27,
            ),
            ProblemEntity(
                "p028", "Remove Nth Node From End",
                "Given the head of a linked list, remove the nth node from the end and return the head.",
                "head = [1, 2, 3, 4, 5], n = 2", "[1, 2, 3, 5]",
                """fun removeNthFromEnd(head: ListNode?, n: Int): ListNode? {
    val dummy = ListNode(0)
    dummy.next = head
    var fast = dummy
    var slow = dummy
    for (i in 0..n) fast = fast.next!!
    while (fast != null) {
        slow = slow.next!!
        fast = fast.next!!
    }
    slow.next = slow.next?.next
    return dummy.next
}""",
                "Use a dummy node and two pointers. Advance fast n+1 steps ahead, then move both until fast reaches the end. Slow will be at the node before the target. O(n) time, O(1) space.",
                "Medium", "Linked Lists", 28,
            ),
            ProblemEntity(
                "p029", "Middle of the Linked List",
                "Return the middle node of a linked list. If two middle nodes exist, return the second one.",
                "head = [1, 2, 3, 4, 5]", "3",
                """fun middleNode(head: ListNode?): ListNode? {
    var slow = head
    var fast = head
    while (fast?.next != null) {
        slow = slow?.next
        fast = fast.next?.next
    }
    return slow
}""",
                "Two-pointer technique: slow moves one step, fast moves two steps. When fast reaches the end, slow is at the middle. O(n) time, O(1) space.",
                "Easy", "Linked Lists", 29,
            ),
            ProblemEntity(
                "p030", "Palindrome Linked List",
                "Given the head of a singly linked list, return true if it's a palindrome.",
                "head = [1, 2, 2, 1]", "true",
                """fun isPalindrome(head: ListNode?): Boolean {
    var slow = head; var fast = head
    while (fast?.next != null) {
        slow = slow?.next; fast = fast.next?.next
    }
    var prev: ListNode? = null
    var curr = slow
    while (curr != null) {
        val next = curr.next; curr.next = prev; prev = curr; curr = next
    }
    var left = head; var right = prev
    while (right != null) {
        if (left?.value != right.value) return false
        left = left.next; right = right.next
    }
    return true
}""",
                "Find the middle, reverse the second half, then compare both halves element by element. O(n) time, O(1) space.",
                "Easy", "Linked Lists", 30,
            ),
            ProblemEntity(
                "p031", "Intersection of Two Linked Lists",
                "Given two singly linked lists, return the node where they intersect. If they don't intersect, return null.",
                "intersectVal = 8, listA = [4, 1, 8, 4, 5], listB = [5, 6, 1, 8, 4, 5]", "8",
                """fun getIntersectionNode(headA: ListNode?, headB: ListNode?): ListNode? {
    var a = headA; var b = headB
    while (a != b) {
        a = if (a == null) headB else a.next
        b = if (b == null) headA else b.next
    }
    return a
}""",
                "Two-pointer technique: each pointer traverses both lists. When one reaches the end, redirect to the other list's head. They'll meet at the intersection after at most 2 passes. O(n+m) time, O(1) space.",
                "Easy", "Linked Lists", 31,
            ),
            ProblemEntity(
                "p032", "Remove Duplicates from Sorted List",
                "Given a sorted linked list, remove all duplicates so each element appears only once.",
                "head = [1, 1, 2]", "[1, 2]",
                """fun deleteDuplicates(head: ListNode?): ListNode? {
    var curr = head
    while (curr?.next != null) {
        if (curr.value == curr.next?.value)
            curr.next = curr.next?.next
        else
            curr = curr.next
    }
    return head
}""",
                "Since the list is sorted, duplicates are adjacent. Compare each node with its next; if equal, skip the next node. O(n) time, O(1) space.",
                "Easy", "Linked Lists", 32,
            ),
            // ═══════════════ STACKS & QUEUES (8) ═══════════════
            ProblemEntity(
                "p033", "Valid Parentheses",
                "Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if it's valid.",
                "s = \"()[]{}\"", "true",
                """fun isValid(s: String): Boolean {
    val stack = ArrayDeque<Char>()
    for (c in s) {
        when (c) {
            '(' -> stack.addLast(')')
            '{' -> stack.addLast('}')
            '[' -> stack.addLast(']')
            else -> if (stack.isEmpty() || stack.removeLast() != c) return false
        }
    }
    return stack.isEmpty()
}""",
                "Use a stack: push closing brackets for opening ones. When a closing bracket is seen, pop and compare. O(n) time, O(n) space.",
                "Easy", "Stacks & Queues", 33,
            ),
            ProblemEntity(
                "p034", "Min Stack",
                "Design a stack that supports push, pop, top, and retrieving the minimum element in O(1) time.",
                "[\"MinStack\",\"push\",\"push\",\"push\",\"getMin\",\"pop\",\"top\",\"getMin\"]\\n[[],[-2],[0],[-3],[],[],[],[]]", "[null,null,null,null,-3,null,0,-2]",
                """class MinStack() {
    private val stack = ArrayDeque<Int>()
    private val minStack = ArrayDeque<Int>()

    fun push(`val`: Int) {
        stack.addLast(`val`)
        if (minStack.isEmpty() || `val` <= minStack.last())
            minStack.addLast(`val`)
    }
    fun pop() {
        if (stack.removeLast() == minStack.last()) minStack.removeLast()
    }
    fun top() = stack.last()
    fun getMin() = minStack.last()
}""",
                "Maintain a second stack that tracks the minimum. When pushing, if the value is ≤ current min, also push to minStack. When popping, if the popped value equals minStack's top, pop from minStack too.",
                "Medium", "Stacks & Queues", 34,
            ),
            ProblemEntity(
                "p035", "Implement Queue using Stacks",
                "Implement a FIFO queue using only two stacks.",
                "[\"MyQueue\",\"push\",\"push\",\"peek\",\"pop\",\"empty\"]\\n[[],[1],[2],[],[],[]]", "[null,null,null,1,1,false]",
                """class MyQueue() {
    private val s1 = ArrayDeque<Int>()
    private val s2 = ArrayDeque<Int>()

    fun push(x: Int) { s1.addLast(x) }
    fun pop(): Int {
        if (s2.isEmpty()) while (s1.isNotEmpty()) s2.addLast(s1.removeLast())
        return s2.removeLast()
    }
    fun peek(): Int {
        if (s2.isEmpty()) while (s1.isNotEmpty()) s2.addLast(s1.removeLast())
        return s2.last()
    }
    fun empty() = s1.isEmpty() && s2.isEmpty()
}""",
                "Use two stacks: s1 for pushes, s2 for pops/peeks. When s2 is empty, transfer all elements from s1 to s2 (reversing order). Each element is moved at most twice, giving amortized O(1) operations.",
                "Easy", "Stacks & Queues", 35,
            ),
            ProblemEntity(
                "p036", "Evaluate Reverse Polish Notation",
                "Evaluate an expression in Reverse Polish Notation (postfix). Valid operators are +, -, *, /. Division truncates toward zero.",
                "tokens = [\"2\",\"1\",\"+\",\"3\",\"*\"]", "9",
                """fun evalRPN(tokens: Array<String>): Int {
    val stack = ArrayDeque<Int>()
    for (t in tokens) {
        when (t) {
            "+" -> stack.addLast(stack.removeLast() + stack.removeLast())
            "-" -> { val b = stack.removeLast(); val a = stack.removeLast(); stack.addLast(a - b) }
            "*" -> stack.addLast(stack.removeLast() * stack.removeLast())
            "/" -> { val b = stack.removeLast(); val a = stack.removeLast(); stack.addLast(a / b) }
            else -> stack.addLast(t.toInt())
        }
    }
    return stack.last()
}""",
                "Process tokens left to right. Push numbers onto stack. When encountering an operator, pop two operands, apply the operator, and push the result. O(n) time, O(n) space.",
                "Medium", "Stacks & Queues", 36,
            ),
            ProblemEntity(
                "p037", "Daily Temperatures",
                "Given an array of temperatures, return an array where answer[i] is the number of days until a warmer temperature. If no warmer day, answer[i] = 0.",
                "temperatures = [73, 74, 75, 71, 69, 72, 76, 73]", "[1, 1, 4, 2, 1, 1, 0, 0]",
                """fun dailyTemperatures(temperatures: IntArray): IntArray {
    val result = IntArray(temperatures.size)
    val stack = ArrayDeque<Int>()
    for ((i, t) in temperatures.withIndex()) {
        while (stack.isNotEmpty() && t > temperatures[stack.last()]) {
            val idx = stack.removeLast()
            result[idx] = i - idx
        }
        stack.addLast(i)
    }
    return result
}""",
                "Use a monotonic decreasing stack that stores indices. For each temperature, pop indices with lower temperature and calculate the day difference. O(n) time, O(n) space.",
                "Medium", "Stacks & Queues", 37,
            ),
            ProblemEntity(
                "p038", "Implement Stack using Queues",
                "Implement a LIFO stack using only two queues.",
                "[\"MyStack\",\"push\",\"push\",\"top\",\"pop\",\"empty\"]\\n[[],[1],[2],[],[],[]]", "[null,null,null,2,2,false]",
                """class MyStack() {
    private val q1 = ArrayDeque<Int>()
    private val q2 = ArrayDeque<Int>()

    fun push(x: Int) {
        q2.addLast(x)
        while (q1.isNotEmpty()) q2.addLast(q1.removeFirst())
        val temp = q1; q1 = q2; q2 = temp
    }
    fun pop() = q1.removeFirst()
    fun top() = q1.first()
    fun empty() = q1.isEmpty()
}""",
                "When pushing, add to q2, then move all elements from q1 to q2, then swap the queues. The most recent element is always at the front of q1. O(n) push, O(1) pop.",
                "Easy", "Stacks & Queues", 38,
            ),
            ProblemEntity(
                "p039", "Next Greater Element",
                "Given an array, find the next greater element for each element. The next greater element is the first greater element to its right. Return -1 if none.",
                "nums1 = [4, 1, 2], nums2 = [1, 3, 4, 2]", "[-1, 3, -1]",
                """fun nextGreaterElement(nums: IntArray): IntArray {
    val result = IntArray(nums.size) { -1 }
    val stack = ArrayDeque<Int>()
    for ((i, num) in nums.withIndex()) {
        while (stack.isNotEmpty() && num > nums[stack.last()])
            result[stack.removeLast()] = num
        stack.addLast(i)
    }
    return result
}""",
                "Use a monotonic decreasing stack of indices. For each element, pop indices with smaller values and set their next greater element. O(n) time, O(n) space.",
                "Medium", "Stacks & Queues", 39,
            ),
            ProblemEntity(
                "p040", "Backspace String Compare",
                "Given two strings s and t, return true if they are equal when typed into empty text editors. '#' represents a backspace character.",
                "s = \"ab#c\", t = \"ad#c\"", "true",
                """fun backspaceCompare(s: String, t: String): Boolean {
    fun process(str: String): String {
        val stack = ArrayDeque<Char>()
        for (c in str) {
            if (c == '#') stack.removeLastOrNull()
            else stack.addLast(c)
        }
        return stack.joinToString("")
    }
    return process(s) == process(t)
}""",
                "Process each string using a stack: push characters, pop on backspace. Compare the resulting strings. O(n+m) time, O(n+m) space.",
                "Easy", "Stacks & Queues", 40,
            ),
            // ═══════════════ TREES (10) ═══════════════
            ProblemEntity(
                "p041", "Maximum Depth of Binary Tree",
                "Given the root of a binary tree, find its maximum depth (number of nodes along the longest root-to-leaf path).",
                "root = [3, 9, 20, null, null, 15, 7]", "3",
                """fun maxDepth(root: TreeNode?): Int {
    if (root == null) return 0
    return 1 + maxOf(maxDepth(root.left), maxDepth(root.right))
}""",
                "Recursive approach: depth = 1 + max(depth of left subtree, depth of right subtree). Base case: null node has depth 0. O(n) time, O(h) space where h is height.",
                "Easy", "Trees", 41,
            ),
            ProblemEntity(
                "p042", "Validate Binary Search Tree",
                "Given the root of a binary tree, determine if it's a valid BST (all left values < node, all right values > node).",
                "root = [2, 1, 3]", "true",
                """fun isValidBST(root: TreeNode?): Boolean {
    fun validate(node: TreeNode?, min: Long, max: Long): Boolean {
        if (node == null) return true
        if (node.value <= min || node.value >= max) return false
        return validate(node.left, min, node.value.toLong()) &&
               validate(node.right, node.value.toLong(), max)
    }
    return validate(root, Long.MIN_VALUE, Long.MAX_VALUE)
}""",
                "Recursive validation with min/max bounds. For each node, check it's within the valid range, then recursively validate children with updated bounds. O(n) time, O(h) space.",
                "Medium", "Trees", 42,
            ),
            ProblemEntity(
                "p043", "Binary Tree Inorder Traversal",
                "Given the root of a binary tree, return the inorder traversal as a list.",
                "root = [1, null, 2, 3]", "[1, 3, 2]",
                """fun inorderTraversal(root: TreeNode?): List<Int> {
    val result = mutableListOf<Int>()
    val stack = ArrayDeque<TreeNode>()
    var curr = root
    while (curr != null || stack.isNotEmpty()) {
        while (curr != null) { stack.addLast(curr); curr = curr.left }
        curr = stack.removeLast()
        result.add(curr.value)
        curr = curr.right
    }
    return result
}""",
                "Iterative inorder traversal: go left as far as possible, push nodes onto stack. Pop, visit, then go right. O(n) time, O(h) space.",
                "Easy", "Trees", 43,
            ),
            ProblemEntity(
                "p044", "Same Tree",
                "Given the roots of two binary trees, check if they are structurally identical and have the same values.",
                "p = [1, 2, 3], q = [1, 2, 3]", "true",
                """fun isSameTree(p: TreeNode?, q: TreeNode?): Boolean {
    if (p == null && q == null) return true
    if (p == null || q == null || p.value != q.value) return false
    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right)
}""",
                "Recursive comparison: both null → true. One null or values differ → false. Otherwise recursively compare left and right subtrees. O(n) time, O(h) space.",
                "Easy", "Trees", 44,
            ),
            ProblemEntity(
                "p045", "Symmetric Tree",
                "Given the root of a binary tree, check if it's a mirror of itself (symmetric around its center).",
                "root = [1, 2, 2, 3, 4, 4, 3]", "true",
                """fun isSymmetric(root: TreeNode?): Boolean {
    fun check(left: TreeNode?, right: TreeNode?): Boolean {
        if (left == null && right == null) return true
        if (left == null || right == null || left.value != right.value) return false
        return check(left.left, right.right) && check(left.right, right.left)
    }
    return check(root?.left, root?.right)
}""",
                "Recursively compare left and right subtrees in a mirrored fashion. Left's left should match right's right, and left's right should match right's left. O(n) time, O(h) space.",
                "Easy", "Trees", 45,
            ),
            ProblemEntity(
                "p046", "Binary Tree Level Order Traversal",
                "Return the level-order traversal of a binary tree (left to right, level by level).",
                "root = [3, 9, 20, null, null, 15, 7]", "[[3], [9, 20], [15, 7]]",
                """fun levelOrder(root: TreeNode?): List<List<Int>> {
    val result = mutableListOf<MutableList<Int>>()
    val queue = ArrayDeque<TreeNode>()
    root?.let { queue.addLast(it) }
    while (queue.isNotEmpty()) {
        val level = mutableListOf<Int>()
        repeat(queue.size) {
            val node = queue.removeFirst()
            level.add(node.value)
            node.left?.let { queue.addLast(it) }
            node.right?.let { queue.addLast(it) }
        }
        result.add(level)
    }
    return result
}""",
                "BFS using a queue. Process one level at a time by capturing the queue size before processing children. O(n) time, O(n) space.",
                "Medium", "Trees", 46,
            ),
            ProblemEntity(
                "p047", "Invert Binary Tree",
                "Given the root of a binary tree, invert it (swap left and right children for every node).",
                "root = [4, 2, 7, 1, 3, 6, 9]", "[4, 7, 2, 9, 6, 3, 1]",
                """fun invertTree(root: TreeNode?): TreeNode? {
    if (root == null) return null
    val temp = root.left
    root.left = invertTree(root.right)
    root.right = invertTree(root.left)
    return root
}""",
                "Recursively swap left and right children for each node. Or use a queue for iterative BFS approach. O(n) time, O(h) space.",
                "Easy", "Trees", 47,
            ),
            ProblemEntity(
                "p048", "Balanced Binary Tree",
                "Check if a binary tree is height-balanced (depth difference of subtrees never exceeds 1).",
                "root = [3, 9, 20, null, null, 15, 7]", "true",
                """fun isBalanced(root: TreeNode?): Boolean {
    fun height(node: TreeNode?): Int {
        if (node == null) return 0
        val left = height(node.left)
        if (left == -1) return -1
        val right = height(node.right)
        if (right == -1) return -1
        if (kotlin.math.abs(left - right) > 1) return -1
        return 1 + maxOf(left, right)
    }
    return height(root) != -1
}""",
                "Post-order traversal computing heights. If any subtree is unbalanced (height diff > 1), propagate -1 upward. O(n) time, O(h) space.",
                "Easy", "Trees", 48,
            ),
            ProblemEntity(
                "p049", "Path Sum",
                "Given a binary tree and a target sum, determine if there's a root-to-leaf path where node values sum to target.",
                "root = [5, 4, 8, 11, null, 13, 4, 7, 2, null, null, 5, 1], targetSum = 22", "true",
                """fun hasPathSum(root: TreeNode?, targetSum: Int): Boolean {
    if (root == null) return false
    if (root.left == null && root.right == null)
        return targetSum == root.value
    return hasPathSum(root.left, targetSum - root.value) ||
           hasPathSum(root.right, targetSum - root.value)
}""",
                "DFS with subtraction: subtract the current node's value from target, check if leaf node matches remaining sum. Recurse on children. O(n) time, O(h) space.",
                "Easy", "Trees", 49,
            ),
            ProblemEntity(
                "p050", "Lowest Common Ancestor of BST",
                "Given a BST and two nodes, find their lowest common ancestor.",
                "root = [6, 2, 8, 0, 4, 7, 9, null, null, 3, 5], p = 2, q = 8", "6",
                """fun lowestCommonAncestor(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode? {
    var curr = root
    while (curr != null) {
        curr = when {
            p?.value!! < curr.value && q?.value!! < curr.value -> curr.left
            p.value > curr.value && q.value > curr.value -> curr.right
            else -> return curr
        }
    }
    return null
}""",
                "Leverage BST property: if both values are less than current, go left. If both greater, go right. Otherwise current is the LCA. O(h) time, O(1) space.",
                "Medium", "Trees", 50,
            ),
            // ═══════════════ GRAPHS (8) ═══════════════
            ProblemEntity(
                "p051", "Clone Graph",
                "Given a reference to a node in a connected undirected graph, return a deep copy of the graph.",
                "adjList = [[2, 4], [1, 3], [2, 4], [1, 3]]", "[[2, 4], [1, 3], [2, 4], [1, 3]]",
                """fun cloneGraph(node: Node?): Node? {
    if (node == null) return null
    val map = mutableMapOf<Int, Node>()
    fun dfs(n: Node): Node {
        if (n.`val` in map) return map[n.`val`]!!
        val copy = Node(n.`val`)
        map[n.`val`] = copy
        for (neighbor in n.neighbors)
            copy.neighbors.add(dfs(neighbor))
        return copy
    }
    return dfs(node)
}""",
                "DFS with a hash map to track cloned nodes. For each node, create a copy, recursively clone neighbors, and add them to the copy's neighbor list. O(V+E) time, O(V) space.",
                "Medium", "Graphs", 51,
            ),
            ProblemEntity(
                "p052", "Number of Islands",
                "Given a 2D grid of '1's (land) and '0's (water), count the number of islands (connected 1s).",
                "grid = [[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"0\",\"0\",\"1\",\"0\",\"0\"],[\"0\",\"0\",\"0\",\"1\",\"1\"]]", "3",
                """fun numIslands(grid: Array<CharArray>): Int {
    var count = 0
    fun dfs(r: Int, c: Int) {
        if (r !in grid.indices || c !in grid[0].indices || grid[r][c] == '0') return
        grid[r][c] = '0'
        dfs(r + 1, c); dfs(r - 1, c); dfs(r, c + 1); dfs(r, c - 1)
    }
    for (r in grid.indices)
        for (c in grid[0].indices)
            if (grid[r][c] == '1') { count++; dfs(r, c) }
    return count
}""",
                "DFS flood fill: when a '1' is found, increment count and sink the entire island by marking visited cells as '0'. O(m×n) time, O(m×n) space in worst case.",
                "Medium", "Graphs", 52,
            ),
            ProblemEntity(
                "p053", "Course Schedule",
                "Given n courses labeled 0 to n-1 and prerequisites pairs [a, b] meaning b must be taken before a, determine if it's possible to finish all courses.",
                "numCourses = 2, prerequisites = [[1, 0]]", "true",
                """fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    val graph = Array(numCourses) { mutableListOf<Int>() }
    val inDegree = IntArray(numCourses)
    for ((course, prereq) in prerequisites) {
        graph[prereq].add(course)
        inDegree[course]++
    }
    val queue = ArrayDeque<Int>()
    inDegree.indices.filter { inDegree[it] == 0 }.forEach { queue.addLast(it) }
    var completed = 0
    while (queue.isNotEmpty()) {
        val course = queue.removeFirst()
        completed++
        for (next in graph[course])
            if (--inDegree[next] == 0) queue.addLast(next)
    }
    return completed == numCourses
}""",
                "Kahn's algorithm for topological sort: build adjacency list and in-degree array. Start with nodes having 0 in-degree. Process each, decrement neighbors' in-degrees. If all courses processed, no cycle exists. O(V+E) time, O(V+E) space.",
                "Medium", "Graphs", 53,
            ),
            ProblemEntity(
                "p054", "Word Search",
                "Given a 2D board of characters and a word, determine if the word exists in the grid through adjacent cells (no reuse of same cell).",
                "board = [[\"A\",\"B\",\"C\",\"E\"],[\"S\",\"F\",\"C\",\"S\"],[\"A\",\"D\",\"E\",\"E\"]], word = \"ABCCED\"", "true",
                """fun exist(board: Array<CharArray>, word: String): Boolean {
    fun dfs(r: Int, c: Int, i: Int): Boolean {
        if (i == word.length) return true
        if (r !in board.indices || c !in board[0].indices || board[r][c] != word[i]) return false
        val temp = board[r][c]; board[r][c] = '#'
        val found = dfs(r+1,c,i+1) || dfs(r-1,c,i+1) || dfs(r,c+1,i+1) || dfs(r,c-1,i+1)
        board[r][c] = temp
        return found
    }
    for (r in board.indices)
        for (c in board[0].indices)
            if (dfs(r, c, 0)) return true
    return false
}""",
                "DFS + backtracking from each matching starting cell. Mark visited cells with a placeholder, explore all 4 directions, then restore. O(n·3^L) time where L is word length.",
                "Medium", "Graphs", 54,
            ),
            ProblemEntity(
                "p055", "Flood Fill",
                "Perform a flood fill on a 2D image: change the starting pixel and all connected same-colored pixels to a new color.",
                "image = [[1, 1, 1], [1, 1, 0], [1, 0, 1]], sr = 1, sc = 1, color = 2", "[[2, 2, 2], [2, 2, 0], [2, 0, 1]]",
                """fun floodFill(image: Array<IntArray>, sr: Int, sc: Int, color: Int): Array<IntArray> {
    val original = image[sr][sc]
    if (original == color) return image
    fun dfs(r: Int, c: Int) {
        if (r !in image.indices || c !in image[0].indices || image[r][c] != original) return
        image[r][c] = color
        dfs(r+1,c); dfs(r-1,c); dfs(r,c+1); dfs(r,c-1)
    }
    dfs(sr, sc)
    return image
}""",
                "DFS from the starting pixel. Change each matching pixel to the new color, then recursively visit 4-directional neighbors. O(m×n) time, O(m×n) space.",
                "Easy", "Graphs", 55,
            ),
            ProblemEntity(
                "p056", "Find the Town Judge",
                "In a town of n people, the town judge trusts nobody and is trusted by everyone else. Find the judge or return -1.",
                "n = 2, trust = [[1, 2]]", "2",
                """fun findJudge(n: Int, trust: Array<IntArray>): Int {
    val trustCount = IntArray(n + 1)
    val trustedBy = IntArray(n + 1)
    for ((a, b) in trust) {
        trustCount[a]++
        trustedBy[b]++
    }
    for (i in 1..n)
        if (trustCount[i] == 0 && trustedBy[i] == n - 1) return i
    return -1
}""",
                "Track two arrays: how many people each person trusts, and how many trust each person. The judge has trustCount == 0 and trustedBy == n-1. O(E) time, O(n) space.",
                "Easy", "Graphs", 56,
            ),
            ProblemEntity(
                "p057", "Pacific Atlantic Water Flow",
                "Find cells in a matrix from which water can flow to both the Pacific (top/left) and Atlantic (bottom/right) oceans.",
                "heights = [[1, 2, 2, 3, 5], [3, 2, 3, 4, 4], [2, 4, 5, 3, 1], [6, 7, 1, 4, 5], [5, 1, 1, 2, 4]]", "[[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]]",
                """fun pacificAtlantic(heights: Array<IntArray>): List<List<Int>> {
    val m = heights.size; val n = heights[0].size
    val pac = Array(m) { BooleanArray(n) }
    val atl = Array(m) { BooleanArray(n) }
    fun dfs(r: Int, c: Int, visited: Array<BooleanArray>, prev: Int) {
        if (r !in 0 until m || c !in 0 until n || visited[r][c] || heights[r][c] < prev) return
        visited[r][c] = true
        dfs(r+1,c,visited,heights[r][c]); dfs(r-1,c,visited,heights[r][c])
        dfs(r,c+1,visited,heights[r][c]); dfs(r,c-1,visited,heights[r][c])
    }
    for (i in 0 until m) { dfs(i,0,pac,0); dfs(i,n-1,atl,0) }
    for (j in 0 until n) { dfs(0,j,pac,0); dfs(m-1,j,atl,0) }
    return (0 until m).flatMap { r -> (0 until n).filter { c -> pac[r][c] && atl[r][c] }.map { c -> listOf(r,c) } }
}""",
                "DFS from the borders: start from Pacific edges (top/left) and Atlantic edges (bottom/right). Cells reachable from both borders are the answer. O(m×n) time, O(m×n) space.",
                "Medium", "Graphs", 57,
            ),
            ProblemEntity(
                "p058", "Graph Valid Tree",
                "Given n nodes labeled 0 to n-1 and an edge list, check if the edges form a valid tree (connected and acyclic).",
                "n = 5, edges = [[0, 1], [0, 2], [0, 3], [1, 4]]", "true",
                """fun validTree(n: Int, edges: Array<IntArray>): Boolean {
    if (edges.size != n - 1) return false
    val parent = IntArray(n) { it }
    fun find(x: Int): Int {
        if (parent[x] != x) parent[x] = find(parent[x])
        return parent[x]
    }
    fun union(x: Int, y: Int): Boolean {
        val px = find(x); val py = find(y)
        if (px == py) return false
        parent[px] = py; return true
    }
    for ((a, b) in edges) if (!union(a, b)) return false
    return true
}""",
                "A valid tree must have exactly n-1 edges and no cycles. Use Union-Find: if any edge connects already-connected nodes, a cycle exists. O(V·α(V)) time, O(V) space.",
                "Medium", "Graphs", 58,
            ),
            // ═══════════════ DYNAMIC PROGRAMMING (10) ═══════════════
            ProblemEntity(
                "p059", "Fibonacci Number",
                "Compute the nth Fibonacci number. F(0) = 0, F(1) = 1, F(n) = F(n-1) + F(n-2).",
                "n = 4", "3",
                """fun fib(n: Int): Int {
    if (n <= 1) return n
    var a = 0; var b = 1
    for (i in 2..n) { val c = a + b; a = b; b = c }
    return b
}""",
                "Iterative DP with constant space: keep only the last two values. O(n) time, O(1) space.",
                "Easy", "Dynamic Programming", 59,
            ),
            ProblemEntity(
                "p060", "Climbing Stairs",
                "You're climbing a staircase with n steps. Each time you can climb 1 or 2 steps. How many distinct ways to reach the top?",
                "n = 3", "3",
                """fun climbStairs(n: Int): Int {
    if (n <= 2) return n
    var a = 1; var b = 2
    for (i in 3..n) { val c = a + b; a = b; b = c }
    return b
}""",
                "This is the Fibonacci sequence: ways(n) = ways(n-1) + ways(n-2). Use iterative DP with O(n) time and O(1) space.",
                "Easy", "Dynamic Programming", 60,
            ),
            ProblemEntity(
                "p061", "Coin Change",
                "Given an array of coin denominations and a target amount, find the minimum number of coins needed to make that amount. Return -1 if impossible.",
                "coins = [1, 2, 5], amount = 11", "3",
                """fun coinChange(coins: IntArray, amount: Int): Int {
    val dp = IntArray(amount + 1) { amount + 1 }
    dp[0] = 0
    for (i in 1..amount)
        for (coin in coins)
            if (coin <= i) dp[i] = minOf(dp[i], dp[i - coin] + 1)
    return if (dp[amount] > amount) -1 else dp[amount]
}""",
                "DP bottom-up: dp[i] = minimum coins to make amount i. For each amount, try each coin denomination. O(n·amount) time, O(amount) space.",
                "Medium", "Dynamic Programming", 61,
            ),
            ProblemEntity(
                "p062", "Longest Increasing Subsequence",
                "Given an integer array, find the length of the longest strictly increasing subsequence.",
                "nums = [10, 9, 2, 5, 3, 7, 101, 18]", "4",
                """fun lengthOfLIS(nums: IntArray): Int {
    val dp = IntArray(nums.size) { 1 }
    var maxLen = 1
    for (i in nums.indices)
        for (j in 0 until i)
            if (nums[j] < nums[i]) {
                dp[i] = maxOf(dp[i], dp[j] + 1)
                maxLen = maxOf(maxLen, dp[i])
            }
    return maxLen
}""",
                "DP approach: dp[i] = LIS ending at index i. For each i, check all previous j where nums[j] < nums[i]. O(n²) time, O(n) space. Can be optimized to O(n log n) using patience sorting.",
                "Medium", "Dynamic Programming", 62,
            ),
            ProblemEntity(
                "p063", "Longest Common Subsequence",
                "Given two strings, find the length of their longest common subsequence.",
                "text1 = \"abcde\", text2 = \"ace\"", "3",
                """fun longestCommonSubsequence(text1: String, text2: String): Int {
    val m = text1.length; val n = text2.length
    val dp = Array(m + 1) { IntArray(n + 1) }
    for (i in 1..m)
        for (j in 1..n)
            dp[i][j] = if (text1[i-1] == text2[j-1]) dp[i-1][j-1] + 1
                       else maxOf(dp[i-1][j], dp[i][j-1])
    return dp[m][n]
}""",
                "2D DP: if characters match, add 1 to diagonal value; otherwise take max of left and top. O(m·n) time, O(m·n) space.",
                "Medium", "Dynamic Programming", 63,
            ),
            ProblemEntity(
                "p064", "House Robber",
                "Given an array of money in houses, find the maximum amount you can rob without robbing adjacent houses.",
                "nums = [1, 2, 3, 1]", "4",
                """fun rob(nums: IntArray): Int {
    if (nums.isEmpty()) return 0
    if (nums.size == 1) return nums[0]
    var prev2 = nums[0]
    var prev1 = maxOf(nums[0], nums[1])
    for (i in 2 until nums.size) {
        val curr = maxOf(prev1, prev2 + nums[i])
        prev2 = prev1; prev1 = curr
    }
    return prev1
}""",
                "DP: at each house, max = max(rob current + skip previous, skip current). Keep only two previous values. O(n) time, O(1) space.",
                "Medium", "Dynamic Programming", 64,
            ),
            ProblemEntity(
                "p065", "Edit Distance",
                "Given two strings, find the minimum operations (insert, delete, replace) to convert one into the other.",
                "word1 = \"horse\", word2 = \"ros\"", "3",
                """fun minDistance(word1: String, word2: String): Int {
    val m = word1.length; val n = word2.length
    val dp = Array(m + 1) { IntArray(n + 1) { it } }
    for (i in 0..m) dp[i][0] = i
    for (i in 1..m)
        for (j in 1..n)
            dp[i][j] = if (word1[i-1] == word2[j-1]) dp[i-1][j-1]
                       else 1 + minOf(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])
    return dp[m][n]
}""",
                "2D DP: dp[i][j] = edit distance for prefixes of length i and j. If chars match, take diagonal. Otherwise 1 + min(delete, insert, replace). O(m·n) time and space.",
                "Hard", "Dynamic Programming", 65,
            ),
            ProblemEntity(
                "p066", "Unique Paths",
                "A robot is at the top-left corner of an m×n grid. It can only move down or right. How many unique paths to the bottom-right corner?",
                "m = 3, n = 7", "28",
                """fun uniquePaths(m: Int, n: Int): Int {
    val dp = IntArray(n) { 1 }
    for (i in 1 until m)
        for (j in 1 until n)
            dp[j] += dp[j - 1]
    return dp[n - 1]
}""",
                "DP with space optimization: dp[j] = dp[j] + dp[j-1] (top + left). Initialize first row to 1. O(m·n) time, O(n) space.",
                "Medium", "Dynamic Programming", 66,
            ),
            ProblemEntity(
                "p067", "0/1 Knapsack",
                "Given weights and values of n items and a knapsack capacity, find the maximum value that can be carried.",
                "weights = [1, 2, 3], values = [6, 10, 12], capacity = 5", "22",
                """fun knapsack(weights: IntArray, values: IntArray, capacity: Int): Int {
    val n = weights.size
    val dp = Array(n + 1) { IntArray(capacity + 1) }
    for (i in 1..n)
        for (w in 0..capacity)
            dp[i][w] = if (weights[i-1] <= w)
                maxOf(dp[i-1][w], dp[i-1][w - weights[i-1]] + values[i-1])
            else dp[i-1][w]
    return dp[n][capacity]
}""",
                "2D DP: dp[i][w] = max value using first i items with capacity w. Either skip item i or take it (if it fits). O(n·capacity) time and space.",
                "Medium", "Dynamic Programming", 67,
            ),
            ProblemEntity(
                "p068", "Maximum Product Subarray",
                "Find the contiguous subarray with the largest product within an integer array (may contain negative numbers).",
                "nums = [2, 3, -2, 4]", "6",
                """fun maxProduct(nums: IntArray): Int {
    var maxProd = nums[0]
    var currMax = nums[0]
    var currMin = nums[0]
    for (i in 1 until nums.size) {
        val temp = currMax
        currMax = maxOf(nums[i], maxOf(currMax * nums[i], currMin * nums[i]))
        currMin = minOf(nums[i], minOf(temp * nums[i], currMin * nums[i]))
        maxProd = maxOf(maxProd, currMax)
    }
    return maxProd
}""",
                "Track both max and min products at each position (negative numbers can flip). Similar to Kadane's but for multiplication. O(n) time, O(1) space.",
                "Medium", "Dynamic Programming", 68,
            ),
            // ═══════════════ SORTING & SEARCHING (8) ═══════════════
            ProblemEntity(
                "p069", "Binary Search",
                "Given a sorted array of integers and a target, return the index of the target. Return -1 if not found.",
                "nums = [-1, 0, 3, 5, 9, 12], target = 9", "4",
                """fun binarySearch(nums: IntArray, target: Int): Int {
    var left = 0; var right = nums.lastIndex
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
                "Standard binary search: repeatedly divide the search interval in half. O(log n) time, O(1) space.",
                "Easy", "Sorting & Searching", 69,
            ),
            ProblemEntity(
                "p070", "First Bad Version",
                "You have n versions [1..n] and want to find the first bad version. The API isBadVersion(version) tells if a version is bad.",
                "n = 5, bad = 4", "4",
                """fun firstBadVersion(n: Int): Int {
    var left = 1; var right = n
    while (left < right) {
        val mid = left + (right - left) / 2
        if (isBadVersion(mid)) right = mid
        else left = mid + 1
    }
    return left
}""",
                "Binary search for the first true value. If mid is bad, search left half. If good, search right half. O(log n) time, O(1) space.",
                "Easy", "Sorting & Searching", 70,
            ),
            ProblemEntity(
                "p071", "Merge Sort",
                "Implement merge sort on an integer array.",
                "arr = [38, 27, 43, 3, 9, 82, 10]", "[3, 9, 10, 27, 38, 43, 82]",
                """fun mergeSort(arr: IntArray): IntArray {
    if (arr.size <= 1) return arr
    val mid = arr.size / 2
    val left = mergeSort(arr.sliceArray(0 until mid))
    val right = mergeSort(arr.sliceArray(mid until arr.size))
    var i = 0; var j = 0; var k = 0
    while (i < left.size && j < right.size)
        arr[k++] = if (left[i] <= right[j]) left[i++] else right[j++]
    while (i < left.size) arr[k++] = left[i++]
    while (j < right.size) arr[k++] = right[j++]
    return arr
}""",
                "Divide and conquer: split array in half, recursively sort each half, then merge the sorted halves. O(n log n) time, O(n) space.",
                "Medium", "Sorting & Searching", 71,
            ),
            ProblemEntity(
                "p072", "Quick Sort",
                "Implement quick sort on an integer array.",
                "arr = [10, 7, 8, 9, 1, 5]", "[1, 5, 7, 8, 9, 10]",
                """fun quickSort(arr: IntArray, low: Int, high: Int) {
    if (low >= high) return
    val pivot = partition(arr, low, high)
    quickSort(arr, low, pivot - 1)
    quickSort(arr, pivot + 1, high)
}

fun partition(arr: IntArray, low: Int, high: Int): Int {
    val pivot = arr[high]
    var i = low - 1
    for (j in low until high)
        if (arr[j] <= pivot) { i++; val t = arr[i]; arr[i] = arr[j]; arr[j] = t }
    val t = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = t
    return i + 1
}""",
                "Divide and conquer with Lomuto partition: pick a pivot (last element), arrange smaller elements to the left, then recursively sort partitions. Average O(n log n), worst O(n²). O(log n) space.",
                "Medium", "Sorting & Searching", 72,
            ),
            ProblemEntity(
                "p073", "Search Insert Position",
                "Given a sorted array and a target, return the index where target would be inserted to maintain order.",
                "nums = [1, 3, 5, 6], target = 5", "2",
                """fun searchInsert(nums: IntArray, target: Int): Int {
    var left = 0; var right = nums.lastIndex
    while (left <= right) {
        val mid = left + (right - left) / 2
        if (nums[mid] == target) return mid
        if (nums[mid] < target) left = mid + 1
        else right = mid - 1
    }
    return left
}""",
                "Binary search: if target found, return its index. If not, 'left' ends up at the correct insertion position. O(log n) time, O(1) space.",
                "Easy", "Sorting & Searching", 73,
            ),
            ProblemEntity(
                "p074", "Find Peak Element",
                "Find a peak element in an array (greater than its neighbors). The array may have multiple peaks; return any peak's index.",
                "nums = [1, 2, 3, 1]", "2",
                """fun findPeakElement(nums: IntArray): Int {
    var left = 0; var right = nums.lastIndex
    while (left < right) {
        val mid = left + (right - left) / 2
        if (nums[mid] > nums[mid + 1]) right = mid
        else left = mid + 1
    }
    return left
}""",
                "Binary search: compare mid with mid+1. If mid > mid+1, peak is in left half (including mid). Otherwise, peak is in right half. O(log n) time, O(1) space.",
                "Medium", "Sorting & Searching", 74,
            ),
            ProblemEntity(
                "p075", "Merge Intervals",
                "Given an array of intervals [start, end], merge all overlapping intervals.",
                "intervals = [[1, 3], [2, 6], [8, 10], [15, 18]]", "[[1, 6], [8, 10], [15, 18]]",
                """fun merge(intervals: Array<IntArray>): Array<IntArray> {
    if (intervals.isEmpty()) return emptyArray()
    intervals.sortBy { it[0] }
    val result = mutableListOf(intervals[0])
    for (i in 1 until intervals.size) {
        val last = result.last()
        if (intervals[i][0] <= last[1])
            last[1] = maxOf(last[1], intervals[i][1])
        else
            result.add(intervals[i])
    }
    return result.toTypedArray()
}""",
                "Sort by start time, then iterate: if current interval overlaps with the last merged one, extend the end. Otherwise, start a new interval. O(n log n) time, O(n) space.",
                "Medium", "Sorting & Searching", 75,
            ),
            ProblemEntity(
                "p076", "Sort Colors",
                "Given an array containing only 0, 1, and 2, sort it in-place. (Dutch national flag problem).",
                "nums = [2, 0, 2, 1, 1, 0]", "[0, 0, 1, 1, 2, 2]",
                """fun sortColors(nums: IntArray) {
    var left = 0; var mid = 0; var right = nums.lastIndex
    while (mid <= right) {
        when (nums[mid]) {
            0 -> { val t = nums[left]; nums[left] = nums[mid]; nums[mid] = t; left++; mid++ }
            1 -> mid++
            2 -> { val t = nums[right]; nums[right] = nums[mid]; nums[mid] = t; right-- }
        }
    }
}""",
                "Dutch national flag algorithm with 3 pointers: low for 0s, mid for 1s, high for 2s. Swap elements to their correct region. O(n) time, O(1) space.",
                "Medium", "Sorting & Searching", 76,
            ),
            // ═══════════════ HASH-BASED (6) ═══════════════
            ProblemEntity(
                "p077", "Contains Duplicate II",
                "Given an array and an integer k, check if there are two distinct indices i, j with nums[i] == nums[j] and |i - j| <= k.",
                "nums = [1, 2, 3, 1], k = 3", "true",
                """fun containsNearbyDuplicate(nums: IntArray, k: Int): Boolean {
    val map = mutableMapOf<Int, Int>()
    for ((i, num) in nums.withIndex()) {
        if (num in map && i - map[num]!! <= k) return true
        map[num] = i
    }
    return false
}""",
                "Use a hash map storing the last seen index of each value. When a repeat is found, check distance. O(n) time, O(n) space.",
                "Easy", "Hash-Based", 77,
            ),
            ProblemEntity(
                "p078", "Intersection of Two Arrays",
                "Given two arrays, return their intersection (elements appearing in both, without duplicates).",
                "nums1 = [1, 2, 2, 1], nums2 = [2, 2]", "[2]",
                """fun intersection(nums1: IntArray, nums2: IntArray): IntArray {
    val set1 = nums1.toSet()
    return nums2.filter { it in set1 }.distinct().toIntArray()
}""",
                "Convert one array to a hash set, then filter the second array for elements in the set. O(n+m) time, O(n) space.",
                "Easy", "Hash-Based", 78,
            ),
            ProblemEntity(
                "p079", "Single Number",
                "Given a non-empty array where every element appears twice except one, find the single element. Use O(n) time and O(1) space.",
                "nums = [4, 1, 2, 1, 2]", "4",
                """fun singleNumber(nums: IntArray): Int {
    var result = 0
    for (num in nums) result = result xor num
    return result
}""",
                "XOR all numbers. Since a XOR a = 0, all paired numbers cancel out, leaving the single element. O(n) time, O(1) space.",
                "Easy", "Hash-Based", 79,
            ),
            ProblemEntity(
                "p080", "Happy Number",
                "A number is happy if repeatedly replacing it by the sum of squares of its digits eventually reaches 1. Determine if a number is happy.",
                "n = 19", "true",
                """fun isHappy(n: Int): Boolean {
    fun sumSquares(x: Int): Int {
        var s = 0; var num = x
        while (num > 0) { val d = num % 10; s += d * d; num /= 10 }
        return s
    }
    var slow = n; var fast = sumSquares(n)
    while (fast != 1 && slow != fast) {
        slow = sumSquares(slow)
        fast = sumSquares(sumSquares(fast))
    }
    return fast == 1
}""",
                "Floyd's cycle detection: unreachable numbers produce a cycle. Use slow/fast pointers to detect cycles. If fast reaches 1, it's happy. O(log n) time, O(1) space.",
                "Easy", "Hash-Based", 80,
            ),
            ProblemEntity(
                "p081", "Isomorphic Strings",
                "Given two strings s and t, determine if they are isomorphic (characters can be replaced to get the other string).",
                "s = \"egg\", t = \"add\"", "true",
                """fun isIsomorphic(s: String, t: String): Boolean {
    if (s.length != t.length) return false
    val mapST = mutableMapOf<Char, Char>()
    val mapTS = mutableMapOf<Char, Char>()
    for (i in s.indices) {
        val c1 = s[i]; val c2 = t[i]
        if ((mapST[c1] ?: c2) != c2 || (mapTS[c2] ?: c1) != c1) return false
        mapST[c1] = c2; mapTS[c2] = c1
    }
    return true
}""",
                "Use two hash maps for bidirectional mapping. If a character maps to different targets from either direction, return false. O(n) time, O(k) space where k is the character set.",
                "Easy", "Hash-Based", 81,
            ),
            ProblemEntity(
                "p082", "Ransom Note",
                "Given a ransom note string and a magazine string, determine if the note can be constructed from the magazine's letters (each letter used at most once).",
                "ransomNote = \"a\", magazine = \"b\"", "false",
                """fun canConstruct(ransomNote: String, magazine: String): Boolean {
    val counts = IntArray(26)
    for (c in magazine) counts[c - 'a']++
    for (c in ransomNote) {
        if (counts[c - 'a'] == 0) return false
        counts[c - 'a']--
    }
    return true
}""",
                "Count character frequencies in magazine using an int array of size 26. Decrement for each character in ransomNote. If any count goes negative, return false. O(n+m) time, O(1) space.",
                "Easy", "Hash-Based", 82,
            ),
            // ═══════════════ RECURSION & MATH (8) ═══════════════
            ProblemEntity(
                "p083", "Pow(x, n)",
                "Implement pow(x, n) calculating x raised to the power n (n may be negative).",
                "x = 2.00000, n = 10", "1024.00000",
                """fun myPow(x: Double, n: Int): Double {
    if (n == 0) return 1.0
    val exp = n.toLong()
    var result = 1.0
    var base = if (exp > 0) x else 1.0 / x
    var power = kotlin.math.abs(exp)
    while (power > 0) {
        if (power % 2 == 1L) result *= base
        base *= base
        power /= 2
    }
    return result
}""",
                "Exponentiation by squaring: repeatedly square the base and multiply when the bit is set. Handle negative exponents by inverting the base. O(log n) time, O(1) space.",
                "Medium", "Recursion & Math", 83,
            ),
            ProblemEntity(
                "p084", "Generate Parentheses",
                "Given n pairs of parentheses, generate all valid combinations.",
                "n = 3", "[\"((()))\",\"(()())\",\"(())()\",\"()(())\",\"()()()\"]",
                """fun generateParenthesis(n: Int): List<String> {
    val result = mutableListOf<String>()
    fun backtrack(curr: String, open: Int, close: Int) {
        if (curr.length == n * 2) { result.add(curr); return }
        if (open < n) backtrack("${'$'}curr(", open + 1, close)
        if (close < open) backtrack("${'$'}curr)", open, close + 1)
    }
    backtrack("", 0, 0)
    return result
}""",
                "Backtracking: keep track of open and close counts. Add '(' if open < n, add ')' if close < open. The Catalan number C_n gives the count. O(4^n / √n) time.",
                "Medium", "Recursion & Math", 84,
            ),
            ProblemEntity(
                "p085", "Permutations",
                "Given an array of distinct integers, return all possible permutations.",
                "nums = [1, 2, 3]", "[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]",
                """fun permute(nums: IntArray): List<List<Int>> {
    val result = mutableListOf<MutableList<Int>>()
    val current = mutableListOf<Int>()
    val used = BooleanArray(nums.size)
    fun backtrack() {
        if (current.size == nums.size) { result.add(current.toList()); return }
        for (i in nums.indices) {
            if (used[i]) continue
            used[i] = true; current.add(nums[i])
            backtrack()
            current.removeAt(current.lastIndex); used[i] = false
        }
    }
    backtrack()
    return result
}""",
                "Backtracking: try each unused element, recurse, then backtrack. Track used elements with a boolean array. O(n·n!) time, O(n) space.",
                "Medium", "Recursion & Math", 85,
            ),
            ProblemEntity(
                "p086", "Subsets",
                "Given an array of distinct integers, return all possible subsets (the power set).",
                "nums = [1, 2, 3]", "[[], [1], [2], [1, 2], [3], [1, 3], [2, 3], [1, 2, 3]]",
                """fun subsets(nums: IntArray): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    val current = mutableListOf<Int>()
    fun backtrack(start: Int) {
        result.add(current.toList())
        for (i in start until nums.size) {
            current.add(nums[i])
            backtrack(i + 1)
            current.removeAt(current.lastIndex)
        }
    }
    backtrack(0)
    return result
}""",
                "Backtracking: at each step, include the current element and recurse on remaining elements, then exclude it. O(n·2^n) time, O(n) space.",
                "Medium", "Recursion & Math", 86,
            ),
            ProblemEntity(
                "p087", "Letter Combinations of a Phone Number",
                "Given digits 2-9, return all possible letter combinations from the phone keypad.",
                "digits = \"23\"", "[\"ad\", \"ae\", \"af\", \"bd\", \"be\", \"bf\", \"cd\", \"ce\", \"cf\"]",
                """fun letterCombinations(digits: String): List<String> {
    if (digits.isEmpty()) return emptyList()
    val map = mapOf('2' to "abc", '3' to "def", '4' to "ghi",
        '5' to "jkl", '6' to "mno", '7' to "pqrs", '8' to "tuv", '9' to "wxyz")
    val result = mutableListOf<String>()
    fun backtrack(idx: Int, curr: StringBuilder) {
        if (idx == digits.length) { result.add(curr.toString()); return }
        for (c in map[digits[idx]]!!) { curr.append(c); backtrack(idx+1, curr); curr.deleteCharAt(curr.lastIndex) }
    }
    backtrack(0, StringBuilder())
    return result
}""",
                "Backtracking: map each digit to its letters, try each letter, recurse for the next digit. O(4^n) time where n is digit count. O(n) space.",
                "Medium", "Recursion & Math", 87,
            ),
            ProblemEntity(
                "p088", "Count Good Numbers",
                "A digit string is good if even-indexed digits are even (0,2,4,6,8) and odd-indexed digits are prime (2,3,5,7). Count good digit strings of length n modulo 10^9+7.",
                "n = 4", "400",
                """fun countGoodNumbers(n: Long): Int {
    val MOD = 1_000_000_007L
    fun pow(a: Long, b: Long): Long {
        if (b == 0L) return 1
        var res = pow(a, b / 2)
        res = (res * res) % MOD
        return if (b % 2 == 1L) (res * a) % MOD else res
    }
    val even = (n + 1) / 2
    val odd = n / 2
    return (pow(5, even) * pow(4, odd) % MOD).toInt()
}""",
                "Combinatorics + fast exponentiation. Each even position has 5 choices (0,2,4,6,8). Each odd position has 4 choices (2,3,5,7). Compute 5^even × 4^odd mod MOD. O(log n) time, O(1) space.",
                "Medium", "Recursion & Math", 88,
            ),
            ProblemEntity(
                "p089", "Ugly Number",
                "An ugly number is a positive integer whose prime factors are limited to 2, 3, and 5. Determine if a given number is ugly.",
                "n = 6", "true",
                """fun isUgly(n: Int): Boolean {
    if (n <= 0) return false
    var num = n
    for (factor in intArrayOf(2, 3, 5))
        while (num % factor == 0) num /= factor
    return num == 1
}""",
                "Repeatedly divide by 2, 3, and 5. If the result is 1, the number is ugly. O(log n) time, O(1) space.",
                "Easy", "Recursion & Math", 89,
            ),
            ProblemEntity(
                "p090", "Factorial Trailing Zeroes",
                "Given an integer n, return the number of trailing zeroes in n! (n factorial).",
                "n = 5", "1",
                """fun trailingZeroes(n: Int): Int {
    var count = 0
    var num = n
    while (num >= 5) { num /= 5; count += num }
    return count
}""",
                "Trailing zeroes are produced by factors of 10 = 2 × 5. Since there are always more 2s than 5s, count the number of 5 factors. Count multiples of 5, 25, 125, etc. O(log n) time, O(1) space.",
                "Easy", "Recursion & Math", 90,
            ),
            // ═══════════════ BIT MANIPULATION (5) ═══════════════
            ProblemEntity(
                "p091", "Number of 1 Bits",
                "Write a function that takes an unsigned integer and returns the number of '1' bits (Hamming weight).",
                "n = 11", "3",
                """fun hammingWeight(n: Int): Int {
    var count = 0
    var num = n
    while (num != 0) { num = num and (num - 1); count++ }
    return count
}""",
                "Brian Kernighan's algorithm: n & (n-1) flips the least significant 1 bit to 0. Count how many iterations until zero. O(k) time where k is the number of 1 bits, O(1) space.",
                "Easy", "Bit Manipulation", 91,
            ),
            ProblemEntity(
                "p092", "Power of Two",
                "Given an integer n, return true if it's a power of two.",
                "n = 16", "true",
                """fun isPowerOfTwo(n: Int): Boolean =
    n > 0 && (n and (n - 1)) == 0""",
                "A power of two has exactly one bit set. n & (n-1) clears that bit, so the result is 0 only for powers of two. O(1) time, O(1) space.",
                "Easy", "Bit Manipulation", 92,
            ),
            ProblemEntity(
                "p093", "Reverse Bits",
                "Reverse the bits of a 32-bit unsigned integer.",
                "n = 43261596", "964176192",
                """fun reverseBits(n: Int): Int {
    var result = 0
    var num = n
    for (i in 0 until 32) {
        result = (result shl 1) or (num and 1)
        num = num shr 1
    }
    return result
}""",
                "Iterate through all 32 bits: shift result left, add the LSB of n, shift n right. O(32) = O(1) time, O(1) space.",
                "Easy", "Bit Manipulation", 93,
            ),
            ProblemEntity(
                "p094", "Missing Number",
                "Given an array containing n distinct numbers from [0, n], find the missing number.",
                "nums = [3, 0, 1]", "2",
                """fun missingNumber(nums: IntArray): Int {
    var result = nums.size
    for ((i, num) in nums.withIndex()) result = result xor i xor num
    return result
}""",
                "XOR all indices and all values. Paired values cancel out, leaving the missing number. O(n) time, O(1) space.",
                "Easy", "Bit Manipulation", 94,
            ),
            ProblemEntity(
                "p095", "Counting Bits",
                "Given an integer n, return an array of length n+1 where ans[i] is the number of 1 bits in the binary representation of i.",
                "n = 5", "[0, 1, 1, 2, 1, 3]",
                """fun countBits(n: Int): IntArray {
    val dp = IntArray(n + 1)
    for (i in 1..n) dp[i] = dp[i shr 1] + (i and 1)
    return dp
}""",
                "DP + bit manipulation: dp[i] = dp[i/2] + (i % 2). The number of 1 bits in i equals the count in i/2 plus the LSB of i. O(n) time, O(n) space.",
                "Easy", "Bit Manipulation", 95,
            ),
            // ═══════════════ MISCELLANEOUS (5) ═══════════════
            ProblemEntity(
                "p096", "Fizz Buzz",
                "Given an integer n, return a string array where for numbers 1..n: multiples of 3 → 'Fizz', 5 → 'Buzz', both → 'FizzBuzz', else the number.",
                "n = 15", "[\"1\",\"2\",\"Fizz\",\"4\",\"Buzz\",...]",
                """fun fizzBuzz(n: Int): List<String> {
    return (1..n).map { i ->
        when {
            i % 15 == 0 -> "FizzBuzz"
            i % 3 == 0 -> "Fizz"
            i % 5 == 0 -> "Buzz"
            else -> i.toString()
        }
    }
}""",
                "Simple iteration with modulo checks. Check 15 first (both 3 and 5) for correctness. O(n) time, O(n) space for output.",
                "Easy", "Miscellaneous", 96,
            ),
            ProblemEntity(
                "p097", "Roman to Integer",
                "Convert a Roman numeral string to an integer. Input is guaranteed to be valid (1-3999).",
                "s = \"LVIII\"", "58",
                """fun romanToInt(s: String): Int {
    val values = mapOf('I' to 1, 'V' to 5, 'X' to 10, 'L' to 50, 'C' to 100, 'D' to 500, 'M' to 1000)
    var total = 0; var prev = 0
    for (c in s.reversed()) {
        val curr = values[c]!!
        total += if (curr < prev) -curr else curr
        prev = curr
    }
    return total
}""",
                "Process from right to left. If a smaller value appears before a larger (e.g., IV), subtract it. Otherwise add. O(n) time, O(1) space.",
                "Easy", "Miscellaneous", 97,
            ),
            ProblemEntity(
                "p098", "Pascal's Triangle",
                "Given numRows, generate the first numRows of Pascal's triangle.",
                "numRows = 5", "[[1], [1, 1], [1, 2, 1], [1, 3, 3, 1], [1, 4, 6, 4, 1]]",
                """fun generate(numRows: Int): List<List<Int>> {
    val triangle = mutableListOf<List<Int>>()
    for (row in 0 until numRows) {
        val current = mutableListOf<Int>()
        for (col in 0..row)
            current.add(if (col == 0 || col == row) 1
                        else triangle[row-1][col-1] + triangle[row-1][col])
        triangle.add(current)
    }
    return triangle
}""",
                "Each row starts and ends with 1. Interior elements are the sum of the two elements above. O(numRows²) time, O(numRows²) space.",
                "Easy", "Miscellaneous", 98,
            ),
            ProblemEntity(
                "p099", "Plus One",
                "Given a large integer represented as a digit array, add one and return the result as an array.",
                "digits = [1, 2, 3]", "[1, 2, 4]",
                """fun plusOne(digits: IntArray): IntArray {
    for (i in digits.indices.reversed()) {
        if (digits[i] < 9) { digits[i]++; return digits }
        digits[i] = 0
    }
    return intArrayOf(1) + digits
}""",
                "Process from right to left. If digit < 9, increment and return. If 9, set to 0 and carry. If all digits were 9, prepend 1. O(n) time, O(1) extra space.",
                "Easy", "Miscellaneous", 99,
            ),
            ProblemEntity(
                "p100", "Excel Sheet Column Title",
                "Given a positive integer, return its corresponding column title as it appears in an Excel sheet (1 → A, 28 → AB, 701 → ZY).",
                "columnNumber = 28", "\"AB\"",
                """fun convertToTitle(columnNumber: Int): String {
    val sb = StringBuilder()
    var n = columnNumber
    while (n > 0) {
        n--
        sb.append('A' + (n % 26))
        n /= 26
    }
    return sb.reverse().toString()
}""",
                "Treat it as base-26 conversion with 1-indexed adjustment. Subtract 1 before each modulo to handle the 1-based system (A=1, not A=0). O(log n) time, O(log n) space.",
                "Easy", "Miscellaneous", 100,
            ),
        )
}
