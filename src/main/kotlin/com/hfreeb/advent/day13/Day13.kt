package com.hfreeb.advent.day13

sealed class Element {
    data class EInt(val i: Int): Element()
    data class EList(val list: List<Element>): Element()
}

class StringParser(private val str: String) {
    private var i = 0

    fun pop(): Char {
        return str[i++]
    }

    fun peek(): Char {
        return str[i]
    }
}

fun compare(a: Element, b: Element): Int {
    when {
        a is Element.EInt && b is Element.EInt -> {
            return a.i - b.i
        }
        a is Element.EList && b is Element.EList -> {
            for (i in a.list.indices) {
                if (i >= b.list.size) {
                    return 1
                }

                val c = compare(a.list[i], b.list[i])
                if (c > 0) {
                    return 1
                } else if (c < 0) {
                    return -1
                }
            }

            return if (b.list.size > a.list.size) -1 else 0
        }
        a is Element.EList && b is Element.EInt -> return compare(a, Element.EList(listOf(b)))
        a is Element.EInt && b is Element.EList -> return compare(Element.EList(listOf(a)), b)
    }

    throw IllegalArgumentException()
}

fun parseList(str: StringParser): Element.EList {
    require(str.pop() == '[')
    val elements = mutableListOf<Element>()
    while (str.peek() != ']') {
        if (str.peek() == '[') {
            elements += parseList(str)
        } else {
            val digits = StringBuilder()
            while (str.peek().isDigit()) {
                digits.append(str.pop())
            }
            elements.add(Element.EInt(digits.toString().toInt()))
        }

        if (str.peek() == ',') {
            str.pop()
        }
    }

    str.pop()

    return Element.EList(elements)
}

fun part1(input: List<List<Element>>): Int {
    var sum = 0

    for ((index, pair) in input.withIndex()) {
        val (a, b) = pair
        if (compare(a, b) <= 0) {
            sum += index + 1
        }
    }

    return sum
}

fun part2(input: List<List<Element>>): Int {
    val packets = input.flatten().toMutableList()

    val a = Element.EList(listOf(Element.EList(listOf(Element.EInt(2)))))
    val b = Element.EList(listOf(Element.EList(listOf(Element.EInt(6)))))
    packets.add(a)
    packets.add(b)

    val sorted = packets.sortedWith(::compare)

    return (sorted.indexOf(a) + 1) * (sorted.indexOf(b) + 1)
}

fun main() {
    val input = ClassLoader.getSystemResource("day13.txt")
        .readText().trim()
        .split("\n\n")
        .map { it.split("\n").map { line -> parseList(StringParser(line)) } }

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
