package challenges.challenge8

object Ch8Parser {
    fun parse(source: String): Ch8Data {
        val lines = source.lines().filter { it.isNotEmpty() }
        val moves = lines[0]
        val nodes = mutableMapOf<String, Pair<String, String>>()
        for (index in 1 until lines.size) {
            val (nodeTag, paths) = lines[index]
                .replace(" ", "")
                .split("=")
            val (leftNode, rightNode) = paths
                .replace("(", "")
                .replace(")", "")
                .split(",")
            nodes[nodeTag] = Pair(leftNode, rightNode)
        }
        return Ch8Data(moves, nodes)
    }
}
