package Day15

import checkAndPrint
import discreteDistanceTo
import measureAndPrintTimeMillis
import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    val lineRgx = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

    fun List<String>.prepareInput() = map { line ->
        val coordinates = lineRgx.matchEntire(line)?.groupValues
            ?.drop(1)
            ?.map(String::toInt)
            ?: throw IllegalArgumentException("line: $line")

        Pair(
            Sensor(coordinates[0] to coordinates[1]),
            Beacon(coordinates[2] to coordinates[3])
        )
    }

    fun part1(row: Int, input: List<Pair<Sensor, Beacon>>): Int {
        val sensors = input.map { it.first }
        val beacons = input.map { it.second }.distinct()
        val minX = min(sensors.minOf { it.coordinate.first }, beacons.minOf { it.coordinate.first })
        val maxX = max(sensors.maxOf { it.coordinate.first }, beacons.maxOf { it.coordinate.first })

        val sensorsRadi = input.map { (sensor, beacon) ->
            sensor to sensor.coordinate.discreteDistanceTo(beacon.coordinate)
        }

        val maxRadius = sensorsRadi.maxOf { it.second }

        return ((minX - maxRadius)..(maxX + maxRadius)).map { x -> x to row }
            .count { currentPos ->
                if (beacons.contains(Beacon(currentPos))) false
                else sensorsRadi.any { (sensor, radiusToBeacon) ->
                    val distance = currentPos.discreteDistanceTo(sensor.coordinate)
                    distance <= radiusToBeacon
                }
            }
    }

    val testInput = readInput("Day15_test").prepareInput()
    check(part1(10, testInput) == 26)

    val input = readInput("Day15").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(2000000, input), 5100463)
    }
}

@JvmInline
private value class Sensor(val coordinate: Pair<Int, Int>)
@JvmInline
private value class Beacon(val coordinate: Pair<Int, Int>)