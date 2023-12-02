package challenges.challenge2

object Ch2Parser {
    fun parse(source: String): List<Ch2Game> {
        val gamePattern = Regex(pattern = "Game (\\d+): (.+)")
        val redPattern = Regex(pattern = "(\\d+) red")
        val greenPattern = Regex(pattern = "(\\d+) green")
        val bluePattern = Regex(pattern = "(\\d+) blue")
        return source.lines().filter { it.isNotEmpty() }.map { line ->
            gamePattern.find(line)!!.let { gameMatch: MatchResult ->
                val gameNumber: Int = gameMatch.groupValues[1].toInt()
                val drawString: String = gameMatch.groupValues[2]
                val draws: List<Ch2Game.Draw> = drawString.split("; ").map { draw: String ->
                    val red: Int = redPattern.find(draw)?.groupValues?.get(1)?.toIntOrNull() ?: 0
                    val green: Int = greenPattern.find(draw)?.groupValues?.get(1)?.toIntOrNull() ?: 0
                    val blue: Int = bluePattern.find(draw)?.groupValues?.get(1)?.toIntOrNull() ?: 0
                    Ch2Game.Draw(red, green, blue)
                }
                Ch2Game(gameNumber, draws)
            }
        }
    }
}
