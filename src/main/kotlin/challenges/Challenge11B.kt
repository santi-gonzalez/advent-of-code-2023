package challenges

import challenges.challenge11.Ch11Data
import instrumentation.Challenge

class Challenge11B(private val source: String) : Challenge {
    override fun solve(): String =
        Ch11Data(source).expandUniverse().countDistances(1000000).toString()
}
