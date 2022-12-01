fun main() {
    fun part1(input: List<String>): Int = input.joinToString(separator = "\n")
        .split("\n\n")
        .maxOf { chunk ->
            chunk.split("\n")
                .map(String::toLong)
                .sum()
                .toInt()
        }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
