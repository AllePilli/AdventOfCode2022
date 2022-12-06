fun main() {
    fun String.indexOfFirstDistinctSubString(length: Int) = length + windowed(length).indexOfFirst { candidate ->
        candidate.toSet().size == candidate.length
    }

    fun part1(input: String): Int = input.indexOfFirstDistinctSubString(4)

    fun part2(input: String): Int = input.indexOfFirstDistinctSubString(14)

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
        checkAndPrint(part2(input), 3559)
    }
}