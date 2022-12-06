fun main() {
    fun part1(input: String): Int = 4 + input.windowed(4).indexOfFirst { markerCandidate ->
        markerCandidate.toSet().size == markerCandidate.length
    }

    fun part2(input: String): Int = 14 + input.windowed(14).indexOfFirst { msgCandidate ->
        msgCandidate.toSet().size == msgCandidate.length
    }

    val testInput = readInput("Day06_test")
    testInput.zip(listOf(7, 5, 6, 10, 11)).forEach { (input, output) ->
        check(part1(input) == output)
    }
    testInput.zip(listOf(19, 23, 23, 29, 26)).forEach { (input, output) ->
        check(part2(input) == output)
    }

    val input = readInput("Day06").first()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), 1702)
    }
    measureAndPrintTimeMillis {
        checkAndPrint(part2(input))
    }
}