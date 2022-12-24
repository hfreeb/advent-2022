package com.hfreeb.advent.day24

import java.util.*
import kotlin.math.abs

data class Vector2i(val x: Int, val y: Int) {
    operator fun plus(other: Vector2i): Vector2i {
        return Vector2i(x + other.x, y + other.y)
    }
}

private operator fun List<String>.get(vec: Vector2i): Char {
    return this[vec.y][vec.x]
}

data class State(val time: Int, val pos: Vector2i)

fun solve(input: List<String>, startTime: Int, start: Vector2i, end: Vector2i): Int {
    val prio = PriorityQueue<State>(compareBy { it.time + abs(end.x - it.pos.x) + abs(end.y - it.pos.y) })
    prio.add(State(startTime, start))

    val seen = mutableSetOf<State>()

    while (prio.isNotEmpty()) {
        val state = prio.poll()
        if (state in seen) {
            continue
        }

        seen += state

        if (state.pos == end) {
            return state.time
        }

        val time = state.time + 1

        for (dir in listOf(Vector2i(0, 0), Vector2i(1, 0), Vector2i(-1, 0), Vector2i(0, 1), Vector2i(0, -1))) {
            val new = state.pos + dir
            if (new.x < 0 || new.y < 0 || new.y >= input.size || new.x >= input[new.y].length || input[new] == '#') {
                continue
            }

            var collision = false

            // Check for blizzards column-wise
            for (y in 1 until input.size - 1) {
                val c = input[y][new.x]
                if (c !in "^v") {
                    continue
                }

                val direction = if (c == 'v') 1 else -1
                val currentPos = Math.floorMod(y + time * direction - 1, input.size - 2) + 1
                if (currentPos == new.y) {
                    collision = true
                    break
                }
            }

            // Check for blizzards row-wise
            for (x in 1 until input[new.y].length - 1) {
                val c = input[new.y][x]
                if (c !in "><") {
                    continue
                }

                val direction = if (c == '>') 1 else -1
                val currentPos = Math.floorMod(x + time * direction - 1, input[new.y].length - 2) + 1
                if (currentPos == new.x) {
                    collision = true
                    break
                }
            }

            if (!collision) {
                prio.add(State(time, new))
            }
        }
    }

    return -1
}

fun part1(input: List<String>): Int {
    val start = Vector2i(1, 0)
    val end = Vector2i(input[input.size - 1].length - 2, input.size - 1)

    return solve(input, 0, start, end)
}
fun part2(input: List<String>): Int {
    val start = Vector2i(1, 0)
    val end = Vector2i(input[input.size - 1].length - 2, input.size - 1)

    val p1 = solve(input, 0, start, end)
    val snacks = solve(input, p1, end, start)
    return solve(input, snacks, start, end)
}

fun main() {
    val input = ClassLoader.getSystemResource("day24.txt")
        .readText().trim()
        .lines()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
