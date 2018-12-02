package aoc.solutions.day2

import aoc.Solution
import aoc.solutions.day1.firstDuplicate

class Day2Solution : Solution(2) {

    override fun solvePartOne(input: String): String {
        val occurrences = input.lineSequence()
            .flatMap { id ->
                id.groupingBy { it }
                    .eachCount()
                    .values
                    .asSequence()
                    .distinct()
            }.groupingBy { it }
            .eachCount()
            .withDefault { 0 }

        val checksum = occurrences.getValue(2) * occurrences.getValue(3)
        return checksum.toString()
    }

    override fun solvePartTwo(input: String): String {
        return input.lineSequence()
            .flatMap { it.subStringsSingleOmmissionIndexed() }
            .firstDuplicate()
            .second
    }

}

fun String.subStringsSingleOmmissionIndexed(): Sequence<Pair<Int,String>> = sequence {
    val str = this@subStringsSingleOmmissionIndexed
    for (i in str.indices)
        yield(i to (str.substring(0..(i - 1)) + str.substring((i + 1)..str.lastIndex)))
}
