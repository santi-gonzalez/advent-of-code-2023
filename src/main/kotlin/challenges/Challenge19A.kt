package challenges

import challenges.challenge19.Ch19Parser
import instrumentation.Challenge
import java.util.Spliterator

class Challenge19A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch19Parser.parse(source).getAllAcceptedPartsSum().toString()
}
