package challenges

import challenges.challenge9.Ch9Parser
import instrumentation.Challenge

class Challenge9B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch9Parser.parse(source).getPreviousExtrapolationSum().toString()
}
