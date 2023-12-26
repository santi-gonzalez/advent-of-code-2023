package challenges.challenge25

object Ch25Parser {
    fun parse(source: String): Ch25Data {
        val map: MutableMap<String, MutableSet<String>> = mutableMapOf<String, MutableSet<String>>()
        source.lines().filter { it.isNotEmpty() }.forEach { line ->
            val (element: String, connectionsLine: String) = line.split(": ")
            val connections: List<String> = connectionsLine.split(" ")
            connections.forEach { connection ->
                map.getOrPut(element) { mutableSetOf() }.add(connection)
                map.getOrPut(connection) { mutableSetOf() }.add(element)
            }
        }
        return Ch25Data(map)
    }
}
