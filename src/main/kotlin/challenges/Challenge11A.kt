package challenges

import challenges.challenge11.Ch11Data
import instrumentation.Challenge

class Challenge11A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch11Data(source).expandUniverse().countDistances().toString()
}
