package com.hfreeb.advent.day17

fun part1(input: String): Int {
    val tetris = Tetris(input)

    var total = 0
    while (total < 2022) {
        if (tetris.iteration()) {
            total++
        }
    }

    return tetris.chamber.indexOfLast { it.contains("#") } + 1
}

fun genDelta(tetris: Tetris): List<Int> {
    val delta = Array(7) { 0 }

    val last = tetris.chamber.indexOfLast { it.contains("#") }
    for (i in delta.indices) {
        delta[i] = last - tetris.chamber.indexOfLast { it[i] == '#' }
    }

    return delta.toList()
}

data class State(val delta: List<Int>, val movementIndex: Int, val patternIndex: Int)

fun part2(input: String): Long {
    val tetris = Tetris(input)

    val seen = mutableMapOf<State, Pair<Int, Int>>()

    var total = 0
    var initialRock = -1

    val heights = mutableListOf<Pair<Int, Int>>()
    val added = mutableSetOf<Int>()
    while (true) {
        if (!tetris.iteration()) {
            continue
        }

        total++

        val height = tetris.chamber.indexOfLast { it.contains("#") } + 1

        val delta = genDelta(tetris)
        val state = State(delta, tetris.movementIndex, tetris.patternIndex)
        if (state in seen.keys) {
            val last = seen[state]!!
            if (initialRock == -1) {
                initialRock = last.first
            }

            if (last.first in added) {
                break
            } else {
                heights += Pair(last.second, height - last.second)
                added += last.first
            }
        } else {
            seen[state] = Pair(total, height)
        }
    }

    val target = 1000000000000L
    val index = ((target - initialRock) % heights.size).toInt()

    val heightOffset = heights[index].first
    val heightDelta = heights[index].second
    val coefficient = (target - initialRock) / heights.size

    return heightOffset + heightDelta * coefficient
}

fun main() {
    val input = ClassLoader.getSystemResource("day17.txt")
        .readText().trim()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
