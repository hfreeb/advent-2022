package com.hfreeb.advent.day6

fun solve(str: String, markerLength: Int): Int {
    for ((i, window) in str.windowed(markerLength).withIndex()) {
        if (window.toSet().size == markerLength) {
            return i + markerLength
        }
    }

    return -1
}

fun part1(input: String): Int {
    return solve(input, 4)
}

fun part2(input: String): Int {
    return solve(input, 14)
}

fun main() {
    val input = ClassLoader.getSystemResource("day6.txt")
        .readText().trim()
        .lines()
        .single()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
