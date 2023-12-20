package challenges

import challenges.challenge20.Ch20Parser
import instrumentation.Challenge

class Challenge20A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch20Parser.parse(source).multiplyPulses().toString()
}
