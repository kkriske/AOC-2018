package aoc.solutions.day3

import aoc.Solution

class Day3Solution : Solution(3) {

    override fun solvePartOne(input: String): String {
        val claims = input.lines().map { Claim.parse(it) }

        val claimed = mutableSetOf<String>()
        val overlap = mutableSetOf<String>()
        var count = 0

        for (claim in claims) {
            for (square in claim.containedSquares()) {
                if (!claimed.add(square) && overlap.add(square))
                    count++
            }
        }

        return count.toString()
    }

    override fun solvePartTwo(input: String): String {
        val claims = input.lines().map { Claim.parse(it) }

        val claimed = mutableSetOf<String>()
        val overlap = mutableSetOf<String>()
        val candidates = claims.toMutableSet()

        for (claim in claims) {
            for (square in claim.containedSquares()) {
                if (!claimed.add(square)) {
                    overlap.add(square)
                    candidates.remove(claim)
                }
            }
        }

        return candidates.first {claim ->
            claim.containedSquares().none { overlap.contains(it) }
        }.id.toString()
    }

}

data class Claim(val id: Int, val left: Int, val top: Int, val width: Int, val height: Int) {
    companion object {
        fun parse(str: String): Claim {
            val parsed = str.drop(1).split(" @ ", ",", ": ", "x").map { it.toInt() }
            return Claim(parsed[0], parsed[1], parsed[2], parsed[3], parsed[4])
        }
    }

    fun containedSquares(): Set<String> {
        val set = mutableSetOf<String>()
        for (i in left until (left + width)) {
            for (j in top until (top + height)) {
                set.add("$i-$j")
            }
        }

        return set
    }
}


