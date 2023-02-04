package Day15

import checkAndPrint
import discreteDistanceTo
import measureAndPrintTimeMillis
import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lineRgx = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

    // Return a list of sensors and a list of beacons
    fun List<String>.prepareInput() = map { line ->
        val coordinates = lineRgx.matchEntire(line)?.groupValues
            ?.drop(1)
            ?.map(String::toInt)
            ?: throw IllegalArgumentException("line: $line")

        Pair(
            Pair(coordinates[0], coordinates[1]),
            Pair(coordinates[2], coordinates[3]),
        )
    }.let {
        it.map { pair -> pair.first } to
                it.map { pair -> pair.second }.distinct()
    }

    fun part1(row: Int, sensors: List<Pair<Int, Int>>, beacons: List<Pair<Int, Int>>): Int {
        val minX = min(sensors.minOf { it.first }, beacons.minOf { it.first })
        val maxX = max(sensors.maxOf { it.first }, beacons.maxOf { it.first })

        val sensorsRadi = sensors.map { sensor ->
            sensor to beacons.minOf { beacon -> sensor.discreteDistanceTo(beacon) }
        }

        val maxRadius = sensorsRadi.maxOf { it.second }

        return ((minX - maxRadius)..(maxX + maxRadius)).map { x -> x to row }
            .count { currentPos ->
                if (beacons.contains(currentPos)) false
                else sensorsRadi.any { (sensor, radiusToBeacon) ->
                    val distance = currentPos.discreteDistanceTo(sensor)
                    distance <= radiusToBeacon
                }
            }
    }

    val testInput = readInput("Day15_test").prepareInput()
    check(part1(10, testInput.first, testInput.second) == 26)

    val input = readInput("Day15").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(2000000, input.first, input.second), 5100463)
    }
}