package com.hfreeb.advent.day5

import java.util.*

fun solve(input: List<String>, part2: Boolean): String {
    val stacks = Array(9) { LinkedList<Char>() }

    for (i in 7 downTo 0) {
        val line = input[i]

        for ((stack, j) in (1..33 step 4).withIndex()) {
            if (line[j] != ' ') {
                stacks[stack].push(line[j])
            }
        }
    }

    for (instruction in input.subList(10, 511)) {
        val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")
        val match = regex.matchEntire(instruction) ?: throw IllegalArgumentException()

        val (number, start, end) = match.destructured

        val crates = mutableListOf<Char>()
        for (i in 0 until number.toInt()) {
            val c = stacks[start.toInt() - 1].pop()
            crates.add(c)
        }

        if (part2) {
            crates.reverse()
        }

        for (crate in crates) {
            stacks[end.toInt() - 1].push(crate)
        }
    }

    val s = StringBuilder()
    for (stack in stacks) {
        s.append(stack.pop())
    }

    return s.toString()
}

fun part1(input: List<String>): String {
    return solve(input, false)
}

fun part2(input: List<String>): String {
    return solve(input, true)
}

fun main() {
    val input = ClassLoader.getSystemResource("day5.txt")
        .readText()
        .lines()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
