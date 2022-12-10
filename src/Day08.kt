fun main() {
    fun List<String>.prepareInput() = map { line -> line.map(Char::digitToInt) }

    fun part1(input: List<List<Int>>): Int {
        fun isVisible(rowIdx: Int, colIdx: Int): Boolean {
            val treeHeight = input[rowIdx][colIdx]
            return input.onEdge(rowIdx, colIdx) || input.getElementsUntilEdges(rowIdx, colIdx).any { branch ->
                branch.all { it < treeHeight }
            }
        }

        return input.indices.sumOf { rowIdx ->
            input.first().indices.count { colIdx ->
                isVisible(rowIdx, colIdx)
            }
        }
    }

    fun part2(input: List<List<Int>>): Int {
        fun List<Int>.scenicScore(treeHeight: Int): Int = (takeWhile { it < treeHeight }.size + 1).coerceAtMost(size)

        return input.indices.maxOf { rowIdx ->
            input.first().indices.maxOf { colIdx ->
                if (input.onEdge(rowIdx, colIdx)) 0 else {
                    val treeHeight = input[rowIdx][colIdx]
                    input.getElementsUntilEdges(rowIdx, colIdx).multOf { it.scenicScore(treeHeight) }
                }
            }
        }
    }

    val testInput = readInput("Day08_test").prepareInput()
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), 1776)
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(input), 234416)
    }
}