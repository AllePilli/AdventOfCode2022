fun main() {
    fun part1(input: List<String>): Int = input.size

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
}