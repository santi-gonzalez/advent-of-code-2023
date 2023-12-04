package challenges

import challenges.challenge4.Ch4Parser
import instrumentation.Challenge
import kotlin.math.pow

class Challenge4A(private val source: String) : Challenge {
    override fun solve(): String = Ch4Parser.parse(source).sumOf { data ->
        2.0.pow((data.matchingCount - 1).toDouble()).toInt()
    }.toString()
}
