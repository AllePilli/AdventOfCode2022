package Day14

import checkAndPrint
import measureAndPrintTimeMillis
import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
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
        var currSand = 500 to 0
        val maxRocksY = input.maxOf { (x, y) -> y }

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
            currSand = 500 to 0
        }

        return sandAtRest.size
    }

    val testInput = readInput("Day14_test").prepareInput()
    check(part1(testInput) == 24)

    val input = readInput("Day14").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input))
    }
}