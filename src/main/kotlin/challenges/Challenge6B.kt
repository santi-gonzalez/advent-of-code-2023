package challenges

import challenges.challenge6.Ch6Parser
import instrumentation.Challenge

class Challenge6B(private val source: String) : Challenge {
    override fun solve(): String = Ch6Parser.parse(source).races[0].getWaysOfWinning().toString()
}
