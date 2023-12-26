package challenges

import challenges.challenge25.Ch25Parser
import instrumentation.Challenge

class Challenge25A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch25Parser.parse(source).solveA().toString()
}
