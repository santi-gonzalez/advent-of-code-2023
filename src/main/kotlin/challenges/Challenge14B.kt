package challenges

import challenges.challenge14.Ch14Parser
import instrumentation.Challenge

class Challenge14B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch14Parser.parse(source).spinCycle().geNorthLoad().toString()
}
