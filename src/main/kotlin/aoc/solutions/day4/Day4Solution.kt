package aoc.solutions.day4

import aoc.Solution
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Day4Solution : Solution(4) {

    override fun solvePartOne(input: String): String {
        val asleep = input.lineSequence()
            .map { GuardEvent.parse(it) }
            .sortedBy { it.timestamp }
            .mapGuardToMinutesAsleep()

        val mostAsleepEntry = asleep.maxBy { it.value.count() } ?: throw IllegalStateException()

        val guard = mostAsleepEntry.key
        val minute = mostAsleepEntry.value
            .groupingBy { it }
            .eachCount()
            .maxBy { it.value }?.key ?: throw IllegalStateException()

        return (guard * minute).toString()
    }

    override fun solvePartTwo(input: String): String {
        val asleep = input.lineSequence()
            .map { GuardEvent.parse(it) }
            .sortedBy { it.timestamp }
            .mapGuardToMinutesAsleep()

        val entry = asleep.flatMap { entry ->
            entry.value.groupingBy { it }
                .eachCount()
                .map { Triple(entry.key, it.value, it.key) } // guard, count, minute
        }.maxBy { it.second } ?: throw IllegalStateException()

        return (entry.first * entry.third).toString()
    }

}

fun Sequence<GuardEvent>.mapGuardToMinutesAsleep(): Map<Int, List<Int>> {
    var guard = 0
    var sleepTime = LocalDateTime.now()
    val asleep = mutableMapOf<Int, MutableList<Int>>()
    forEach { event ->
        when (event) {
            is GuardEvent.Shift -> guard = event.guard
            is GuardEvent.Sleep -> sleepTime = event.timestamp
            is GuardEvent.Wake -> {
                val list = asleep.computeIfAbsent(guard) { mutableListOf() }
                (sleepTime.minute until event.timestamp.minute).forEach { list.add(it) }
            }
        }
    }
    return asleep
}


sealed class GuardEvent(val timestamp: LocalDateTime) {

    class Wake(timestamp: LocalDateTime) : GuardEvent(timestamp)
    class Sleep(timestamp: LocalDateTime) : GuardEvent(timestamp)
    class Shift(timestamp: LocalDateTime, val guard: Int) : GuardEvent(timestamp)

    fun print() {
        when (this) {
            is GuardEvent.Wake -> println("$timestamp Wake")
            is GuardEvent.Sleep -> println("$timestamp Sleep")
            is GuardEvent.Shift -> println("$timestamp Shift $guard")
        }
    }

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

