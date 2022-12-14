package com.hfreeb.advent.day14

import kotlin.math.sign

fun parse(line: String): MutableSet<Pair<Int, Int>> {
    val occupied = mutableSetOf<Pair<Int, Int>>()

    for (pair in line.split(" -> ").windowed(2)) {
        val (start, end) = pair.map {
            val (x, y) = it.split(",").map { it.toInt() }
            Pair(x, y)
        }

        val deltaX = (end.first - start.first).sign
        val deltaY = (end.second - start.second).sign
        val stop = Pair(end.first + deltaX, end.second + deltaY)
        var pos = start
        while (pos != stop) {
            occupied += pos
            pos = Pair(pos.first + deltaX, pos.second + deltaY)
        }
    }

    return occupied
}

fun solve(input: Set<Pair<Int, Int>>, part2: Boolean): Int {
    val occupied = input.toMutableSet()

    val floor = occupied.maxOf { it.second } + 2
    var sand = Pair(500, 0)
    var total = 0
    while (true) {
        val next = listOf(
            Pair(sand.first, sand.second + 1),
            Pair(sand.first - 1, sand.second + 1),
            Pair(sand.first + 1, sand.second + 1)
        ).firstOrNull { it !in occupied }

        if (!part2 && next != null && next.second == floor) {
            return total
        }

        if (next != null && next.second != floor) {
            sand = next
        } else {
            total++
            occupied += sand

            if (part2 && sand == Pair(500, 0)) {
                return total
            }

            sand = Pair(500, 0)
        }
    }
}

fun part1(input: Set<Pair<Int, Int>>): Int {
    return solve(input, false)
}

fun part2(input: Set<Pair<Int, Int>>): Int {
    return solve(input, true)
}

fun main() {
    val input = ClassLoader.getSystemResource("day14.txt")
        .readText()
        .lines()
        .map(::parse)
        .flatten()
        .toSet()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
