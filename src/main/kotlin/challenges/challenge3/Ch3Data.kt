package challenges.challenge3

data class Ch3Data(
    val numberBundle: List<NumberBundle>,
    val symbolBundle: List<SymbolBundle>
) {
    val symbolsPosition = symbolBundle.map { Pair(it.position.first, it.position.second) }

    data class NumberBundle(val position: Pair<Int, Int>, val number: Int) {
        val length: Int = number.toString().length
        fun getPositions(): List<Pair<Int, Int>> {
            val pairs = mutableListOf<Pair<Int, Int>>()
            for (columnOffset in 0 until length) {
                pairs.add(Pair(position.first, position.second + columnOffset))
            }
            return pairs
        }
    }

    data class SymbolBundle(val position: Pair<Int, Int>, val isPotentialGear: Boolean)
}
