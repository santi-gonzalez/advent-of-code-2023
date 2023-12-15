package challenges

import challenges.challenge15.Ch15Parser
import instrumentation.Challenge

class Challenge15A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch15Parser.parse(source).hashSum().toString()
}
