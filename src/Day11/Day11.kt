package Day11

import checkAndPrint
import measureAndPrintTimeMillis
import multOf
import readInput
import java.util.*
import kotlin.math.floor

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
                .map(String::toInt)
                .let { LinkedList(it) }

            val operation = operationRgx.matchEntire(monkeyLines[1])!!.groupValues.last().let(::Operation)
            val testDivisor = testRgx.matchEntire(monkeyLines[2])!!.groupValues.last().toInt()
            val trueId = trueRgx.matchEntire(monkeyLines[3])!!.groupValues.last().toInt()
            val falseId = falseRgx.matchEntire(monkeyLines[4])!!.groupValues.last().toInt()

            Monkey(
                items as Queue<Int>,
                operation,
                Test(testDivisor, trueId, falseId)
            )
        }

    fun part1(input: List<Monkey>): Int {
        repeat(20) {
            input.forEach { monkey ->
                while (monkey.items.peek() != null) {
                    val item = monkey.items.poll()!!.let {
                        monkey.operation.perform(it)
                            .let { newWorryLevel -> floor(newWorryLevel.toDouble() / 3.0).toInt() }
                    }
                    monkey.inspectCnt++

                    val nextMonkeyId = with(monkey.test) {
                        if (item % divisor == 0) trueId else falseId
                    }

                    input[nextMonkeyId].items.offer(item)
                }
            }
        }

        return input.sortedByDescending { it.inspectCnt }
            .take(2)
            .multOf { it.inspectCnt }
    }

    val testInput = readInput("Day11_test").prepareInput()
    check(part1(testInput) == 10605)

    val input = readInput("Day11").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input))
    }
}

private data class Monkey(val items: Queue<Int>, val operation: Operation, val test: Test) {
    var inspectCnt = 0
}

private data class Test(val divisor: Int, val trueId: Int, val falseId: Int)

private data class Operation(/*val op: Char, val value: Int, */val operationString: String) {
    fun perform(item: Int): Int = Parser(operationString.replace("old", "$item")).parse().run()
}

private class Parser(text: String) {
    private val tokens: List<Lexer.Token> = Lexer(text).lex().filterNot { it is Lexer.Token.WhiteSpaceToken }
    private var idx = 0

    fun parse(): Expression.BinaryExpression {
        idx = 0
        return Expression.BinaryExpression(
            parseExpression(),
            tokens[idx].also { idx++ } as Lexer.Token.OperatorToken,
            parseExpression()
        )
    }

    private fun parseExpression(): Expression = when (val token = tokens[idx]) {
        is Lexer.Token.NumberToken -> Expression.LiteralExpression(token)
        is Lexer.Token.NameToken -> Expression.NameExpression(token)
        else -> error("unreachable")
    }.also { idx++ }

    sealed class Expression {
        class LiteralExpression(val token: Lexer.Token.NumberToken): Expression()
        class NameExpression(val nameToken: Lexer.Token.NameToken): Expression()

        class BinaryExpression(
            val left: Expression,
            val operator: Lexer.Token.OperatorToken,
            val right: Expression
        ) : Expression() {
            fun run(): Int = when (operator.text) {
                "+" -> (left as LiteralExpression).token.text.toInt() + (right as LiteralExpression).token.text.toInt()
                "*" -> (left as LiteralExpression).token.text.toInt() * (right as LiteralExpression).token.text.toInt()
                else -> error("unknown operator $operator")
            }
        }
    }
}

private class Lexer(val text: String) {
    private var idx = 0

    fun lex(): List<Token> = buildList {
        idx = 0
        while (idx in text.indices) {
            add(lexToken())
        }
    }

    private fun lexToken(): Token {
        val c = text[idx]

        return when {
            c.isDigit() -> Token.NumberToken(getWord())
            c.isLetter() -> Token.NameToken(getWord())
            c == '+' || c == '*' -> Token.OperatorToken(c.toString()).also { idx++ }
            c.isWhitespace() -> Token.WhiteSpaceToken(
                buildString {
                    while (idx in text.indices && text[idx].isWhitespace()) {
                        append(text[idx])
                        idx++
                    }
                }
            )
            else -> error("unknown char $c")
        }
    }

    private fun getWord(): String = buildString {
        while (idx in text.indices && text[idx].isLetterOrDigit()) {
            append(text[idx])
            idx++
        }
    }

    sealed class Token(val text: String) {
        class NumberToken(text: String) : Token(text)
        class OperatorToken(text: String) : Token(text)
        class NameToken(text: String) : Token(text)

        class WhiteSpaceToken(text: String) : Token(text)
    }
}

//private data class Item(val worryLevel: Int)