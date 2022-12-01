fun main() {
    fun caloriesPerElves(input: List<String>) = input.joinToString(separator = "\n")
        .split("\n\n")
        .map { chunk ->
            chunk.split("\n")
                .sumOf(String::toInt)
        }

    fun part1(input: List<String>): Int = caloriesPerElves(input).max()

    fun part2(input: List<String>): Int = caloriesPerElves(input)
        .sortedDescending()
        .take(3)
        .sum()

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
