package challenges

import challenges.challenge12.Ch12Parser
import instrumentation.Challenge

class Challenge12B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch12Parser.parse(source).countArrangements(expanded = true).toString()
    // 1977400964 too low
}
