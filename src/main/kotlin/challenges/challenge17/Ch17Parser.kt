package challenges.challenge17

object Ch17Parser {
    fun parse(source: String): Ch17Data =
        Ch17Data(source.lines().filter { it.isNotEmpty() })
}
