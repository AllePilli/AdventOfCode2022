package Day12

import checkAndPrint
import get
import getDirectNeighbouringIndices
import indicesOf
import measureAndPrintTimeMillis
import readInput
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    fun prepareInput(input: List<String>): List<List<Int>> = input.map { line ->
        line.map { c -> c.code }
    }

    fun part1(input: List<String>): BigInteger {
        val grid = prepareInput(input)
        val graph = Graph()

        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val currPos = i to j
                val currHeight = when (val height = grid[currPos]) {
                    'S'.code -> 'a'.code
                    'E'.code -> 'z'.code
                    else -> height
                }

                grid.getDirectNeighbouringIndices(currPos)
                    .associateWith { neighbourPos ->
                        when (val height = grid[neighbourPos]) {
                            'S'.code -> 'a'.code
                            'E'.code -> 'z'.code
                            else -> height
                        }
                    }
                    .filterValues { neighbourHeight -> neighbourHeight <= currHeight || neighbourHeight == currHeight + 1 }
                    .forEach { graph.addDirectedEdge(currPos, it.key) }
            }
        }

        val startPos = grid.indicesOf('S'.code)
        val endPos = grid.indicesOf('E'.code)

        return graph.dijkstra(startPos, endPos)
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == BigInteger("31"))

    val input = readInput("Day12")
    measureAndPrintTimeMillis {
        checkAndPrint(part1(input), BigInteger("497"))
    }
}

private class Graph {
    val adjacencyMap: HashMap<Pair<Int, Int>, HashSet<Pair<Int, Int>>> = HashMap()
    val weightMap: HashMap<Pair<Int, Int>, HashMap<Pair<Int, Int>, BigInteger>> = HashMap()

    fun addDirectedEdge(srcVertex: Pair<Int, Int>, destVertex: Pair<Int, Int>) {
        adjacencyMap
            .computeIfAbsent(srcVertex) { HashSet() }
            .add(destVertex)

        weightMap.computeIfAbsent(srcVertex) { HashMap() }[destVertex] = BigInteger.ONE
    }

    fun dijkstra(srcVertex: Pair<Int, Int>, destVertex: Pair<Int, Int>): BigInteger {
        val inf = Int.MAX_VALUE.toBigInteger()
        val shortestDistances = adjacencyMap.keys
            .associateWith { inf }
            .toMutableMap()
            .apply { this[srcVertex] = BigInteger.ZERO }
        val unvisitedVertices = adjacencyMap.keys.toList().toMutableSet()
        var currentVertex: Pair<Int, Int>? = srcVertex

        while (unvisitedVertices.isNotEmpty()) {
            unvisitedVertices.remove(currentVertex)

            adjacencyMap[currentVertex]!!
                .filter { it in unvisitedVertices }
                .forEach { v ->
                    val distance = shortestDistances[v]!!
                    val currDistance = shortestDistances[currentVertex]!! + weightMap[currentVertex]!![v]!!
                    if (currDistance < distance) {
                        shortestDistances[v] = currDistance
                    }
                }

            currentVertex = unvisitedVertices
                .takeUnless { it.isEmpty() }
                ?.minBy { shortestDistances[it]!! }
        }

        return shortestDistances[destVertex]!!
    }
}