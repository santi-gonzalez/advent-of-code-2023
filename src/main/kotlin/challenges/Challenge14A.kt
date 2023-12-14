package challenges

import challenges.challenge14.Ch14Data.Direction.NORTH
import challenges.challenge14.Ch14Parser
import instrumentation.Challenge

class Challenge14A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch14Parser.parse(source).tilt(NORTH).geNorthLoad().toString()
}
