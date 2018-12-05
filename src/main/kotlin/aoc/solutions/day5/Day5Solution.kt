package aoc.solutions.day5

import aoc.Solution

class Day5Solution : Solution(5) {

    override fun solvePartOne(input: String): String {
        val chars = input.toList().react()
        return chars.size.toString()
    }

    override fun solvePartTwo(input: String): String {
        val min = ('a'..'z').map { char ->
            input.filterNot { it == char || char - it == 32 }.toList()
        }.map { it.react().size }.min() ?: throw IllegalStateException()

        return min.toString()
    }

}

fun List<Char>.react(): List<Char> {
    val chars = toMutableList()
    var idx = 0
    while(idx < chars.lastIndex) {
        if (Math.abs(chars[idx] - chars[idx+1]) == 32) {
            chars.removeAt(idx)
            chars.removeAt(idx)
            idx = idx.dec().coerceAtLeast(0)
        } else {
            ++idx
        }
    }
    return chars
}

