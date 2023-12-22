package challenges

import challenges.challenge22.Ch22Parser
import instrumentation.Challenge

class Challenge22A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch22Parser.parse(source).countSafeToDisintegrate().toString()
}
