package challenges.challenge14

object Ch14Parser {
    fun parse(source: String): Ch14Data = Ch14Data(source.lines().filter { it.isNotEmpty() })
}
