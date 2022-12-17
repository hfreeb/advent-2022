package com.hfreeb.advent.day17

val patterns = listOf(
    listOf("..@@@@."),
    listOf("...@...", "..@@@..", "...@..."),
    listOf("....@..", "....@..", "..@@@.."),
    listOf("..@....", "..@....", "..@....", "..@...."),
    listOf("..@@...", "..@@...")
)

class Tetris(val input: String) {
    val chamber = mutableListOf<String>()
    var patternIndex = 0
    var movementIndex = 0

    private fun newRock() {
        val rock = patterns[patternIndex]
        patternIndex = (patternIndex + 1) % patterns.size

        val last = chamber.indexOfLast { line -> line.any { it == '#' } }
        val start = last + 4

        val heightNeeded = start + rock.size + 1
        for (i in chamber.size until heightNeeded) {
            chamber += "......."
        }

        for ((index, level) in rock.reversed().withIndex()) {
            chamber[start + index] = level
        }
    }

    private fun move() {
        val movement = input[movementIndex]
        movementIndex = (movementIndex + 1) % input.length

        var allFree = true
        for (i in 0 until chamber.size) {
            val row = chamber[i]
            if (!row.contains("@")) {
                continue
            }

            if (movement == '<') {
                val first = row.indexOfFirst { it == '@' }
                if (first == 0 || row[first - 1] != '.') {
                    allFree = false
                }
            } else if (movement == '>') {
                val last = row.indexOfLast { it == '@' }
                if (last == row.length - 1 || row[last + 1] != '.') {
                    allFree = false
                }
            }
        }

        if (allFree) {
            for (i in 0 until chamber.size) {
                val row = chamber[i]
                if (!row.contains("@")) {
                    continue
                }

                if (movement == '<') {
                    val first = row.indexOfFirst { it == '@' }
                    val last = row.indexOfLast { it == '@' }

                    chamber[i] = row.substring(0, first - 1) + row.substring(first, last + 1) + '.' + row.substring(last + 1)
                } else if (movement == '>') {
                    val first = row.indexOfFirst { it == '@' }
                    val last = row.indexOfLast { it == '@' }

                    chamber[i] = row.substring(0, first) + '.' + row.substring(first, last + 1) + row.substring(
                        last + 2,
                        row.length
                    )
                }
            }
        }
    }

    private fun fall(): Boolean {
        var allFree = true
        for (i in 0 until chamber.size) {
            val line = chamber[i]
            if (!line.contains("@")) {
                continue
            }

            if (i == 0) {
                allFree = false
                break
            }

            for ((index, c) in line.withIndex()) {
                if (c != '@') {
                    continue
                }

                if (chamber[i - 1][index] == '#') {
                    allFree = false
                }
            }
        }

        for (i in 0 until chamber.size) {
            val line = chamber[i]
            if (!line.contains("@")) {
                continue
            }

            if (allFree) {
                for ((index, c) in line.withIndex()) {
                    if (c != '@') {
                        continue
                    }

                    chamber[i] = chamber[i].substring(0, index) + '.' + chamber[i].substring(index + 1)
                    chamber[i - 1] = chamber[i - 1].substring(0, index) + '@' + chamber[i - 1].substring(index + 1)
                }
            } else {
                chamber[i] = line.replace("@", "#")
            }
        }

        return allFree
    }

    fun iteration(): Boolean {
        if (chamber.none { it.contains("@") }) {
            newRock()
        }

        move()

        return !fall()
    }

    override fun toString(): String {
        val builder = StringBuilder()

        for (i in 0 until chamber.size) {
            builder.append('|')
            builder.append(chamber[chamber.size - 1 - i])
            builder.append('|')
            builder.appendLine()
        }

        builder.appendLine("+-------+")

        return builder.toString()
    }
}
