package com.hackerrank.app.core

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class LocalizationResourcesTest {
    @Test
    fun `indonesian strings file exists`() {
        assertTrue("values-in/strings.xml not found", inFile.exists())
    }

    @Test
    fun `all english string keys exist in indonesian translations`() {
        val enKeys = extractKeys(enFile)
        val inKeys = extractKeys(inFile)

        val missing = enKeys - inKeys
        assertTrue(
            "Missing keys in values-in/strings.xml: ${missing.joinToString(", ")}",
            missing.isEmpty(),
        )
    }

    @Test
    fun `english and indonesian have same number of entries`() {
        val enKeys = extractKeys(enFile)
        val inKeys = extractKeys(inFile)

        assertTrue(
            "Key count mismatch: en=${enKeys.size} in=${inKeys.size}",
            enKeys.size == inKeys.size,
        )
    }

    @Test
    fun `english and indonesian format arguments match`() {
        val enArgs = extractFormatArgs(enFile)
        val inArgs = extractFormatArgs(inFile)

        val mismatches =
            enArgs.keys.intersect(inArgs.keys).filter { key ->
                enArgs[key] != inArgs[key]
            }
        assertTrue(
            "Format argument mismatch: ${mismatches.joinToString(", ") { "$it en=${enArgs[it]} in=${inArgs[it]}" }}",
            mismatches.isEmpty(),
        )
    }

    companion object {
        private val enFile: File = findResFile("values/strings.xml")
        private val inFile: File = findResFile("values-in/strings.xml")

        private fun findResFile(relativePath: String): File {
            val candidates =
                listOf(
                    File("app/src/main/res", relativePath),
                    File("src/main/res", relativePath),
                    File("../app/src/main/res", relativePath),
                )
            return candidates.firstOrNull { it.exists() }
                ?: error("Could not find res/$relativePath (tried ${candidates.map { it.path }})")
        }

        private fun extractKeys(file: File): Set<String> {
            val content = file.readText()
            val regex = Regex("""<(?:string|plurals)\s+name="([^"]+)"[^>]*>""")
            return regex.findAll(content).map { it.groupValues[1] }.toSet()
        }

        private fun extractFormatArgs(file: File): Map<String, List<String>> {
            val content = file.readText()
            val entryRegex = Regex("""<(?:string|plurals)\s+name="([^"]+)"[^>]*>(.*?)</(?:string|plurals)>""", RegexOption.DOT_MATCHES_ALL)
            val argRegex = Regex("""%(\d+\$)?[a-zA-Z]""")
            val result = mutableMapOf<String, List<String>>()
            for (match in entryRegex.findAll(content)) {
                val body = match.groupValues[2].replace("%%", "")
                val args = mutableListOf<String>()
                for (arg in argRegex.findAll(body)) {
                    args.add(arg.value)
                }
                // Distinct: quantity sets legitimately differ per locale (e.g. Indonesian
                // uses only `other`), but the argument shapes used must agree.
                result[match.groupValues[1]] = args.distinct().sorted()
            }
            return result
        }
    }
}
