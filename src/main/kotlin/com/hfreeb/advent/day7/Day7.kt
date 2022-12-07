package com.hfreeb.advent.day7

data class File(val name: String, val size: Int)

class Directory(val parent: Directory?) {
    val files: MutableSet<File> = mutableSetOf()
    val children: MutableMap<String, Directory> = mutableMapOf()

    fun size(): Int {
        return files.sumOf { it.size } + children.entries.sumOf { it.value.size() }
    }
}

fun traverse(dir: Directory): Set<Directory> {
    val directories = mutableSetOf<Directory>()
    directories += dir

    for (child in dir.children.values) {
        directories += traverse(child)
    }

    return directories
}

fun build(input: List<String>): Directory {
    val root = Directory(null)

    var working = root
    var i = 0
    while (i < input.size) {
        val line = input[i++]
        val args = line.split(" ")

        when (args[1]) {
            "cd" -> {
                when (val dir = args[2]) {
                    "/" -> working = root
                    ".." -> working = working.parent!!
                    else -> {
                        if (dir !in working.children) {
                            working.children[dir] = Directory(working)
                        }

                        working = working.children[dir]!!
                    }
                }
            }
            "ls" -> {
                var output = input[i]

                while (!output.startsWith("$")) {
                    val components = output.split(" ")
                    if (components[0] == "dir") {
                        val dir = components[1]
                        if (dir !in working.children) {
                            working.children[dir] = Directory(working)
                        }
                    } else {
                        val size = components[0].toInt()
                        working.files += File(components[1], size)
                    }

                    if (i == input.size - 1) {
                        break
                    }

                    output = input[++i]
                }
            }
        }
    }

    return root
}

fun part1(input: List<String>): Int {
    val root = build(input)

    return traverse(root)
        .map(Directory::size)
        .filter { it <= 100000 }
        .sum()
}

fun part2(input: List<String>): Int {
    val root = build(input)

    val used = root.size()
    val needed = used - (70000000 - 30000000)
    return traverse(root)
        .map(Directory::size)
        .filter { it >= needed }
        .minOrNull()!!
}

fun main() {
    val input = ClassLoader.getSystemResource("day7.txt")
        .readText().trim()
        .lines()

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
