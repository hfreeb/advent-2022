package com.hfreeb.advent.day10

fun xValues(input: List<List<String>>): List<Int> {
    val vals = mutableListOf<Int>()
    var x = 1

    for (instr in input) {
        vals.add(x)

        if (instr[0] == "addx") {
            vals.add(x)
            x += instr[1].toInt()
        }
    }

    return vals
}

fun part1(input: List<List<String>>): Int {
    val vals = xValues(input)

    var sum = 0
    for (i in 20..220 step 40) {
        sum += i * vals[i - 1]
    }

    return sum
}

fun part2(input: List<List<String>>): String {
    val vals = xValues(input)

    val screen = StringBuilder()
    for (i in 0 until 240) {
        if (i % 40 == 0) {
            screen.appendLine()
        }

        val sprite = vals[i]
        val char = if (i % 40 in sprite-1..sprite+1) '#' else '.'
        screen.append(char)
    }

    return screen.toString()
}

fun main() {
    val input = ClassLoader.getSystemResource("day10.txt")
        .readText()
        .lines()
        .map { it.split(" ") }

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
