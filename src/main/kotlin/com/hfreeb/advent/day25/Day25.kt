package com.hfreeb.advent.day25

fun part1(input: List<String>): String {
    var total = 0L

    for (line in input) {
        var sum = 0L
        var value = 1L

        for (c in line.toCharArray().reversed()) {
            val num = when (c) {
                '2' -> 2
                '1' -> 1
                '0' -> 0
                '-' -> -1
                '=' -> -2
                else -> throw IllegalArgumentException()
            }

            sum += value * num
            value *= 5
        }

        total += sum
    }

    val builder = StringBuilder()
    var remaining = total
    var carry = 0
    while (remaining != 0L || carry != 0) {
        var place = (remaining % 5) + carry
        if (place >= 5) {
            place -= 5
            carry = 1
        } else {
            carry = 0
        }

        val c = when (place) {
            3L -> { carry++; '='}
            4L -> { carry++; '-' }
            0L -> '0'
            1L -> '1'
            2L -> '2'
            else -> throw IllegalArgumentException()
        }

        builder.append(c)
        remaining /= 5
    }

    return builder.reversed().toString()
}

fun part2(input: List<String>): Int {
    return -1
}

fun main() {
    val input = ClassLoader.getSystemResource("day25.txt")
        .readText().trim()
        .lines()

    println("input $input")

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
