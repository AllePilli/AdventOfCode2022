fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        val compartmentSize = line.length / 2
        val first = line.take(compartmentSize)
        val second = line.takeLast(compartmentSize)

        first.first { c -> c in second }
            .priority
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)

    val input = readInput("Day03")
    part1(input).also {
        check(it == 8240)
        println(it)
    }
}

private val Char.priority: Int get() = code - if (isUpperCase()) 38 else 96