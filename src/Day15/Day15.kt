package Day15

import checkAndPrint
import discreteDistanceTo
import measureAndPrintTimeMillis
import readInput
import kotlin.math.abs
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

    fun Pair<Int, Int>.noPossibleBeacon(beacons: List<Beacon>, sensorRadi: List<Pair<Sensor, Int>>): Boolean {
        return if (beacons.contains(Beacon(this))) false
        else sensorRadi.any { (sensor, radiusToBeacon) ->
            val distance = this.discreteDistanceTo(sensor.coordinate)
            distance <= radiusToBeacon
        }
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
            .count { currentPos -> currentPos.noPossibleBeacon(beacons, sensorsRadi) }
    }

    fun part2(maxRange: Int, input: List<Pair<Sensor, Beacon>>): Long {
        val freqMultiplier = 4000000L
        val sensorsRadi = input.map { (sensor, beacon) ->
            sensor to sensor.coordinate.discreteDistanceTo(beacon.coordinate)
        }

        var x: Int
        var y = 0
        while (y <= maxRange) {
            x = 0

            while (x <= maxRange) {
                val pos = x to y

                val (sensor, radius) = sensorsRadi
                    .find { (sensor, radius) -> sensor.coordinate.discreteDistanceTo(pos) <= radius }
                    ?: return freqMultiplier * x.toLong() + y.toLong()

                x += if (pos.first <= sensor.coordinate.first) {
                    val dx = sensor.coordinate.first - pos.first
                    dx + (radius - abs(pos.second - sensor.coordinate.second)) + 1
                } else {
                    val dist = pos.first - sensor.coordinate.first
                    radius - dist + 1
                }
            }

            y++
        }

        throw IllegalStateException("No position found")
    }

    val testInput = readInput("Day15_test").prepareInput()
    check(part1(10, testInput) == 26)
    check(part2(20, testInput) == 56000011L)

    val input = readInput("Day15").prepareInput()
    measureAndPrintTimeMillis {
        checkAndPrint(part1(2000000, input), 5100463)
    }

    measureAndPrintTimeMillis {
        checkAndPrint(part2(4000000, input), 11557863040754L)
    }
}

@JvmInline
private value class Sensor(val coordinate: Pair<Int, Int>)
@JvmInline
private value class Beacon(val coordinate: Pair<Int, Int>)