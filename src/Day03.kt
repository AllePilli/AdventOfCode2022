import kotlin.system.measureNanoTime

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
    measureNanoTime {
        part1(input).also {
            check(it == 8240)
            print(it)
        }
    }.let { println(" in ${it}ns") }

    measureNanoTime {
        part2(input).also {
            check(it == 2587)
            print(it)
        }
    }.let { println(" in ${it}ns") }
}

private val Char.priority: Int get() = code - if (isUpperCase()) 38 else 96