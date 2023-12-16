package challenges

import challenges.challenge16.Ch16Parser
import instrumentation.Challenge

class Challenge16B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch16Parser.parse(source).getMaxEnergizedTiles().toString()
}
