package challenges

import challenges.challenge24.Ch24Parser
import instrumentation.Challenge

class Challenge24A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch24Parser.parse(source).print().calculateIntersections().toString()
}
