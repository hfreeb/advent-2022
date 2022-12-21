package com.hfreeb.advent.day21

fun getValue(input: Map<String, String>, monkey: String, part2: Boolean): Long? {
    if (part2 && monkey == "humn") {
        return null
    }

    val str = input[monkey]!!

    val value = str.toLongOrNull()
    if (value != null) {
        return value
    }

    val aStr = str.substring(0, 4)
    val bStr = str.substring(7, 11)

    val a = getValue(input, aStr, part2)
    val b = getValue(input, bStr, part2)

    if (a == null || b == null) {
        return null
    }

    return when (str[5]) {
        '+' -> a + b
        '-' -> a - b
        '*' -> a * b
        '/' -> a / b
        else -> throw IllegalArgumentException()
    }
}

fun calculateUnknown(input: Map<String, String>, target: Long?, monkey: String): Long {
    val str = input[monkey]!!

    if (monkey == "humn") {
        return target!!
    }

    val value = str.toLongOrNull()
    if (value != null) {
        return value
    }

    val strA = str.substring(0, 4)
    val strB = str.substring(7, 11)

    val a = getValue(input, strA, true)
    val b = getValue(input, strB, true)

    if (a != null && b != null) {
        throw IllegalStateException()
    }

    val known = a ?: b ?: throw IllegalStateException()
    val unknown = if (a == null) strA else strB

    if (monkey == "root") {
        return calculateUnknown(input, known, unknown)
    }

    requireNotNull(target)
    val next = when (str[5]) {
        '+' -> target - known
        '-' -> if (a == null) target + known else known - target
        '*' -> target / known
        '/' -> if (a == null) target * known else known - target
        else -> throw IllegalArgumentException()
    }

    return calculateUnknown(input, next, unknown)
}

fun part1(input: Map<String, String>): Long {
    return getValue(input, "root", false)!!
}

fun part2(input: Map<String, String>): Long {
    return calculateUnknown(input, null, "root")
}

fun main() {
    val input = ClassLoader.getSystemResource("day21.txt")
        .readText().trim()
        .lines()
        .associate { line ->
            val (name, expr) = line.split(": ")
            Pair(name, expr)
        }

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
