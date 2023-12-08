package challenges

import challenges.challenge8.Ch8Parser
import instrumentation.Challenge

class Challenge8B(private val source: String) : Challenge {
    override fun solve(): String = Ch8Parser.parse(source).countGhostMovesToExit().toString()
}
