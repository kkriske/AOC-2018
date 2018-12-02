package aoc

data class ApplicationParameters(val day: Int, val sessionToken: String)

fun applicationParameters(args: Array<String>): ApplicationParameters {
    if (args.size != 2) {
        throw IllegalArgumentException("Expected 2 arguments (first is session token, second is day number")
    }

    val sessionToken = args[0]
    val dayString = args[1]
    val day = Integer.valueOf(dayString)!!

    return ApplicationParameters(day, sessionToken)
}
