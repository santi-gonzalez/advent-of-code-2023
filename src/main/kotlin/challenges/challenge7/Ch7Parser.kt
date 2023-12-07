package challenges.challenge7

object Ch7Parser {
    fun parse(source: String, pathB: Boolean = false): Ch7Data = Ch7Data(source.lines().filter { it.isNotEmpty() }.map { line ->
        line.split(" ").let { (hand, bid) ->
            Pair(hand, bid.toLong())
        }
    },
        pathB = pathB
    )
}
