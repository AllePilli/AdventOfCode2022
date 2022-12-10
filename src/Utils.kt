import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun measureAndPrintTimeMillis(block: () -> Unit) = println(" in ${measureTimeMillis(block)}ms")

fun measureAndPrintTimeNano(block: () -> Unit) = println(" in ${measureNanoTime(block)}ns")

fun <T> checkAndPrint(actual: T, expected: T? = null) {
    if (expected != null) check(actual == expected)
    print(actual)
}

fun <T> List<List<T>>.getColumn(colIdx: Int): List<T> = firstOrNull()
    ?.indices
    ?.map { rowIdx ->
        getOrNull(rowIdx)!!.getOrNull(colIdx)!!
    }
    ?: emptyList()

fun <T> List<List<T>>.getColumn(colIdx: Int, defaultValue: T) = firstOrNull()
    ?.indices
    ?.map { rowIdx ->
        getOrNull(rowIdx)?.getOrNull(colIdx) ?: defaultValue
    }
    ?: emptyList()

fun <T> List<List<T>>.transpose(): List<List<T>> = List(first().size) { col ->
    List(size) { row ->
        this[row][col]
    }
}

inline fun <T> Iterable<T>.multOf(selector: (T) -> Int): Int {
    var product = 1
    for (element in this) {
        product *= selector(element)
    }
    return product
}

fun <T> List<List<T>>.onEdge(idx1: Int, idx2: Int): Boolean =
    idx1 == 0 || idx1 == size - 1 || idx2 == 0 || idx2 == get(idx1).size - 1

fun <T> List<List<T>>.getElementsUntilEdges(idx1: Int, idx2: Int): List<List<T>> = buildList {
    val row = this@getElementsUntilEdges[idx1]
    val col = this@getElementsUntilEdges.getColumn(idx2)

    // In clockwise direction starting from top
    add(col.take(idx1).asReversed())
    add(row.takeLast(col.size - 1 - idx2))
    add(col.takeLast(row.size - 1 - idx1))
    add(row.take(idx2).asReversed())
}
