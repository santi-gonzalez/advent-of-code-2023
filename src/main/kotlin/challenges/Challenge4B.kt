package challenges

import challenges.challenge4.Ch4Parser
import instrumentation.Challenge

class Challenge4B(private val source: String) : Challenge {
    override fun solve(): String {
        val data = Ch4Parser.parse(source)
        val result = mutableMapOf<Int, Int>()
        for (index in 1..data.size) result[index] = 1
        data.forEach { card ->
            for (index in 1 .. card.matchingCount) {
                result[card.game + index] = result[card.game + index]!! + result[card.game]!!
            }
        }
        return result.values.sum().toString()
    }
}
