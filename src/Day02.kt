fun main() {
    fun part1(input: List<String>) = input
        .map { line ->
            line.split(" ")
                .map { it.first().toHand() }
        }.sumOf { (opponent, player) ->
            player.score.toLong() + playerScore(player, opponent)
        }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15L)

    val input = readInput("Day02")
    println(part1(input))
}

private fun playerScore(player: Hand, opponent: Hand): Int = when {
    player.score == opponent.score -> 3
    (player.score % 3) + 1 == opponent.score -> 0
    else -> 6
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
}