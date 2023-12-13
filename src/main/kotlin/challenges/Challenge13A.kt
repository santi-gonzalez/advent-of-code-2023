package challenges

import challenges.challenge13.Ch13Parser
import instrumentation.Challenge

class Challenge13A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch13Parser.parse(source).getMirrorSummary().toString()
}
