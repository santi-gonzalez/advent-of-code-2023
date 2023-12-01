package challenges

import instrumentation.Challenge

class Challenge1A(private val source: String) : Challenge {
    override fun solve(): String = source.lines().filter { it.isNotEmpty() }.sumOf { line: String ->
        val first: Int = line.first { it.isDigit() }.toString().toInt()
        val last: Int = line.last { it.isDigit() }.toString().toInt()
        val calibration: Int = first * 10 + last
        calibration
    }.toString()
}
