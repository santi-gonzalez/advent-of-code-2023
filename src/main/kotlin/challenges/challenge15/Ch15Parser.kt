package challenges.challenge15

object Ch15Parser {
    fun parse(source: String): Ch15Data =
        Ch15Data(source.lines()[0].split(","))
}
