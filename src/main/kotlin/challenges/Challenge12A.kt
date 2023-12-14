package challenges

import challenges.challenge12.Ch12Parser
import instrumentation.Challenge

class Challenge12A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch12Parser.parse(source).countArrangements().toString()
}
