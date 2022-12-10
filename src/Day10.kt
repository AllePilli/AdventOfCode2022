fun main() {
    fun List<String>.prepareInput(): List<Operation> {
        val addxRgx = """addx (-?\d+)""".toRegex()
        val noopRgx = "noop".toRegex()
        return map { line ->
            addxRgx.matchEntire(line)?.groupValues?.drop(1)?.let { (amt) ->
                Operation.Addx(amt.toInt())
            } ?: noopRgx.matchEntire(line)?.let { Operation.NoOp() }
            ?: error("could not match line $line")
        }
    }

    fun part1(input: List<Operation>): Int {
        var x = 1
        var cycle = 0

        return buildList {
            fun postValue() {
                if (cycle == 20 || (cycle - 20).coerceAtLeast(1) % 40 == 0) add(cycle * x)
            }

            input.forEach { op ->
                repeat(op.completionTime) {
                    cycle++
                    postValue()
                }

                if (op is Operation.Addx) x += op.amt
            }
        }.sum()
    }

    val testInput = readInput("Day10_test").prepareInput()
    check(part1(testInput) == 13140)

    val input = readInput("Day10").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), 13720)
    }
}

private sealed class Operation(val completionTime: Int) {
    class Addx(val amt: Int): Operation(2)
    class NoOp: Operation(1)
}