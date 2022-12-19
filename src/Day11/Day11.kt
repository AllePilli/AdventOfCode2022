package Day11

import checkAndPrint
import divides
import measureAndPrintTimeMillis
import readInput
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.*

fun main() {
    val itemsRgx = """\s+Starting items:\s+(.*)""".toRegex()
    val operationRgx = """\s+Operation:\s+new = (.*)""".toRegex()
    val testRgx = """\s+Test: divisible by (\d+)""".toRegex()
    val trueRgx = """\s+If true: throw to monkey (\d+)""".toRegex()
    val falseRgx = """\s+If false: throw to monkey (\d+)""".toRegex()

    fun List<String>.prepareInput() = joinToString(separator = "\n")
        .split("\n\n")
        .map { monkeyString ->
            val monkeyLines = monkeyString.split("\n").drop(1)

            val items = itemsRgx.matchEntire(monkeyLines.first())!!.groupValues.last()
                .split(", ")
                .map(String::toBigInteger)
                .let { LinkedList(it) }

            val operation = operationRgx.matchEntire(monkeyLines[1])!!.groupValues.last().let(::Operation)
            val testDivisor = testRgx.matchEntire(monkeyLines[2])!!.groupValues.last().toBigInteger()
            val trueId = trueRgx.matchEntire(monkeyLines[3])!!.groupValues.last().toInt()
            val falseId = falseRgx.matchEntire(monkeyLines[4])!!.groupValues.last().toInt()

            Monkey(
                items as Queue<BigInteger>,
                operation,
                Test(testDivisor, trueId, falseId)
            )
        }

    fun gatherResult(input: List<Monkey>) = input.map(Monkey::inspectCnt)
        .sortedDescending()
        .take(2)
        .let { (first, second) -> first.toBigInteger() * second.toBigInteger() }

    fun part1(input: List<Monkey>): BigInteger {
        val three = BigDecimal(3.0)
        repeat(20) { _ ->
            input.forEach { monkey ->
                while (monkey.items.peek() != null) {
                    val item = monkey.items.poll()!!.let {
                        monkey.operation.perform(it)
                            .toBigDecimal()
                            .divide(three, 0, RoundingMode.DOWN)
                            .toBigInteger()
                    }
                    monkey.inspectCnt++

                    val nextMonkeyId = with(monkey.test) {
                        if (divisor.divides(item)) trueId else falseId
                    }

                    input[nextMonkeyId].items.offer(item)
                }
            }
        }

        return gatherResult(input)
    }

    fun part2(input: List<Monkey>): BigInteger {
        val modulo = input.fold(BigInteger.ONE) { acc, monkey ->
            acc * monkey.test.divisor
        }

        repeat(10000) { _ ->
            input.forEach { monkey ->
                while (monkey.items.peek() != null) {
                    val (item, id) = monkey.operation
                        .performAndTest(monkey.items.poll()!!, monkey.test)

                    monkey.inspectCnt++

                    input[id].items.offer(item.mod(modulo))
                }
            }
        }

        return gatherResult(input)
    }

    check(part1(readInput("Day11_test").prepareInput()) == BigInteger("10605"))
    check(part2(readInput("Day11_test").prepareInput()) == BigInteger("2713310158"))

    measureAndPrintTimeMillis {
        checkAndPrint(part1(readInput("Day11").prepareInput()), BigInteger("108240"))
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(readInput("Day11").prepareInput()), BigInteger("25712998901"))
    }
}

private data class Monkey(val items: Queue<BigInteger>, val operation: Operation, val test: Test) {
    var inspectCnt = 0L
}
private class Test(val divisor: BigInteger, val trueId: Int, val falseId: Int)

private class Operation(operationString: String) {
    private val operationList = operationString.split(" ")
        .let { (left, op, right) ->
            val leftNew = if (left != "old") left.toBigInteger() else left
            val rightNew = if (right != "old") right.toBigInteger() else right
            Triple(leftNew, op, rightNew)
        }

    fun performAndTest(item: BigInteger, test: Test) = operationList.let { (left, op, right) ->
        val leftNum = if (left is BigInteger) left else item
        val rightNum = if (right is BigInteger) right else item
        val div = test.divisor

        when (op) {
            "+" -> {
                val sum = leftNum + rightNum
                sum to if (div.divides(sum)) test.trueId else test.falseId
            }
            "*" -> {
                val product = leftNum * rightNum
                product to if (div.divides(product)) test.trueId else test.falseId
            }
            else -> error("Unknown op $op")
        }
    }

    fun perform(item: BigInteger): BigInteger = operationList.let { (left, op, right) ->
        val leftNum = if (left is BigInteger) left else item
        val rightNum = if (right is BigInteger) right else item

        when (op) {
            "+" -> leftNum + rightNum
            "*" -> leftNum * rightNum
            else -> error("Unknown op $op")
        }
    }
}