package challenges

import challenges.challenge5.Ch5Parser
import instrumentation.Challenge

class Challenge5A(private val source: String) : Challenge {
    override fun solve(): String = Ch5Parser.parse(source).let { data ->
        data.seeds.minOf { seed -> data.mapSeedToLocation(seed) }.toString()
    }
}
