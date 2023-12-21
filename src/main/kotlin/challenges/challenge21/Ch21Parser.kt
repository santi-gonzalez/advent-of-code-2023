package challenges.challenge21

object Ch21Parser {
    fun parse(source: String): Ch21Data =
        Ch21Data(source.lines().filter { it.isNotEmpty() })
}
