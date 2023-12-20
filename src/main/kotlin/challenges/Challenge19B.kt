package challenges

import challenges.challenge19.Ch19Parser
import instrumentation.Challenge

class Challenge19B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch19Parser.parse(source).getCompatiblePartsCount().toString()
}
