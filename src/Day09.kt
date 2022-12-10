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

    fun simulateRope(tailSize: Int, steps: List<Pair<Direction, Int>>): Int {
        val rope = List(tailSize + 1) { Pair(0, 0) }.toMutableList()

        return steps.flatMap { (dir, amt) ->
            buildList {
                repeat(amt) {
                    rope[0] = rope[0].move(dir)

                    for (idx in 1 until rope.size) {
                        if (rope[idx - 1] != rope[idx]
                            && !rope[idx].touches(rope[idx - 1])
                            && !rope[idx].touchesDiagonally(rope[idx - 1])) {
                            rope[idx].directionsTo(rope[idx - 1]).forEach { direction ->
                                rope[idx] = rope[idx].move(direction)
                            }
                        }
                    }

                    add(rope.last())
                }
            }
        }.distinct().size
    }

    fun part1(input: List<Pair<Direction, Int>>): Int = simulateRope(1, input)

    fun part2(input: List<Pair<Direction, Int>>): Int = simulateRope(9, input)

    val testInputPart1 = readInput("Day09_part1_test").prepareInput()
    check(part1(testInputPart1) == 13)
    val testInputPart2 = readInput("Day09_part2_test").prepareInput()
    check(part2(testInputPart2) == 36)

    val input = readInput("Day09").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), 6243)
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(input), 2630)
    }
}