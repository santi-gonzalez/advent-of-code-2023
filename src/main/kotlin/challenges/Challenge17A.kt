package challenges

import challenges.challenge17.Ch17Parser
import instrumentation.Challenge

class Challenge17A(private val source: String) : Challenge {
    override fun solve(): String =
        Ch17Parser.parse(source).findLeastHeatLoss().toString()
}
