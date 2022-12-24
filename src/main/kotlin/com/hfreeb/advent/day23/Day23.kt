package com.hfreeb.advent.day23

data class Vector2i(val x: Int, val y: Int) {
    operator fun plus(other: Vector2i): Vector2i {
        return Vector2i(x + other.x, y + other.y)
    }
}

private operator fun <E> List<List<E>>.get(vec: Vector2i): E {
    return this[vec.y][vec.x]
}


private operator fun <E> MutableList<MutableList<E>>.set(end: Vector2i, value: E) {
    this[end.y][end.x] = value
}

fun printGrid(taken: Set<Vector2i>) {
    val xMin = taken.minOf { it.x }
    val xMax = taken.maxOf { it.x }
    val yMin = taken.minOf { it.y }
    val yMax = taken.maxOf { it.y }

    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            print(if (Vector2i(x, y) in taken) '#' else '.')
        }
        println()
    }
}

fun solve(input: List<String>, part2: Boolean): Int {
    val taken = mutableSetOf<Vector2i>()
    for (y in input.indices) {
        for (x in input[y].indices) {
            val pos = Vector2i(x, y)
            if (input[y][x] == '#') {
                taken += pos
            }
        }
    }

    val considered = listOf(
        listOf(Vector2i(-1, -1), Vector2i(0, -1), Vector2i(1, -1)) to Vector2i(0, -1),
        listOf(Vector2i(-1, 1), Vector2i(0, 1), Vector2i(1, 1)) to Vector2i(0, 1),
        listOf(Vector2i(-1, -1), Vector2i(-1, 0), Vector2i(-1, 1)) to Vector2i(-1, 0),
        listOf(Vector2i(1, -1), Vector2i(1, 0), Vector2i(1, 1)) to Vector2i(1, 0),
    )

    var round = 0
    while (round < 10 || part2) {
        var elfMoved = false
        val proposed = mutableMapOf<Vector2i, Vector2i>()

        for (pos in taken) {
            var move: Vector2i? = null

            if (considered.none { it.first.any { delta -> (pos + delta) in taken } }) {
                continue
            }

            for (i in considered.indices) {
                val (required, dest) = considered[(i + round) % considered.size]

                if (required.map { pos + it }.all { it !in taken }) {
                    move = pos + dest
                    break
                }
            }

            if (move == null) {
                continue
            }

            proposed[pos] = move
        }

        for ((start, end) in proposed.entries) {
            val count = proposed.values.count { it == end }
            if (count > 1) {
                continue
            }

            elfMoved = true

            taken -= start
            taken += end
        }

        if (!elfMoved && part2) {
            return round + 1
        }

        round++
    }

    val xMin = taken.minOf { it.x }
    val xMax = taken.maxOf { it.x }
    val yMin = taken.minOf { it.y }
    val yMax = taken.maxOf { it.y }

    var count = 0
    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            if (Vector2i(x, y) !in taken) {
                count++
            }
        }
    }

    return count
}

fun part1(input: List<String>): Int {
    return solve(input, false)
}

fun part2(input: List<String>): Int {
    return solve(input, true)
}

fun main() {
    val input = ClassLoader.getSystemResource("day23.txt")
        .readText().trim()
        .lines()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
