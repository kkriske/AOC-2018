package aoc.solutions.day1

import aoc.Solution

class Day1Solution : Solution(1) {

    override fun solvePartOne(input: String): String {
        return input.lineSequence()
            .sumBy { it.toInt() }
            .toString()
    }

    override fun solvePartTwo(input: String): String {
        return input.lineSequence()
            .infinite()
            .accumulate()
            .firstDuplicate()
            .toString()
    }
}

fun <T> Sequence<T>.infinite(): Sequence<T> = sequence {
    if (!this@infinite.iterator().hasNext())
        return@sequence
    while (true) yieldAll(this@infinite)
}

fun Sequence<String>.accumulate(): Sequence<Int> = sequence {
    var value = 0
    yield(value)
    forEach {
        value += it.toInt()
        yield(value)
    }
}

fun <T> Sequence<T>.firstDuplicate(): T {
    val set = mutableSetOf<T>()
    return first { !set.add(it) }
}
