package com.hfreeb.advent.day3

fun getValue(c: Char): Int {
    return when (c) {
        in 'a'..'z' -> c - 'a' + 1
        in 'A'..'Z' -> c - 'A' + 27
        else -> throw IllegalArgumentException("Illegal char $c")
    }
}

fun part1(input: List<String>): Int {
    var total = 0

    for (rucksack in input) {
        val (compartment1, compartment2) = rucksack
            .chunked(rucksack.length / 2)
            .map { it.toSet() }

        for (char in compartment2) {
            if (char in compartment1) {
                total += getValue(char)
            }
        }
    }

    return total
}

fun part2(input: List<String>): Int {
    var total = 0

    for (group in input.chunked(3)) {
        var common = group[0].toSet()

        for (elf in group.subList(1, group.size)) {
            common = common.intersect(elf.toSet())
        }

        total += getValue(common.first())
    }

    return total
}

fun main() {
    val input = ClassLoader.getSystemResource("day3.txt")
        .readText().trim()
        .lines()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
