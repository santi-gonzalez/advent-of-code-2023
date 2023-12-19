package challenges

import challenges.challenge18.Ch18Parser
import instrumentation.Challenge

class Challenge18B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch18Parser.parse(source).countAvailableTilesFromColors().toString()
}
