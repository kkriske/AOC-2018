package aoc.profiling

import java.util.*

/**
 * A simple profiler implementation that outputs the time when it is stopped.
 */
class Profiler {
	private val map: MutableMap<String, Long> = HashMap()

	fun start(tag: String) {
		val start = System.currentTimeMillis()
		map.put(tag, start)
	}

	fun stop(tag: String) {
		val stop = System.currentTimeMillis()
		val start = map[tag]

		if (start != null) {
			val duration = stop - start
			println(String.format("\"$tag\" took %.3f seconds", duration / 1000.0))
		}
	}
}
