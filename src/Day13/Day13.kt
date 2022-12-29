package Day13

import checkAndPrint
import measureAndPrintTimeMillis
import readInput

fun main() {
    val printComparisons = false
    val parser = Parser()

    fun List<String>.prepareInputPart1() = (this + "").chunked(3)
        .map { it.dropLast(1) }
        .map { (left, right) -> left to right }

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

    fun compare(pair: Pair<List<Any>, List<Any>>): Boolean {
        if (printComparisons) println("- Compare ${pair.first} vs ${pair.second}")
        return compare(pair.first, pair.second).also { if (printComparisons) println() } == Tri.True
    }

    fun part1(input: List<Pair<String, String>>): Int = input
        .map { (left, right) -> parser.parse(left) to parser.parse(right) }
        .withIndex()
        .filter { (_, pair) -> compare(pair) }
        .sumOf { (idx, _) -> idx + 1 }

    fun part2(input: List<String>): Int {
        val dividers = listOf("[[2]]", "[[6]]")
        val orderedPackets = input.toMutableList()
            .also { it.addAll(dividers) }
            .filterNot { it.isBlank() || it.isEmpty() }
            .map(parser::parse)
            .sortedWith { o1, o2 ->
                when (compare(o1!!, o2!!)) {
                    Tri.True -> 1
                    Tri.False -> -1
                    Tri.Equal -> 0
                }
            }
            .reversed()
            .map(List<Any>::toString)

        return dividers.fold(1) { acc, div -> acc * (orderedPackets.indexOf(div) + 1) }
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput.prepareInputPart1()) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input.prepareInputPart1()), 5843)
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(input), 26289)
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