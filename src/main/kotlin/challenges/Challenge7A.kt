package challenges

import challenges.challenge7.Ch7Parser
import instrumentation.Challenge

class Challenge7A(private val source: String) : Challenge {
    override fun solve(): String = Ch7Parser.parse(source).getTotalWinnings().toString()
}
