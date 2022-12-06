fun main() {
    fun part1(input: String): Int = 4 + input.windowed(4).indexOfFirst { markerCandidate ->
        markerCandidate.toSet().size == markerCandidate.length
    }

    val testInput = readInput("Day06_test")
    testInput.zip(listOf(7, 5, 6, 10, 11)).forEach { (input, output) ->
        check(part1(input) == output)
    }

    val input = readInput("Day06").first()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input))
    }
}