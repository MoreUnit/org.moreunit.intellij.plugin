package org.moreunit.intellij.plugin

const val DOT_CHAR = '.'

fun applySameCapitalization(target: String, example: String): String {
    if (target.isEmpty() || example.isEmpty()) {
        return target
    }

    if (example.first().isUpperCase())
        return capitalize(target)

    return uncapitalize(target)
}

fun capitalize(str: String) =
    if (str.isEmpty()) str else str.first().toUpperCase()+str.substring(1)

fun uncapitalize(str: String) =
    if (str.isEmpty()) str else str.first().toLowerCase()+str.substring(1)

fun withoutExtension(name: String): String {
    val lastDotIndex = name.lastIndexOf(DOT_CHAR)
    return if (lastDotIndex <= 0) name else name.substring(0, lastDotIndex)
}

fun withoutLeadingDot(name: String): String =
    if (name.startsWith(DOT_CHAR)) name.substring(1) else name