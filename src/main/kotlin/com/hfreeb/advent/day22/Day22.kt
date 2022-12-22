package com.hfreeb.advent.day22

const val DIM = 50

data class Vector2i(val x: Int, val y: Int) {
    operator fun plus(other: Vector2i): Vector2i {
        return Vector2i(x + other.x, y + other.y)
    }
}

private operator fun List<String>.get(vec: Vector2i): Char {
    return this[vec.y][vec.x]
}

fun next(pos: Vector2i, facing: Int): Vector2i {
      val direction = when (facing) {
        0 -> Vector2i(0, -1)
        90 -> Vector2i(1, 0)
        180 -> Vector2i(0, 1)
        270 -> Vector2i(-1, 0)
        else -> throw IllegalArgumentException()
    }

    return pos + direction
}

fun wrap(grid: List<String>, pos: Vector2i, facing: Int): Pair<Vector2i, Int> {
    val next = next(pos, facing)

    val wrapped = when {
        next.y < 0 || facing == 0 && grid[next] == ' ' -> {
            val y = grid.indexOfLast { it.length > next.x && it[next.x] != ' ' }
            Vector2i(next.x, y)
        }
        next.y >= grid.size || facing == 180 && (next.x >= grid[next.y].length || grid[next] == ' ') -> {
            val y = grid.indexOfFirst { it.length > next.x && it[next.x] != ' ' }
            Vector2i(next.x, y)
        }
        next.x < 0 || facing == 270 && grid[next] == ' ' -> {
            val x = grid[next.y].indexOfLast { it != ' ' }
            Vector2i(x, next.y)
        }
        next.x >= grid[next.y].length || facing == 90 && grid[next] == ' ' -> {
            val x = grid[next.y].indexOfFirst { it != ' ' }
            Vector2i(x, next.y)
        }
        else -> next
    }

    return Pair(wrapped, facing)
}

fun wrapCube(pos: Vector2i, facing: Int): Pair<Vector2i, Int> {
    val face = (pos.y / DIM) * 3 + pos.x / DIM
    val faceX = pos.x % DIM
    val faceY = pos.y % DIM

    val next = next(pos, facing)
    val faceNext = next.y / DIM * 3 + next.x / DIM

    if (face == faceNext && next.x >= 0 && next.y >= 0) {
        return Pair(next, facing)
    }

    val wrapped = when (Pair(face, facing)) {
        Pair(1, 0) -> Pair(Vector2i(0, 3 * DIM + faceX), 90)
        Pair(1, 90) -> Pair(next, 90)
        Pair(1, 180) -> Pair(next, 180)
        Pair(1, 270) -> Pair(Vector2i(0, 3 * DIM - 1 - faceY), 90)
        Pair(2, 0) -> Pair(Vector2i(faceX, 4 * DIM - 1), 0)
        Pair(2, 90) -> Pair(Vector2i(2 * DIM - 1, 3 * DIM - 1 - faceY), 270)
        Pair(2, 180) -> Pair(Vector2i(2 * DIM - 1, DIM + faceX), 270)
        Pair(2, 270) -> Pair(next, 270)
        Pair(4, 0) -> Pair(next, 0)
        Pair(4, 90) -> Pair(Vector2i(2 * DIM + faceY, DIM - 1), 0)
        Pair(4, 180) -> Pair(next, 180)
        Pair(4, 270) -> Pair(Vector2i(faceY, 2 * DIM), 180)
        Pair(6, 0) -> Pair(Vector2i(DIM, DIM + faceX), 90)
        Pair(6, 90) -> Pair(next, 90)
        Pair(6, 180) -> Pair(next, 180)
        Pair(6, 270) -> Pair(Vector2i(DIM, DIM - 1 - faceY), 90)
        Pair(7, 0) -> Pair(next, 0)
        Pair(7, 90) -> Pair(Vector2i(3 * DIM - 1, DIM - 1 - faceY), 270)
        Pair(7, 180) -> Pair(Vector2i(DIM - 1, 3 * DIM + faceX), 270)
        Pair(7, 270) -> Pair(next, 270)
        Pair(9, 0) -> Pair(next, 0)
        Pair(9, 90) -> Pair(Vector2i(DIM + faceY, 3 * DIM - 1), 0)
        Pair(9, 180) -> Pair(Vector2i(2 * DIM + faceX, 0), 180)
        Pair(9, 270) -> Pair(Vector2i(DIM + faceY, 0), 180)
        else -> throw IllegalArgumentException()
    }

    return wrapped
}

fun solve(input: List<String>, part2: Boolean): Int {
    val grid = input[0].lines()
    val instrs = Regex("[RL]|\\d+").findAll(input[1]).map { it.value }

    var pos = Vector2i(grid[0].indexOfFirst { it == '.' }, 0)
    var facing = 90

    for (instr in instrs) {
        if (instr == "L" || instr == "R") {
            val delta = if (instr == "R") 90 else -90

            facing = Math.floorMod(facing + delta, 360)
            continue
        }

        val distance = instr.toInt()

        for (i in 0 until distance) {
            val (newPos, newFacing) = if (part2) wrapCube(pos, facing) else wrap(grid, pos, facing)

            if (grid[newPos] == '.') {
                pos = newPos
                facing = newFacing
            } else {
                break
            }
        }
    }

    return (pos.y + 1) * 1000 + (pos.x + 1) * 4 + Math.floorMod(facing - 90, 360) / 90
}

fun part1(input: List<String>): Int {
    return solve(input, false)
}

fun part2(input: List<String>): Int {
    return solve(input, true)
}

fun main() {
    val input = ClassLoader.getSystemResource("day22.txt")
        .readText()
        .split("\n\n")

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
