package challenges

import challenges.challenge13.Ch13Parser
import instrumentation.Challenge

class Challenge13B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch13Parser.parse(source).getMirrorSummary(findSmudges = true).toString()
}
