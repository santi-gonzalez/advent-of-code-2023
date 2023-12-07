package challenges

import challenges.challenge7.Ch7Parser
import instrumentation.Challenge

class Challenge7B(private val source: String) : Challenge {
    override fun solve(): String = Ch7Parser.parse(source, pathB = true).getTotalWinnings().toString()
}
