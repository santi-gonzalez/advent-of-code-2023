package challenges

import challenges.challenge23.Ch23Parser
import instrumentation.Challenge

class Challenge23A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch23Parser.parse(source).countLongestPathSteps().toString()
}
