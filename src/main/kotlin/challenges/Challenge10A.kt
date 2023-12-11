package challenges

import challenges.challenge10.Ch10Parser
import instrumentation.Challenge

class Challenge10A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch10Parser.parse(source).getFurthestDistance().toString()
}
