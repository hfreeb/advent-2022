package com.hfreeb.advent.day15

import kotlin.math.abs
import kotlin.math.max

fun generateRanges(input: List<List<Int>>, y: Int): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    for (sensor in input) {
        val manhattan = abs(sensor[2] - sensor[0]) + abs(sensor[3] - sensor[1])
        val distance = abs(sensor[1] - y)
        if (distance <= manhattan) {
            val side = manhattan - distance
            val range = IntRange(sensor[0] - side, sensor[0] + side)
            ranges += range
        }
    }

    return ranges
}

fun part1(input: List<List<Int>>): Int {
    val ranges = generateRanges(input, 2000000)

    var total = 0
    var last: Int? = null
    for (range in ranges.sortedWith(compareBy({ it.first }, { it.last }))) {
        if (last == null) {
            last = range.first - 1
        }

        val start = max(range.first, last + 1)

        if (start <= range.last) {
            total += range.last - start + 1
            last = range.last
        }
    }

    // I'm cheeky and just assume 1 beacon in our y-level
    return total - 1
}

fun part2(input: List<List<Int>>): Long {
    for (y in 0 until 4000000) {
        val ranges = generateRanges(input, y)

        var last = -1
        for (range in ranges.sortedWith(compareBy({ it.first }, { it.last }))) {
            val start = max(range.first, last + 1)
            if (start > last + 1) {
                return (last+1).toLong() * 4000000L +y
            }

            if (start <= range.last) {
                last = range.last
            }
        }
    }

    return -1
}

fun parse(line: String): List<Int> {
    val regex = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
    val match = regex.matchEntire(line)!!
    return match.groupValues.drop(1).map { it.toInt() }
}

fun main() {
    val input = ClassLoader.getSystemResource("day15.txt")
        .readText().trim()
        .lines()
        .map(::parse)

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
