package com.hfreeb.advent.day12

import java.util.PriorityQueue
import kotlin.math.abs

data class Node(val pos: Pair<Int, Int>, val cost: Int)

private operator fun <E> List<List<E>>.get(p: Pair<Int, Int>): E {
    return this[p.first][p.second]
}

private fun Pair<Int, Int>.neighbours(height: Int, width: Int): Set<Pair<Int, Int>> {
    val delta = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

    val total = mutableSetOf<Pair<Int, Int>>()
    for (d in delta) {
        val new = this + d
        if (new.first in 0 until height && new.second in 0 until width) {
            total += new
        }
    }

    return total
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + other.first, this.second + other.second)
}

// djikstra was a bit unnecessary - I thought it'd be needed for part 2
fun solve(input: List<List<Char>>, starting: Char): Int {
    val prio = PriorityQueue<Node> { a, b -> a.cost - b.cost }
    val seen = mutableSetOf<Pair<Int, Int>>()

    for (i in input.indices) {
        for (j in 0 until input[i].size) {
            if (input[i][j] == starting) {
                val pos = Pair(i, j)
                seen += pos

                val neighbours = pos.neighbours(input.size, input[0].size)
                    .filter { it !in seen && abs(input[it] - 'a') <= 1 }
                    .map { Node(it, 1) }

                prio.addAll(neighbours)
            }
        }
    }

    while (!prio.isEmpty()) {
        val pop = prio.poll()
        if (pop.pos in seen) {
            continue
        }

        if (input[pop.pos] == 'E') {
            return pop.cost
        }

        seen += pop.pos

        val neighbours = pop.pos.neighbours(input.size, input[0].size)
            .filter { it !in seen && ((input[it] - input[pop.pos]) <= 1 || input[it] == 'E' && (input[pop.pos] - 'z') <= 1) }
            .map { Node(it, pop.cost + 1) }

        prio.addAll(neighbours)
    }

    return -1
}

fun part1(input: List<List<Char>>): Int {
    return solve(input, 'S')
}

fun part2(input: List<List<Char>>): Int {
    return solve(input, 'a')
}

fun main() {
    val input = ClassLoader.getSystemResource("day12.txt")
        .readText().trim()
        .lines()
        .map { it.toCharArray().toList() }

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
