package aoc.solutions.day4

import aoc.Solution
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Day4Solution : Solution(4) {

    override fun solvePartOne(input: String): String {
        val orderedEvents = input.lineSequence()
            .map { GuardEvent.parse(it) }
            .sortedBy { it.timestamp }

        var guard = 0
        var sleepTime = LocalDateTime.now()
        val asleep = mutableMapOf<Int, MutableList<Int>>()
        for (event in orderedEvents) {
            when (event) {
                is GuardEvent.Shift -> guard = event.guard
                is GuardEvent.Sleep -> sleepTime = event.timestamp
                is GuardEvent.Wake -> {
                    val list = asleep.computeIfAbsent(guard) { mutableListOf()}
                    (sleepTime.minute until event.timestamp.minute).forEach { list.add(it) }
                }
            }
        }

        val mostAsleepEntry = asleep.maxBy { it.value.sum() } ?: throw IllegalStateException()

        val sleepyGuard = mostAsleepEntry.key
        val sleepyMinute = mostAsleepEntry.value
            .groupingBy { it }
            .eachCount()
            .maxBy { it.value }?.value ?: throw IllegalStateException()

        return (sleepyGuard * sleepyMinute).toString()
    }

    override fun solvePartTwo(input: String): String {
        return ""

    }

}


sealed class GuardEvent(val timestamp: LocalDateTime) {

    class Wake(timestamp: LocalDateTime) : GuardEvent(timestamp)
    class Sleep(timestamp: LocalDateTime) : GuardEvent(timestamp)
    class Shift(timestamp: LocalDateTime, val guard: Int) : GuardEvent(timestamp)

    companion object {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        fun parse(str: String): GuardEvent {
            val parsed = str.drop(1).split("] ")
            val date = LocalDateTime.parse(parsed[0], formatter)
            val event = parsed[1].split(' ')
            return when (event[0][0]) {
                'G' -> Shift(date, event[1].drop(1).toInt())
                'f' -> Sleep(date)
                'w' -> Wake(date)
                else -> throw IllegalArgumentException()
            }
        }
    }
}

