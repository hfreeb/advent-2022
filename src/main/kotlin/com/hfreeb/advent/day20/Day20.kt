package com.hfreeb.advent.day20

fun mix(list: MutableList<Long>, mapping: MutableMap<Int, Int>) {
    val indices = list.indices

    for (i in indices) {
        val current = mapping[i]!!
        val value = list[current]

        list.removeAt(current)
        for (j in indices) {
            if (mapping[j]!! > current) {
                mapping[j] = mapping[j]!! - 1
            }
        }

        var new = (current + value) % (mapping.size - 1)
        if (new <= 0) {
            new += mapping.size - 1
        }

        list.add(new.toInt(), value)
        for (j in indices) {
            if (mapping[j]!! >= new) {
                mapping[j] = mapping[j]!! + 1
            }
        }

        mapping[i] = new.toInt()
    }
}

fun part1(input: List<Long>): Long {
    val mapping = mutableMapOf<Int, Int>()
    for (i in input.indices) {
        mapping[i] = i
    }

    val list = input.toMutableList()
    mix(list, mapping)

    val zero = list.indexOf(0)
    return listOf(1000, 2000, 3000).sumOf { list[(zero + it) % list.size] }
}

fun part2(input: List<Long>): Long {
    val mapping = mutableMapOf<Int, Int>()
    for (i in input.indices) {
        mapping[i] = i
    }

    val list = input.map { it * 811589153 }.toMutableList()
    for (iteration in 0 until 10) {
      mix(list, mapping)
    }

    val zero = list.indexOf(0)
    return listOf(1000, 2000, 3000).sumOf { list[(zero + it) % list.size] }
}

fun main() {
    val input = ClassLoader.getSystemResource("day20.txt")
        .readText().trim()
        .lines()
        .map { it.toLong() }

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
