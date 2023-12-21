package challenges

import challenges.challenge21.Ch21Parser
import instrumentation.Challenge

class Challenge21A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch21Parser.parse(source).countReachableTiles().toString()
}
