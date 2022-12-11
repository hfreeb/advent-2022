package com.hfreeb.advent.day11

data class Monkey(val startingItems: List<Long>, val op: String, val rhs: String, val div: Long, val onTrue: Int, val onFalse: Int)

fun parse(str: String): Monkey {
    val startingItems =
        Regex("Starting items: ([\\d, ]+)").find(str)!!.groupValues[1].split(", ").map { it.toLong() }
    val (op, rhs) = Regex("Operation: new = old ([+*]) (old|\\d+)").find(str)!!.destructured
    val div = Regex("divisible by (\\d+)").find(str)!!.groupValues[1].toLong()
    val onTrue = Regex("If true: throw to monkey (\\d)").find(str)!!.groupValues[1].toInt()
    val onFalse = Regex("If false: throw to monkey (\\d)").find(str)!!.groupValues[1].toInt()

    return Monkey(startingItems, op, rhs, div, onTrue, onFalse)
}

fun solve(input: List<Monkey>, rounds: Int, manage: (Long) -> Long): Long {
    val items = Array<MutableList<Long>>(input.size) { mutableListOf() }
    val inspects = Array(input.size) { 0 }

    for (round in 0 until rounds) {
        for ((index, monkey) in input.withIndex()) {
            if (round == 0) {
                items[index].addAll(monkey.startingItems)
            }

            for (item in items[index]) {
                val rhsVal = if (monkey.rhs == "old") item else monkey.rhs.toLong()
                val worry = when (monkey.op) {
                    "+" -> item + rhsVal
                    "*" -> item * rhsVal
                    else -> throw IllegalArgumentException()
                }

                inspects[index]++

                val relieved = manage(worry)

                if (relieved % monkey.div == 0L) {
                    items[monkey.onTrue] += relieved
                } else {
                    items[monkey.onFalse] += relieved
                }
            }

            items[index].clear()
        }
    }

    return inspects.sorted().map { it.toLong() }.takeLast(2).fold(1L) { a, b -> a*b }
}

fun part1(input: List<Monkey>): Long {
    return solve(input, 20) { it / 3 }
}

fun part2(input: List<Monkey>): Long {
    val prod = input.map { it.div }.fold(1L) { a, b -> a * b }

    return solve(input, 10000) { it % prod }
}

fun main() {
    val input = ClassLoader.getSystemResource("day11.txt")
        .readText().trim()
        .split("\n\n")
        .map(::parse)

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
