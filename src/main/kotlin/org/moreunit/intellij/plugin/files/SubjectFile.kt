package org.moreunit.intellij.plugin.files

import com.intellij.openapi.vfs.VirtualFile
import org.moreunit.intellij.plugin.*


class SubjectFile(val fileNameWithoutExtension: String) {

    private val prefix: TestMarker?
    private val suffix: TestMarker?
    val isTestFile: Boolean

    constructor(srcVFile: VirtualFile): this(srcVFile.nameWithoutExtension)

    init {
        prefix = findTestPrefix(fileNameWithoutExtension)
        suffix = findTestSuffix(fileNameWithoutExtension)
        isTestFile = prefix != null || suffix != null
    }

    fun isCorrespondingFilename(name: String): Boolean {
        var srcName = fileNameWithoutExtension
        var destName = withoutExtension(name)

        // Let's assume that if the user triggered an action from an hidden file,
        // she actually wanted to do something with it...
        if (srcName.startsWith(DOT_CHAR)) {
            if (!destName.startsWith(DOT_CHAR))
                return false

            srcName = withoutLeadingDot(srcName)
            destName = withoutLeadingDot(destName)
        }

        return if (isTestFile) isCorrespondingProductionFilename(srcName, destName) else isCorrespondingTestFilename(srcName, destName)
    }

    private fun isCorrespondingTestFilename(srcName: String, destName: String): Boolean {
        COMMON_TEST_SUFFIXES.firstOrNull { suffix -> suffix.isSuffixBetween(srcName, destName) }?.let { return true }
        COMMON_TEST_PREFIXES.firstOrNull { prefix -> prefix.isPrefixBetween(srcName, destName) }?.let { return true }
        return false
    }

    private fun isCorrespondingProductionFilename(srcName: String, destName: String): Boolean {
        if (suffix != null && suffix.isSuffixBetween(destName, srcName)) return true

        return prefix != null && prefix.isPrefixBetween(destName, srcName)
    }

    companion object {
        val wordSeparators = listOf("-", "_", ".", " ", "")
        val prefixStrings = listOf("spec", "test", "tests")
        val suffixStrings = listOf("spec", "test", "tests", "should")

        val COMMON_TEST_PREFIXES: List<TestMarker> = prefixStrings.flatMap { prefix ->
            wordSeparators.map { separator ->
                TestMarker(prefix, separator)
            }
        }

        val COMMON_TEST_SUFFIXES: List<TestMarker> = suffixStrings.flatMap { suffix ->
            wordSeparators.map { separator ->
                TestMarker(suffix, separator)
            }
        }

        fun findTestPrefix(name: String): TestMarker? =
            COMMON_TEST_PREFIXES.firstOrNull { it.isPrefixIn(name) }

        fun findTestSuffix(name: String): TestMarker? =
            COMMON_TEST_SUFFIXES.firstOrNull { it.isSuffixIn(name) }
    }
}

class TestMarker(val marker: String, val separator: String) {

    fun isPrefixBetween(base: String, maybePrefixed: String): Boolean {
        if (isSeparatedByCase())
            return maybePrefixed == (applySameCapitalization(marker, maybePrefixed)+ capitalize(base))

        val prefixPart = marker + separator
        if (maybePrefixed.length != prefixPart.length + base.length)
            return false

        val start = maybePrefixed.substring(0, prefixPart.length)
        val end = maybePrefixed.substring(prefixPart.length)

        return end == base && start.toLowerCase() == prefixPart
    }

    fun isPrefixIn(name: String): Boolean {
        if (isSeparatedByCase()) {
            return name.startsWith(applySameCapitalization(marker, name))
        }

        return name.toLowerCase().startsWith(marker + separator)
    }

    fun isSuffixBetween(base: String, maybeSuffixed: String): Boolean {
        if (isSeparatedByCase())
            return maybeSuffixed.equals(base + capitalize(marker))

        val suffixPart = separator + marker
        if (maybeSuffixed.length != base.length + suffixPart.length) {
            return false
        }

        val start = maybeSuffixed.substring(0, maybeSuffixed.length - suffixPart.length)
        val end = maybeSuffixed.substring(maybeSuffixed.length - suffixPart.length)

        return start == base && end.toLowerCase() == suffixPart
    }

    fun isSuffixIn(name: String): Boolean {
        if (isSeparatedByCase())
            return name.endsWith(capitalize(marker))

        return name.toLowerCase().endsWith(separator+marker)
    }

    fun isSeparatedByCase(): Boolean {
        return separator == ""
    }
}
