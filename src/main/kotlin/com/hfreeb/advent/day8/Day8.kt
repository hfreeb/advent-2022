package com.hfreeb.advent.day8

fun march(i: Int, j: Int, iSize: Int, jSize: Int): List<List<Pair<Int, Int>>> {
    val rays = listOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(-1, 0))

    val marched = mutableListOf<List<Pair<Int, Int>>>()
    for (ray in rays) {
        val rayMarched = mutableListOf<Pair<Int, Int>>()

        var coords = Pair(i, j) + ray
        while (coords.first in 0 until iSize && coords.second in 0 until jSize) {
            rayMarched += coords
            coords += ray
        }

        marched += rayMarched
    }

    return marched
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + other.first, this.second + other.second)
}

fun part1(input: List<List<Int>>): Int {
    var count = 0

    for (i in input.indices) {
        for (j in input[i].indices) {
            val height = input[i][j]

            val marched = march(i, j, input.size, input[i].size)
            for (ray in marched) {
                var visible = true

                for (loc in ray) {
                    if (input[loc.first][loc.second] >= height) {
                        visible = false
                        break
                    }
                }

                if (visible) {
                    count++
                    break
                }
            }
        }
    }

    return count
}

fun part2(input: List<List<Int>>): Int {
    var max = 0

    for (i in input.indices) {
        for (j in input[i].indices) {
            val height = input[i][j]
            var scenic = 1

            val marched = march(i, j, input.size, input[i].size)
            for (ray in marched) {
                var count = 0

                for (loc in ray) {
                    count++

                    if (input[loc.first][loc.second] >= height) {
                        break
                    }
                }

                scenic *= count
            }

            if (scenic > max) {
                max = scenic
            }
        }
    }

    return max
}

fun main() {
    val input = ClassLoader.getSystemResource("day8.txt")
        .readText().trim()
        .lines()
        .map { line -> line.map { it - '0' } }

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
