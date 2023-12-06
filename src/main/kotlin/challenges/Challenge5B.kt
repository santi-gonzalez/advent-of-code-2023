package challenges

import challenges.challenge5.Ch5Parser
import instrumentation.Challenge

class Challenge5B(private val source: String) : Challenge {
    override fun solve(): String = Ch5Parser.parse(source, seedPairs = true).let { data ->
        val ranges: List<LongRange> = data.getRanges()
        for (location: Long in 0L..Long.MAX_VALUE) {
            if (ranges.find { it.contains(data.mapLocationToSeed(location)) } != null) {
                return@let location.toString()
            }
        }
        throw IllegalStateException("No location found!")
    }
}
