package Day13

import checkAndPrint
import measureAndPrintTimeMillis
import readInput

fun main() {
    val parser = Parser()

    fun List<String>.prepareInput() = (this + "").chunked(3)
        .map { it.dropLast(1) }
        .map { (left, right) -> left to right }

    fun part1(input: List<Pair<String, String>>, printComparisons: Boolean = false): Int {
        fun compare(left: List<Any>, right: List<Any>, prefix: String = "-"): Tri {
            var pref = prefix
            pref = " $pref"

            for (i in left.indices) {
                val leftItem = left[i]
                val rightItem = right.getOrNull(i) ?: run {
                    if (printComparisons) println("$pref Right side ran out of items, so inputs are not in right order")
                    return Tri.False
                }

                if (printComparisons) println("$pref Compare $leftItem vs $rightItem")
                if (leftItem is Int) {
                    if (rightItem is Int) {
                        if (leftItem < rightItem) {
                            if (printComparisons) println(" $pref Left side is smaller, so inputs are in right order")
                            return Tri.True
                        } else if (leftItem > rightItem) {
                            if (printComparisons) println(" $pref Right side is smaller, so inputs are not in right order")
                            return Tri.False
                        }
                    } else if (rightItem is List<*>) {
                        val compare = compare(listOf(leftItem), rightItem as List<Any>, " $pref")
                        if (compare == Tri.True || compare == Tri.False) return compare
                    }
                } else {
                    if (rightItem is Int) {
                        val compare = compare(leftItem as List<Any>, listOf(rightItem), " $pref")
                        if (compare == Tri.True || compare == Tri.False) return compare
                    } else {
                        val compare = compare(leftItem as List<Any>, rightItem as List<Any>, " $pref")
                        if (compare == Tri.True || compare == Tri.False) return compare
                    }
                }
            }

            return if (left.size == right.size) Tri.Equal else {
                if (printComparisons) println("$pref Left side ran out of items, so inputs are in the right order")
                Tri.True
            }
        }
        fun compare(pair: Pair<List<Any>, List<Any>>): Tri {
            if (printComparisons) println("- Compare ${pair.first} vs ${pair.second}")
            return compare(pair.first, pair.second).also { if (printComparisons) println() }
        }

        return input
            .map { (left, right) -> parser.parse(left) to parser.parse(right) }
            .withIndex()
            .filter { (_, pair) -> compare(pair) == Tri.True }
            .sumOf { (idx, _) -> idx + 1 }
    }

    val testInput = readInput("Day13_test").prepareInput()
    check(part1(testInput) == 13)

    val input = readInput("Day13").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input))
    }
}

private enum class Tri {
    True, False, Equal
}

private class Parser {
    var idx = 0
    var string = ""
    val char: Char get() = string[idx]
    fun parse(string: String): List<Any> {
        idx = 0
        this.string = string
        return parseList()
    }

    private fun parseList(): List<Any> = buildList {
        idx++
        while (char != ']') {
            when {
                char.isDigit() -> add(parseNumber())
                char == '[' -> add(parseList())
                else -> idx++
            }
        }
        idx++
    }

    private fun parseNumber(): Int = buildString {
        while (char.isDigit()) {
            append(char)
            idx++
        }
    }.toInt()
}