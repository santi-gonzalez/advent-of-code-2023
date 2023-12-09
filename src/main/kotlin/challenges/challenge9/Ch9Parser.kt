package challenges.challenge9

object Ch9Parser {
    fun parse(source: String): Ch9Data = Ch9Data(
        source
            .lines()
            .filter { it.isNotEmpty() }
            .map { line ->
                line
                    .split(" ")
                    .map { it.toLong() }
            })
}
