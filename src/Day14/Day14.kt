package Day14

import TerminalUtils
import checkAndPrint
import measureAndPrintTimeMillis
import minMaxRange
import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val showAnimationInTerminal = false
    val startPosition = 500 to 0

    fun List<String>.prepareInput() = flatMap { line ->
        line.split(" -> ")
            .map { pair ->
                pair.split(",")
                    .let { (first, second) ->
                        first.toInt() to second.toInt()
                    }
            }
            .zipWithNext { first, second ->
                buildList {
                    if (first.first == second.first) {
                        for (y in min(first.second, second.second)..max(first.second, second.second)) {
                            add(first.first to y)
                        }
                    } else {
                        for (x in min(first.first, second.first)..max(first.first, second.first)) {
                            add(x to first.second)
                        }
                    }
                }
            }
            .flatten()
    }.toSet()

    fun part1(input: Set<Pair<Int, Int>>): Int {
        val sandAtRest: MutableList<Pair<Int, Int>> = mutableListOf()
        var currSand = startPosition.copy()
        val maxRocksY = input.maxOf { (_, y) -> y }

        fun positionFree(position: Pair<Int, Int>): Boolean = position !in input && position !in sandAtRest

        while (true) {
            val down = currSand.copy(second = currSand.second + 1)
            if (positionFree(down)) {
                if (down.second > maxRocksY) break
                currSand = down
                continue
            }

            val diagLeft = down.copy(first = down.first - 1)
            if (positionFree(diagLeft)) {
                currSand = diagLeft
                continue
            }

            val diagRight = down.copy(first = down.first + 1)
            if (positionFree(diagRight)) {
                currSand = diagRight
                continue
            }

            sandAtRest += currSand.copy()
            currSand = startPosition.copy()
        }

        return sandAtRest.size
    }

    fun part2(input: Set<Pair<Int, Int>>): Int {
        val sandAtRest: MutableList<Pair<Int, Int>> = mutableListOf()
        var currSand = startPosition.copy()
        val maxRocksY = input.maxOf { (_, y) -> y }
        val rocksXRange = input.minMaxRange { (x, _) -> x }
        val floor = ((rocksXRange.first - maxRocksY / 2)..(rocksXRange.last + maxRocksY / 2))
            .map { floorX -> floorX to (maxRocksY + 2) }
            .toMutableList()

        fun positionFree(position: Pair<Int, Int>): Boolean {
            return position.second < maxRocksY + 2 && position !in input && position !in sandAtRest

//            return if (position.second == maxRocksY + 2) false
//            else position !in input && position !in sandAtRest
        }

        if (showAnimationInTerminal) TerminalUtils.hideTerminalCursor()
        while (sandAtRest.lastOrNull() != startPosition) {
            if (showAnimationInTerminal) {
                TerminalUtils.clearTerminal()
                for (x in 0..11) {
                    for (y in 488..512) {
                        when (y to x) {
                            in sandAtRest -> print("o")
                            in input, in floor -> print("#")
                            currSand -> print("+")
                            else -> print(".")
                        }
                    }
                    println()
                }
                Thread.sleep(500)
            }

            val down = currSand.copy(second = currSand.second + 1)
            if (positionFree(down)) {
                currSand = down
                continue
            }

            val diagLeft = down.copy(first = down.first - 1)
            if (positionFree(diagLeft)) {
                currSand = diagLeft
                continue
            }

            val diagRight = down.copy(first = down.first + 1)
            if (positionFree(diagRight)) {
                currSand = diagRight
                continue
            }

            sandAtRest += currSand.copy()
            currSand = startPosition.copy()
        }
        if (showAnimationInTerminal) TerminalUtils.restoreTerminalCursor()

        return sandAtRest.size
    }

    val testInput = readInput("Day14_test").prepareInput()
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), 964)
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(input))
    }
}