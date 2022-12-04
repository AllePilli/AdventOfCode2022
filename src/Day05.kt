fun main() {
    fun part1(input: List<String>): Int = input.size

    val inputTest = readInput("Day05_test")

    val input = readInput("Day05")
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input))
    }
}