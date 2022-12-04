import kotlin.system.measureNanoTime

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        line.split(",")
            .map { part ->
                part.split("-").let { (first, second) ->
                    first.toInt()..second.toInt()
                }
            }
            .let { (first, second) ->
                val (longest, shortest) =
                    if (first.last - first.first > second.last - second.first) first to second
                    else second to first

                if (longest.first <= shortest.first && longest.last >= shortest.last) 1 else 0 as Int
            }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    measureNanoTime {
        part1(input).also {
            check(it == 450)
            print(it)
        }
    }.let { println(" in ${it}ns") }
}