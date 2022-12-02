package com.hfreeb.advent.day2

val winAgainst = mapOf(
    Move.Rock to Move.Paper,
    Move.Scissors to Move.Rock,
    Move.Paper to Move.Scissors,
)

val loseAgainst = mapOf(
    Move.Rock to Move.Scissors,
    Move.Paper to Move.Rock,
    Move.Scissors to Move.Paper
)

val patternsA = mapOf(
    "A" to Move.Rock,
    "B" to Move.Paper,
    "C" to Move.Scissors,
)

val patternsB = mapOf(
    "X" to Move.Rock,
    "Y" to Move.Paper,
    "Z" to Move.Scissors,
)

enum class Move(val value: Int) {
    Rock(1),
    Paper(2),
    Scissors(3);

    fun outcome(opponent: Move): Int {
        return when {
            winAgainst[opponent] == this -> 6
            this == opponent -> 3
            else -> 0
        }
    }
}

data class Round(val a: String, val b: String) {
    companion object {
        fun parse(str: String): Round {
            val (a, b) = str.split(" ")
            return Round(a, b)
        }
    }
}

fun part1(input: List<Round>): Int {
    var total = 0

    for (round in input) {
        val moveA = patternsA[round.a]!!
        val moveB = patternsB[round.b]!!

        total += moveB.value + moveB.outcome(moveA)
    }

    return total
}

fun part2(input: List<Round>): Int {
    var total = 0

    for (round in input) {
        val moveA = patternsA[round.a]!!

        val moveB = when(val pattern = round.b) {
            "X" -> loseAgainst[moveA]!!
            "Y" -> moveA
            "Z" -> winAgainst[moveA]!!
            else -> throw IllegalArgumentException("Invalid move '$pattern'")
        }

        total += moveB.value + moveB.outcome(moveA)
    }

    return total
}

fun main() {
    val input = ClassLoader.getSystemResource("day2.txt")
        .readText().trim()
        .lines()
        .map(Round::parse)

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
