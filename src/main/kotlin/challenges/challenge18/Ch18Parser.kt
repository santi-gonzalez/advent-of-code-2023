package challenges.challenge18

object Ch18Parser {
    fun parse(source: String) = Ch18Data(
        source.lines().filter { it.isNotEmpty() }.map { line ->
            val (dir, count, color) = line.split(" ")
            Triple(dir[0], count.toInt(), color)
        }
    )
}
