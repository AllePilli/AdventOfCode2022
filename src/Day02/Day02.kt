package Day02

import checkAndPrint
import measureAndPrintTimeMillis
import readInput

fun main() {
    fun prepareInput(input: List<String>) = input.map { line ->
        line.split(" ").map(String::first)
    }

    fun part1(input: List<List<Char>>) = input.sumOf { (opponent, player) ->
        matchScore(player.toHand(), opponent.toHand())
    }

    fun part2(input: List<List<Char>>) = input.sumOf { (opponent, outcome) ->
        val opponentHand = opponent.toHand()
        matchScore(outcome.toHand(opponentHand), opponentHand)
    }

    val testInput = prepareInput(readInput("Day02_test"))
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = prepareInput(readInput("Day02"))
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), 15337)
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(input), 11696)
    }
}

private fun matchScore(player: Hand, opponent: Hand) =
    player.score + playerScore(player, opponent)

private fun playerScore(player: Hand, opponent: Hand): Int = when {
    player.score == opponent.score -> 3
    (player.score % 3) + 1 == opponent.score -> 0
    else -> 6
}

private fun Char.toHand(opponent: Hand) = when (this) {
    'X' -> Hand.fromScore((opponent.score - 1).takeUnless { it == 0 } ?: 3)
    'Y' -> Hand.fromScore(opponent.score)
    'Z' -> Hand.fromScore((opponent.score % 3) + 1)
    else -> error("unknown hand: $this")
}

private fun Char.toHand() = when (this) {
    'A', 'X' -> Hand.Rock
    'B', 'Y' -> Hand.Paper
    'C', 'Z' -> Hand.Scissors
    else -> error("unknown hand: $this")
}

private enum class Hand(val score: Int) {
    Rock(1),
    Paper(2),
    Scissors(3);

    companion object {
        fun fromScore(score: Int): Hand = values().find { it.score == score }
            ?: error("No Day02.Hand found for score $score")
    }
}