package aoc

import io.reactivex.Single
import aoc.profiling.Profiler
import aoc.services.ChallengeInputService
import aoc.services.RequestService
import aoc.services.SessionService
import aoc.solutions.day1.Day1Solution
import aoc.solutions.day2.Day2Solution
import aoc.solutions.day3.Day3Solution
import aoc.solutions.day4.Day4Solution
import aoc.solutions.day5.Day5Solution

open class Application {
    companion object {
        private val requestService = RequestService()
        private val sessionService = SessionService(requestService)
        private val challengeInputService = ChallengeInputService(requestService)
        private val profiler = Profiler()
        private val solutions = arrayOf(
            Day1Solution(),
            Day2Solution(),
            Day3Solution(),
            Day4Solution(),
            Day5Solution()
        )

        @JvmStatic
        fun main(args: Array<String>) {

            println("Advent of Code solutions")
            println()

            try {
                val applicationParameters = applicationParameters(args)

                sessionService.setSessionToken(applicationParameters.sessionToken)

                val dayIndex = applicationParameters.day - 1

                if (dayIndex < 0 || dayIndex > solutions.lastIndex) {
                    println("ERROR: Failed to get solution")
                    return
                }

                val solution = solutions[dayIndex]

                solve(solution).blockingGet()

            } catch (caught: Exception) {
                println("Usage:")
                println("\tjava -jar AdventOfCode.jar [sessionToken] [day]")
            }
        }

        private fun solve(solution: Solution): Single<String> {
            println("Retrieving assignment data...")

            return challengeInputService.getInput(solution.day)
                .map { getSolutionTextFromInput(solution, it) }
                .doOnError { handleSolutionError(it) }
                .doOnSuccess { handleSolutionSuccess(solution, it) }
        }

        private fun getSolutionTextFromInput(solution: Solution, input: String): String {
            println("Solving assignment...")
            profiler.start("Solution Part 1")
            val resultPartOne = solution.solvePartOne(input)
            profiler.stop("Solution Part 1")

            profiler.start("Solution Part 2")
            val resultPartTwo = solution.solvePartTwo(input)
            profiler.stop("Solution Part 2")

            return " - part 1: $resultPartOne\n - part 2: $resultPartTwo"
        }

        private fun handleSolutionError(caught: Throwable) {
            println("error when solving assignment")

            val message = caught.message

            if (message != null) {
                println("Solution failed: $message")
            }

            caught.printStackTrace()
        }

        private fun handleSolutionSuccess(solution: Solution, output: String) {
            println("Solution for day ${solution.day}:\n$output")
        }
    }
}
