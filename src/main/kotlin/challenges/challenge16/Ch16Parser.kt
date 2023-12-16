package challenges.challenge16

object Ch16Parser {
    fun parse(source: String): Ch16Data =
        Ch16Data(source.lines().filter { it.isNotEmpty() })
}
