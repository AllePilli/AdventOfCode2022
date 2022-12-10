import kotlin.math.max

fun main() {
    fun List<String>.prepareInput() = map { line -> line.map(Char::digitToInt) }

    fun part1(input: List<List<Int>>): Int {
        fun isVisible(rowIdx: Int, colIdx: Int): Boolean {
            val treeHeight = input[rowIdx][colIdx]
            val row = input[rowIdx]
            val col = input.getColumn(colIdx, -1)

            return rowIdx == 0 || rowIdx == input.size - 1 || colIdx == 0 || colIdx == input.first().size - 1
                    || col.take(rowIdx).all { it < treeHeight } || col.takeLast(row.size - 1 - rowIdx).all { it < treeHeight }
                    || row.take(colIdx).all { it < treeHeight } || row.takeLast(col.size - 1 - colIdx).all { it < treeHeight }
        }

        return input.indices.sumOf { rowIdx ->
            input.first().indices.count { colIdx ->
                isVisible(rowIdx, colIdx)
            }
        }
    }

    val testInput = readInput("Day08_test").prepareInput()
    check(part1(testInput) == 21)

    val input = readInput("Day08").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), 1776)
    }
}