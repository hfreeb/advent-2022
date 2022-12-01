package com.hfreeb.advent.day1

val input = ClassLoader.getSystemResource("day1.txt")
    .readText()
    .split("\n\n")
    .map { it.trim().lines().map(String::toInt) }

fun part1(input: List<List<Int>>): Int {
    return input.maxOf { it.sum() }
}

fun part2(input: List<List<Int>>): Int {
    return input.map { it.sum() }
        .sorted().takeLast(3)
        .sum()
}

fun main() {
    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
