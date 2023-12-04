package challenges.challenge4

object Ch4Parser {
    fun parse(source: String): List<Ch4Data> = source.lines().filter { it.isNotEmpty() }.map { line ->
        val (gameString, numbersString) = line.split(":")
        val gameNumber = gameString.filter { it.isDigit() }.toInt()
        val (winningsString, ownedString) = numbersString.split("|")
        val winningNumbers = winningsString.split(" ").mapNotNull { it.toIntOrNull() }
        val ownedNumbers = ownedString.split(" ").mapNotNull { it.toIntOrNull() }
        println("Game $gameNumber")
        println("Won: $winningNumbers")
        println("Owned: $ownedNumbers")
        Ch4Data(gameNumber, winningNumbers, ownedNumbers)
    }
}
