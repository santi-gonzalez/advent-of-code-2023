package challenges

import challenges.challenge3.Ch3Data
import challenges.challenge3.Ch3Parser
import instrumentation.Challenge

class Challenge3B(private val source: String) : Challenge {
    override fun solve(): String =
        getGearRatiosSum(Ch3Parser.parse(source)).toString()

    private fun getGearRatiosSum(data: Ch3Data): Int = data.symbolBundle.filter { it.isPotentialGear }.sumOf { bundle ->
        var ratio = 1
        val found: MutableList<Ch3Data.NumberBundle> = mutableListOf()
        for (rowIndex: Int in bundle.position.first - 1..bundle.position.first + 1) {
            for (columnIndex: Int in bundle.position.second - 1..bundle.position.second + 1) {
                data.numberBundle.find {
                    found.contains(it).not() && it.getPositions().contains(Pair(rowIndex, columnIndex))
                }?.let {
                    ratio *= it.number
                    found.add(it)
                }
            }
        }
        if (found.size == 2) println("gear ratio: $ratio")
        if (found.size == 2) ratio else 0
    }

    private fun hasAdjacentNumber(data: Ch3Data, position: Pair<Int, Int>): Boolean {
        return false
    }
}
