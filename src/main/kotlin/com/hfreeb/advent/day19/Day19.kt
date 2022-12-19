package com.hfreeb.advent.day19

data class Blueprint(
    val id: Int,
    val oreRobotCost: Int,
    val clayRobotCost: Int,
    val obsidianRobotCostOre: Int, val obsidianRobotCostClay: Int,
    val geodeRobotCostOre: Int, val geodeRobotCostObsidian: Int
)

fun parse(str: String): Blueprint {
    val regex =
        Regex("Blueprint (\\d+): Each ore robot costs (\\d) ore. Each clay robot costs (\\d) ore. Each obsidian robot costs (\\d) ore and (\\d+) clay. Each geode robot costs (\\d) ore and (\\d+) obsidian.")
    val match = regex.matchEntire(str)!!
    val (id, oreRobotCost, clayRobotCost, obsidianRobotCostOre, obsidianRobotCostClay, geodeRobotCostOre, geodeRobotCostObsidian) = match.destructured
    return Blueprint(
        id.toInt(),
        oreRobotCost.toInt(),
        clayRobotCost.toInt(),
        obsidianRobotCostOre.toInt(),
        obsidianRobotCostClay.toInt(),
        geodeRobotCostOre.toInt(),
        geodeRobotCostObsidian.toInt()
    )
}

data class State(
    val mins: Int,
    val oreRobots: Int,
    val clayRobots: Int,
    val obsidianRobots: Int,
    val geodeRobots: Int,
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geodes: Int
)

fun maximise(
    blueprint: Blueprint,
    seen: MutableMap<State, Int>,
    maxSeen: Array<Int>,
    maxGeodes: Array<Int>,
    maxGeodeRobots: Array<Int>,
    state: State
): Int {
    if (state.mins == 0) {
        return state.geodes
    }

    if (state in seen) {
        return seen[state]!!
    }

    // Check if impossible to get max geodes seen with time left
    if (state.mins * (state.mins + state.geodeRobots) + state.geodes < maxSeen[0]) {
        return 0
    }

    // Check if geode/geode robots are > 1 away from max seen at this minute
    if (maxGeodeRobots[state.mins - 1] - state.geodeRobots > 1 || maxGeodes[state.mins - 1] - state.geodes > 1) {
        return 0
    }
    if (state.geodeRobots > maxGeodeRobots[state.mins - 1]) {
        maxGeodeRobots[state.mins - 1] = state.geodeRobots
    }
    if (state.geodes > maxGeodes[state.mins - 1]) {
        maxGeodes[state.mins - 1] = state.geodes
    }

    // Get next states
    val next = state.copy(
        mins = state.mins - 1,
        ore = state.ore + state.oreRobots,
        clay = state.clay + state.clayRobots,
        obsidian = state.obsidian + state.obsidianRobots,
        geodes = state.geodes + state.geodeRobots
    )

    val children = mutableListOf<State>()
    if (state.ore >= blueprint.oreRobotCost) {
        children += next.copy(oreRobots = state.oreRobots + 1, ore = next.ore - blueprint.oreRobotCost)
    }
    if (state.ore >= blueprint.clayRobotCost) {
        children += next.copy(clayRobots = state.clayRobots + 1, ore = next.ore - blueprint.clayRobotCost)
    }
    if (state.ore >= blueprint.obsidianRobotCostOre && state.clay >= blueprint.obsidianRobotCostClay) {
        children += next.copy(
            obsidianRobots = state.obsidianRobots + 1,
            ore = next.ore - blueprint.obsidianRobotCostOre,
            clay = next.clay - blueprint.obsidianRobotCostClay
        )
    }
    if (state.ore >= blueprint.geodeRobotCostOre && state.obsidian >= blueprint.geodeRobotCostObsidian) {
        children += next.copy(
            geodeRobots = state.geodeRobots + 1,
            ore = next.ore - blueprint.geodeRobotCostOre,
            obsidian = next.obsidian - blueprint.geodeRobotCostObsidian
        )
    }
    children += next

    val max = children.map { maximise(blueprint, seen, maxSeen, maxGeodes, maxGeodeRobots, it) }.maxOf { it }
    seen[state] = max

    if (max > maxSeen[0]) {
        maxSeen[0] = max
    }

    return max
}

fun part1(input: List<Blueprint>): Int {
    var sum = 0
    for (blueprint in input) {
        val max = maximise(blueprint, mutableMapOf(), Array(1) { 0 }, Array(24) { 0 }, Array(24) { 0 },
            State(24, 1, 0, 0, 0, 0, 0, 0, 0)
        )
        sum += blueprint.id * max
    }

    return sum
}

fun part2(input: List<Blueprint>): Int {
    var product = 1
    for (blueprint in input.take(3)) {
        val max = maximise(blueprint, mutableMapOf(), Array(1) { 0 }, Array(32) { 0 }, Array(32) { 0 },
            State(32, 1, 0, 0, 0, 0, 0, 0, 0)
        )
        product *= max
    }
    return product
}

fun main() {
    val input = ClassLoader.getSystemResource("day19.txt")
        .readText().trim()
        .lines()
        .map(::parse)

    println("part 1 = ${part1(input)}")
    println("part 2 = ${part2(input)}")
}
