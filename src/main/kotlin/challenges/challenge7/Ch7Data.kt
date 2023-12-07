package challenges.challenge7

data class Ch7Data(private val _plays: List<Pair<String, Long>>, private val pathB: Boolean) {
    private val rankedPlays: List<Pair<String, Long>> = _plays.sortedWith { o1, o2 ->
        val h1 = o1.first
        val h2 = o2.first
        compareType(h1, h2, pathB).takeIf { it != 0 } ?: compareStrength(h1, h2, pathB)
    }

    fun getTotalWinnings(): Long {
        var result = 0L
        rankedPlays.forEachIndexed { index, play ->
            val rank = index + 1
            result += play.second * rank
        }
        return result
    }

    companion object {
        private const val FIVE_OF_KIND = 7
        private const val FOUR_OF_KIND = 6
        private const val FULL_HOUSE = 5
        private const val THREE_OF_KIND = 4
        private const val DOUBLE_PAIR = 3
        private const val SINGLE_PAIR = 2
        private const val HIGHEST_CARD = 1

        private fun compareType(h1: String, h2: String, pathB: Boolean): Int = getType(h1, pathB) - getType(h2, pathB)

        private fun getType(h: String, pathB: Boolean = true): Int {
            val hMap = mutableMapOf<Char, Int>()
            h.forEach { label -> hMap[label] = (hMap[label] ?: 0) + 1 }
            return getType(hMap, pathB)
        }

        private fun getType(hMap: Map<Char, Int>, pathB: Boolean): Int = with(hMap.values) {
            when {
                contains(5) -> FIVE_OF_KIND
                pathB && contains(4) && hMap['J'] == 1 -> FIVE_OF_KIND
                pathB && contains(3) && hMap['J'] == 2 -> FIVE_OF_KIND
                pathB && contains(2) && hMap['J'] == 3 -> FIVE_OF_KIND
                pathB && hMap['J'] == 4 -> FIVE_OF_KIND
                contains(4) -> FOUR_OF_KIND
                pathB && contains(3) && hMap['J'] == 1 -> FOUR_OF_KIND
                pathB && hMap.keys.size == 3 && hMap['J'] == 2 -> FOUR_OF_KIND
                pathB && hMap['J'] == 3 -> FOUR_OF_KIND
                contains(3) && contains(2) -> FULL_HOUSE
                pathB && count { it == 2 } == 2 && hMap['J'] == 1 -> FULL_HOUSE
                contains(3) -> THREE_OF_KIND
                pathB && contains(2) && hMap['J'] == 1 -> THREE_OF_KIND
                pathB && hMap['J'] == 2 -> THREE_OF_KIND
                count { it == 2 } == 2 -> DOUBLE_PAIR
                contains(2) -> SINGLE_PAIR
                pathB && hMap['J'] == 1 -> SINGLE_PAIR
                else -> HIGHEST_CARD
            }
        }

        fun compareStrength(h1: String, h2: String, pathB: Boolean = true): Int {
            for (index in 0 until 5) {
                val h1Strength = getStrength(h1[index], pathB)
                val h2Strength = getStrength(h2[index], pathB)
                if (h1Strength != h2Strength) {
                    return h1Strength - h2Strength
                }
            }
            throw IllegalStateException("There must be some kind of problem with the input data")
        }

        private fun getStrength(label: Char, pathB: Boolean): Int = when (label) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if (pathB) 1 else 11
            'T' -> 10
            else -> label.toString().toInt()
        }
    }
}
