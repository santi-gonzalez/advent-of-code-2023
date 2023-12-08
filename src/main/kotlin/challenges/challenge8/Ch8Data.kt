package challenges.challenge8

data class Ch8Data(val moves: String, val nodes: Map<String, Pair<String, String>>) {
    fun countMovesToExit(): Long =
        countMovesToExit("AAA") { nextNode -> nextNode == "ZZZ" }

    fun countGhostMovesToExit(): Long =
        findLestCommonMultiple(
            findStartingNodes().map { node ->
                countMovesToExit(node) { nextNode -> nextNode.endsWith("Z") }
            }
        )

    private fun countMovesToExit(startingNode: String, shouldFinish: (String) -> Boolean): Long {
        var count = 0L
        var currentNode = nodes[startingNode]
        while (true) {
            moves.forEach { move ->
                count++
                val nextNode: String = when (move) {
                    'L' -> currentNode!!.first
                    'R' -> currentNode!!.second
                    else -> throw IllegalArgumentException()
                }
                if (shouldFinish(nextNode)) return count
                currentNode = nodes[nextNode]
            }
        }
    }

    private fun findStartingNodes(): List<String> = nodes.keys.filter { it.endsWith("A") }

    private fun findLestCommonMultiple(numbers: List<Long>): Long =
        numbers.reduce { acc, number -> findLestCommonMultiple(acc, number) }

    private fun findLestCommonMultiple(a: Long, b: Long): Long = a / findGreatestCommonDivisor(a, b) * b

    private fun findGreatestCommonDivisor(a: Long, b: Long): Long {
        if (b == 0L) return a
        return findGreatestCommonDivisor(b, a % b)
    }
}
