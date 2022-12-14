package Day05

import checkAndPrint
import measureAndPrintTimeMillis
import readInput
import java.util.ArrayDeque

@ExperimentalStdlibApi
fun main() {
    val moveRgx = """move (\d+) from (\d) to (\d)""".toRegex()

    fun getStartingConfig(input: List<String>): List<ArrayDeque<Char>> {
        val digitLineIdx = input.indexOfFirst { it[1].isDigit() }
        val stackAmt = input.first { it[1].isDigit() }
            .last(Char::isDigit)
            .digitToInt()

        val charAmtInRow = 4 * stackAmt - 1

        return (0 ..< stackAmt).map { col ->
            val charCol = 1 + col * 4

            ArrayDeque<Char>().apply {
                (0 ..< digitLineIdx).mapNotNull { row ->
                    val line = input[row].takeIf { it.length == charAmtInRow }
                        ?: input[row].let { it + " ".repeat(charAmtInRow - it.length) }
                    line[charCol].takeIf { it.isLetter() }
                }.asReversed().forEach(::push)
            }
        }
    }

    fun getMoves(input: List<String>): List<Triple<Int, Int, Int>> = input
        .indexOfFirst { it.isEmpty() || it.isBlank() }
        .let { input.drop(it + 1) }
        .map { moveLine ->
            moveRgx.find(moveLine)!!.groupValues
                .drop(1)
                .let { (amt, col, dest) ->
                    Triple(amt.toInt(), col.toInt() - 1, dest.toInt() - 1)
                }
        }

    fun part1(input: List<String>): String {
        val config = getStartingConfig(input)
        val moves = getMoves(input)

        moves.forEach { (amt, start, dest) ->
            for (i in 0 ..< amt) {
                config[dest].push(config[start].pop())
            }
        }

        return config.joinToString(separator = "") { it.peek()!!.toString() }
    }

    fun part2(input: List<String>): String {
        val config = getStartingConfig(input)
        val moves = getMoves(input)

        moves.forEach { (amt, start, dest) ->
            (0 ..< amt).map { config[start].pop() }
                .asReversed()
                .forEach { config[dest].push(it) }
        }

        return config.joinToString(separator = "") { it.peek()!!.toString() }
    }

    val inputTest = readInput("Day05_test")
    check(part1(inputTest) == "CMZ")
    check(part2(inputTest) == "MCD")

    val input = readInput("Day05")
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), "VQZNJMWTR")
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(input), "NLCDCLVMQ")
    }
}