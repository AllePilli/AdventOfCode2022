fun main() {
    fun List<String>.prepareInput() = map { line ->
        line.split(" ")
            .let { (first, second) ->
                val direction = when (val dir = first.first()) {
                    'U' -> Direction.Up
                    'R' -> Direction.Right
                    'D' -> Direction.Down
                    'L' -> Direction.Left
                    else -> error("unknown direction $dir")
                }
                direction to second.toInt()
            }
    }

    fun part1(input: List<Pair<Direction, Int>>): Int {
        var posH = Pair(0, 0)
        var posT = Pair(0, 0)

        fun Pair<Int, Int>.move(direction: Direction) = when (direction) {
            Direction.Up -> copy(second = second + 1)
            Direction.Right -> copy(first = first + 1)
            Direction.Down -> copy(second = second - 1)
            Direction.Left -> copy(first = first - 1)
        }

        return input.flatMap { (dir, amt) ->
            buildList {
                repeat(amt) {
                    val nextH = posH.move(dir)

                    if (nextH != posT && !posT.touches(nextH) && !posT.touchesDiagonally(nextH)) {
                        posT = posH.copy()
                    }
                    posH = nextH
                    add(posT)
                }
            }
        }.distinct().size
    }

    val testInput = readInput("Day09_test").prepareInput()
    check(part1(testInput) == 13)

    val input = readInput("Day09").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), 6243)
    }
}