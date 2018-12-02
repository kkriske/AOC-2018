package aoc.services

import aoc.Configuration
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

/**
 * Get input to solve challenges.
 */
class ChallengeInputService(private val requestService: RequestService) {

	/**
	 * Retrieve the assignment data for a specific day.
	 * @param day the day to retrieve data for
	 * @return the data as a String
	 */
	fun getInput(day: Int): Single<String> {
		return if (getLocalFile(day).exists()) {
			getInputFromLocalFile(day)
		} else {
			getInputFromWebsite(day)
		}
	}

	/**
	 * Retrieve the assignment data for a specific day from a local file.
	 * @param day the day to retrieve data for
	 * @return the data as a String
	 */
	private fun getInputFromLocalFile(day: Int): Single<String> {
		return Single.just(day)
				.observeOn(Schedulers.io())
				.map(::getLocalFile)
				.map(File::readBytes)
				.map { String(it) }
	}

	/**
	 * Retrieve the assignment data for a specific day from the website.
	 * @param day the day to retrieve data for
	 * @return the data as a String
	 */
	private fun getInputFromWebsite(day: Int): Single<String> {
		val url = getUrl(day)
		val request = requestService.requestBuilder()
				.url(url)
				.build()

		return requestService.executeForString(request)
				.map(::stripNewline)
				.doOnSuccess { storeInputToFile(it, day) }
	}
}

/**
 * Strips the last character if it is a newline character and returns the resulting String.
 */
private fun stripNewline(text: String): String {
	return if (text.endsWith('\n')) {
		text.substring(0, text.length - 1)
	} else {
		text
	}
}

/**
 * Store the text input for the specified day.
 */
private fun storeInputToFile(text: String, day: Int) {
	// Write data to local cache
	val file = getLocalFile(day)

	if (!file.exists()) {
		file.createNewFile()
	}

	// Assignment data is not very big, so we can write it unbuffered
	val outputStream = FileOutputStream(file)
	outputStream.write(text.toByteArray())
	outputStream.close()
}

/**
 * Get the local file to store the local data.
 * The file might not exist yet.

 * @param day the day to retrieve File for
 * *
 * @return the file (which might not exist yet)
 */
private fun getLocalFile(day: Int): File {
	val directory = File("data")
	val filename = getFilename(day)

	if (!directory.isDirectory) {
		directory.mkdir()
	}

	return File(directory, filename)
}

private fun getFilename(day: Int): String = "${Configuration.year} day $day.txt"

private fun getUrl(day: Int) = "https://adventofcode.com/${Configuration.year}/day/$day/input"