package Day03

import checkAndPrint
import measureAndPrintTimeNano
import readInput

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        val compartmentSize = line.length / 2
        val first = line.take(compartmentSize)
        val second = line.takeLast(compartmentSize)

        first.first { c -> c in second }
            .priority
    }

    fun part2(input: List<String>): Int = input.chunked(3).sumOf { group ->
        group.first()
            .first { c -> c in group[1] && c in group.last() }
            .priority
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    measureAndPrintTimeNano {
        checkAndPrint(part1(input), 8240)
    }

    measureAndPrintTimeNano {
        checkAndPrint(part2(input), 2587)
    }
}

private val Char.priority: Int get() = code - if (isUpperCase()) 38 else 96