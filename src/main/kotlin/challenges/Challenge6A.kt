package challenges

import challenges.challenge6.Ch6Data
import challenges.challenge6.Ch6Parser
import instrumentation.Challenge

class Challenge6A(private val source: String) : Challenge {
    override fun solve(): String = multiplyWaysOfWinning(Ch6Parser.parse(source)).toString()

    private fun multiplyWaysOfWinning(data: Ch6Data): Int =
        data.races.map { race -> race.getWaysOfWinning() }.reduce { acc, element -> acc * element }
}
