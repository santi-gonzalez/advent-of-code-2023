package challenges.challenge23

object Ch23Parser {
    fun parse(source: String): Ch23Data {
        val result: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        val lines: List<String> = source.lines().filter { it.isNotEmpty() }
        lines.forEachIndexed { rowIndex: Int, row: String ->
            row.forEachIndexed { colIndex: Int, char: Char ->
                result[rowIndex to colIndex] = char
            }
        }
        return Ch23Data(result, lines.size)
    }
}
