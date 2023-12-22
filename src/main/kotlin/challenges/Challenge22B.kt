package challenges

import challenges.challenge22.Ch22Parser
import instrumentation.Challenge

class Challenge22B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch22Parser.parse(source).countTotalBrickFalls().toString()
}
