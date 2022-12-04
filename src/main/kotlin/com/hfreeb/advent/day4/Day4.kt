package com.hfreeb.advent.day4

fun part1(input: List<List<List<Int>>>): Int {
    var total = 0
    for (line in input) {
        val (a, b) = line

        if (a[0] <= b[0] && a[1] >= b[1] || b[0] <= a[0] && b[1] >= a[1]) {
            total += 1
        }
    }
    return total
}

fun part2(input: List<List<List<Int>>>): Int {
    var total = 0
    for (line in input) {
        val (a, b) = line

        if (b[0] >= a[0] && b[0] <= a[1] || a[0] >= b[0] && a[0] <= b[1]) {
            total += 1
        }
    }
    return total
}

fun main() {
    val input = ClassLoader.getSystemResource("day4.txt")
        .readText().trim()
        .lines()
        .map { line ->
            line.split(",").map { range ->
                range.split("-").map { it.toInt() }
            }
        }

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
