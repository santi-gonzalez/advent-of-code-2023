package challenges.challenge13

object Ch13Parser {
    fun parse(source: String): Ch13Data {
        val patterns: MutableList<Ch13Data.Ch13Pattern> = mutableListOf()
        val newRows: MutableList<List<Char>> = mutableListOf()
        source.lines().forEach { line ->
            if (line.isNotEmpty()) {
                newRows.add(line.map { it })
            } else {
                patterns.add(Ch13Data.Ch13Pattern(newRows.toList()))
                newRows.clear()
            }
        }
        // add last pattern in case the source file doesn't have an extra white line
        if (newRows.isNotEmpty()) {
            patterns.add(Ch13Data.Ch13Pattern(newRows.toList()))
        }
        return Ch13Data(patterns)
    }
}
