package challenges

import instrumentation.Challenge

class Challenge1B(private val source: String) : Challenge {
    private val numberWords = mapOf(
        "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
        "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
    )

    override fun solve(): String = source.lines().filter { it.isNotEmpty() }.sumOf { line: String ->
        val first: Int = findFirstDigit(line)
        val last: Int = findLastDigit(line)
        val calibration: Int = first * 10 + last
        calibration
    }.toString()

    private fun findFirstDigit(input: String): Int {
        for (index: Int in input.indices) {
            for ((word: String, number: Int) in numberWords) {
                if (input.startsWith(word, index)) {
                    return number
                }
            }
            if (input[index].isDigit()) {
                return Character.getNumericValue(input[index])
            }
        }
        throw IllegalStateException()
    }

    private fun findLastDigit(input: String): Int {
        for (index: Int in input.length downTo 1) {
            for ((word: String, number: Int) in numberWords) {
                if (index >= word.length && input.substring(index - word.length, index) == word) {
                    return number
                }
            }
            if (input[index - 1].isDigit()) {
                return Character.getNumericValue(input[index - 1])
            }
        }
        throw IllegalStateException()
    }
}
