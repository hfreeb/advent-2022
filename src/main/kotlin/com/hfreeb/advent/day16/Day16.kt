package com.hfreeb.advent.day16

// This code is rough, but I don't want to look at this problem anymore.

data class Node(val valve: String, val rate: Int, val leadsTo: List<String>)

data class Memoize(val valve: String, val open: Set<String>, val mins: Int)

fun max(node: Node, input: Map<String, Node>, seen: MutableMap<Memoize, Int>, open: Set<String>, mins: Int, sum: Int, maxSums: MutableList<Int>): Int {
    if (mins == 0) {
        return 0
    }

    val id = Memoize(node.valve, open, mins)
    if (id in seen.keys) {
        return seen[id]!!
    }

    val maxTheoretical = sum + input.values.filter { it.valve !in open }.sumOf { (mins - 1) * it.rate }
    if (maxTheoretical < maxSums[0]) {
        return 0
    }

    if (input.values.none { it.valve !in open && it.rate > 0 }) {
        return 0
    }

    val gain = (mins - 1) * node.rate
    var max = 0

    if (mins >= 2 && node.valve !in open && node.rate != 0) {
        // moves
        for (v in node.leadsTo) {
            val total = max(
                input[v]!!,
                input,
                seen,
                open.plus(node.valve),
                mins - 2,
                sum + gain,
                maxSums
            ) + gain
            if (total > max) {
                max = total
            }
        }
    }

    for (v in node.leadsTo) {
        val total = max(
            input[v]!!,
            input,
            seen,
            open,
            mins - 1,
            sum,
            maxSums
        )
        if (total > max) {
            max = total
        }
    }

    if (sum + max > maxSums[0]) {
        maxSums[0] = sum + max
    }

    seen[id] = max

    return max
}

fun <T> Collection<T>.powerSet(): Set<Set<T>> = when {
    isEmpty() -> setOf(setOf())
    else -> drop(1).powerSet().let { it + it.map { it + first() } }
}

fun part1(input: Map<String, Node>): Int {
    return max(input["AA"]!!, input, mutableMapOf(), emptySet(), 30, 0, mutableListOf(0))
}

fun part2(input: Map<String, Node>): Int {
    var max = 0

    val candidates = input.values.filter { it.rate != 0 }.toSet()
    val powerSet = candidates.powerSet()
    val filtered = mutableSetOf<Set<Node>>()
    for (element in powerSet) {
        if (candidates.minus(element) !in filtered) {
            filtered += element
        }
    }

    for ((index, personElements) in filtered.withIndex()) {
        val elephantElements = candidates.minus(personElements)

        var total = 0
        total += max(input["AA"]!!, input, mutableMapOf(), personElements.map { it.valve }.toSet(), 26, 0, mutableListOf(0))
        total += max(input["AA"]!!, input, mutableMapOf(), elephantElements.map { it.valve }.toSet(), 26, 0, mutableListOf(0))

        println("$index/${filtered.size}: $total")
        if (total > max) {
            max = total
        }
    }

    return max
}


fun main() {
    val input = ClassLoader.getSystemResource("day16.txt")
        .readText().trim()
        .lines().associate {
            println(it)
            val regex = Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? ((?:[A-Z]{2},? ?)+)")
            val (valve, rate, leadsTo) = regex.matchEntire(it)!!.destructured
            Pair(valve, Node(valve, rate.toInt(), leadsTo.split(", ")))
        }

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
