package Day11

import checkAndPrint
import divides
import measureAndPrintTimeMillis
import multOf
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

    fun part1(input: List<Monkey>): Int {
        repeat(20) { round ->
            input.forEach { monkey ->
                while (monkey.items.peek() != null) {
                    val item = monkey.items.poll()!!.let {
                        monkey.operation.perform(it)
                            .toBigDecimal()
                            .divide(
                                BigDecimal(3.0),
                                0,
                                RoundingMode.DOWN
                            ).toBigInteger()
                    }
                    monkey.inspectCnt++

                    val nextMonkeyId = with(monkey.test) {
                        if (item.mod(divisor) == BigInteger.ZERO) trueId else falseId
                    }

                    input[nextMonkeyId].items.offer(item)
                }
            }
        }

        return input.sortedByDescending { it.inspectCnt }
            .take(2)
            .multOf { it.inspectCnt.toInt() }
    }

    fun part2(input: List<Monkey>): BigInteger {
        val modulo = input.fold(BigInteger.ONE) { acc, monkey ->
            acc * monkey.test.divisor
        }

        repeat(10000) { _ ->
            input.forEach { monkey ->
                while (monkey.items.peek() != null) {
                    val (item, id) = monkey.items.poll()!!.let {
                        monkey.operation.performAndTest(it, monkey.test)
                    }

                    monkey.inspectCnt++

                    input[id].items.offer(item.mod(modulo))
                }
            }
        }

        return input.map { it.inspectCnt }
            .sortedDescending()
            .take(2)
            .fold(BigInteger.ONE) { acc, l -> acc * l.toBigInteger() }
    }

    check(part1(readInput("Day11_test").prepareInput()) == 10605)
    check(part2(readInput("Day11_test").prepareInput()) == BigInteger("2713310158"))

    measureAndPrintTimeMillis {
        checkAndPrint(part1(readInput("Day11").prepareInput()), 108240)
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(readInput("Day11").prepareInput()), BigInteger("25712998901"))
    }
}

private data class Monkey(val items: Queue<BigInteger>, val operation: Operation, val test: Test) {
    var inspectCnt = 0L
}
private data class Test(val divisor: BigInteger, val trueId: Int, val falseId: Int)

private data class Operation(val operationString: String) {
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