package challenges.challenge20

object Ch20Parser {
    fun parse(source: String): Ch20Data {
        val result = mutableMapOf<String, Pair<Char, MutableList<String>>>()
        source.lines().filter { it.isNotEmpty() }.map { line ->
            val (typeAndName, outputsLine) = line.split(" -> ")
            val (name, char) = when {
                typeAndName.startsWith("%") || typeAndName.startsWith("&") -> typeAndName.drop(1) to typeAndName[0]
                else -> typeAndName to '.'
            }
            val outputsList = outputsLine.replace(" ", "").split(",").toMutableList()
            result[name] = char to outputsList
        }
        return Ch20Data(result)
    }
}
