package com.hfreeb.advent.day18

import kotlin.math.abs

val faces = setOf(
    Vector3i(1, 0, 0),
    Vector3i(-1, 0, 0),
    Vector3i(0, 1, 0),
    Vector3i(0, -1, 0),
    Vector3i(0, 0, 1),
    Vector3i(0, 0, -1),
)

data class Vector3i (val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Vector3i): Vector3i {
        return Vector3i(x + other.x, y + other.y, z + other.z)
    }

    fun axis(axis: Int): Int {
        return when (axis) {
            1 -> x
            2 -> y
            3 -> z
            else -> throw IllegalArgumentException()
        }
    }

    fun axis(axis: Int, value: Int): Vector3i {
        return when(axis) {
            1 -> Vector3i(value, y, z)
            2 -> Vector3i(x, value, z)
            3 -> Vector3i(x, y, value)
            else -> throw IllegalArgumentException()
        }
    }

    fun collides(other: Vector3i, axis: Int, direction: Int): Boolean {
        val sameColumn = (1..3).minus(axis).all { axis(it) == other.axis(it) }

        val result = sameColumn && when (direction) {
            -1 -> return other.axis(axis) < axis(axis)
            1 -> return other.axis(axis) > axis(axis)
            else -> throw IllegalArgumentException()
        }

        return result
    }
}

fun part1(input: Set<Vector3i>): Int {
    var total = 0
    for (pos in input) {
        for (face in faces) {
            val sum = pos + face

            if (sum !in input) {
                total += 1
            }
        }
    }

    return total
}

fun canEscape(input: Set<Vector3i>, pos: Vector3i, visited: MutableSet<Vector3i>): Boolean {
    visited += pos

    for (axis in 1..3) {
        for (direction in listOf(-1, 1)) {
            val collision = input.filter { pos.collides(it, axis, direction) }
                .minByOrNull { abs(pos.axis(axis) - it.axis(axis)) } ?: return true

            val start = collision.axis(axis, collision.axis(axis) - direction)
            if (start in visited) {
                continue
            }

            if (canEscape(input, start, visited)) {
                return true
            }
        }
    }

    return false
}

fun part2(input: Set<Vector3i>): Int {
    var total = 0
    for (pos in input) {
        for (face in faces) {
            val sum = pos + face

            if (sum !in input && canEscape(input, sum, mutableSetOf())) {
                total += 1
            }
        }
    }

    return total
}

fun main() {
    val input = ClassLoader.getSystemResource("day18.txt")
        .readText().trim()
        .lines()
        .map { line ->
            val (x, y, z) = line.split(",").map { it.toInt() }
            Vector3i(x, y, z)
        }.toSet()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
