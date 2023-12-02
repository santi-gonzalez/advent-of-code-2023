package challenges

import challenges.challenge2.Ch2Game
import challenges.challenge2.Ch2Parser
import instrumentation.Challenge

class Challenge2A(private val source: String) : Challenge {
    private val maxRed = 12
    private val maxGreen = 13
    private val maxBlue = 14

    override fun solve(): String = Ch2Parser.parse(source).sumOf { game: Ch2Game ->
        game.getValue(maxRed, maxGreen, maxBlue)
    }.toString()
}
