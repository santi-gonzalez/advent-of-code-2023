package challenges

import challenges.challenge2.Ch2Game
import challenges.challenge2.Ch2Parser
import instrumentation.Challenge

class Challenge2B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch2Parser.parse(source).sumOf { game: Ch2Game -> game.getPower() }.toString()
}
