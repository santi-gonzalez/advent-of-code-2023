package challenges

import challenges.challenge3.Ch3Data
import challenges.challenge3.Ch3Parser
import instrumentation.Challenge

class Challenge3A(private val source: String) : Challenge {
    override fun solve(): String =
        getPartNumbersSum(Ch3Parser.parse(source)).toString()

    private fun getPartNumbersSum(data: Ch3Data): Long {
        var sum = 0L
        data.numberBundle.forEach {
            val fromColum = it.position.second - 1
            val toColumn = it.position.second + it.length
            val fromRow = it.position.first - 1
            val toRow = it.position.first + 1
            var found = false
            loop@ for (column: Int in fromColum..toColumn) {
                for (row: Int in fromRow..toRow) {
                    if (data.symbolsPosition.contains(Pair(row, column))) {
                        found = true
                        break@loop
                    }
                }
            }
            println("${it.number} is $found")
            if (found) sum += it.number
        }
        return sum
    }

}
