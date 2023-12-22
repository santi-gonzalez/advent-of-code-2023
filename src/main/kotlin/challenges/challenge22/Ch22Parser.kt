package challenges.challenge22

object Ch22Parser {
    fun parse(source: String): Ch22Data = Ch22Data(
        source.lines().filter { it.isNotEmpty() }.map { line ->
            val (p1, p2) = line.split("~")
            val (p1x, p1y, p1z) = p1.split(",")
            val (p2x, p2y, p2z) = p2.split(",")
            Ch22Data.Ch22Brick(p1x.toInt()..p2x.toInt(), p1y.toInt()..p2y.toInt(), p1z.toInt()..p2z.toInt())
        }.toSet()
    )
}
