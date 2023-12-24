package challenges

import challenges.challenge23.Ch23Parser
import instrumentation.Challenge

class Challenge23B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch23Parser.parse(source).countLongestPathStepsNoSlopes().toString()
}
