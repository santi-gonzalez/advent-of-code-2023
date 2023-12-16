package challenges

import challenges.challenge16.Ch16Parser
import instrumentation.Challenge

class Challenge16A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch16Parser.parse(source).countEnergizedTiles().toString()
}
