package challenges.challenge11

object Ch11Parser {
    fun parse(source: String): MutableList<MutableList<Ch11Data.Ch11Kind>> {
        val result: MutableList<MutableList<Ch11Data.Ch11Kind>> = mutableListOf()
        source.lines().filter { it.isNotEmpty() }.forEach { line ->
            result.add(line.map { tile ->
                if (tile == '#') Ch11Data.Ch11Kind.GALAXY else Ch11Data.Ch11Kind.VOID
            }.toMutableList())
        }
        return result
    }
}
