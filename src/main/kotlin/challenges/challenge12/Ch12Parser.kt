package challenges.challenge12

object Ch12Parser {
    fun parse(source: String) = Ch12Data(source.lines().filter { it.isNotEmpty() }.map { line ->
        line.split(" ").let { (springs, groups) ->
            springs to groups.split(",").map { it.toInt() }
        }
    })
}
