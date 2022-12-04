import kotlin.system.measureTimeMillis

fun main() {
    fun List<String>.prepareInput(): List<Pair<IntRange, IntRange>> = map { line ->
        line.split(",")
            .map { part ->
                part.split("-")
                    .let { (first, second) -> first.toInt()..second.toInt() }
            }
            .let { (first, second) -> first to second }
    }

    fun part1(input: List<Pair<IntRange, IntRange>>): Int = input.count { (first, second) ->
        val (longest, shortest) =
            if (first.last - first.first > second.last - second.first) first to second
            else second to first

        longest.first <= shortest.first && longest.last >= shortest.last
    }

    fun part2(input: List<Pair<IntRange, IntRange>>): Int = input.count { (first, second) ->
        val spots = first.toSet()
        second.firstOrNull { spot -> spot in spots } != null
    }

    val testInput = readInput("Day04_test").prepareInput()
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04").prepareInput()
    measureTimeMillis {
        part1(input).also {
            check(it == 450)
            print(it)
        }
    }.let { println(" in ${it}ms") }

    measureTimeMillis {
        part2(input).also {
            check(it == 837)
            print(it)
        }
    }.let { println(" in ${it}ms") }
}