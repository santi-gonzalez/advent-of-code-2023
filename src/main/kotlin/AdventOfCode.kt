import instrumentation.Challenge
import instrumentation.ChallengeFactory
import instrumentation.FileReader

fun main(args: Array<String>) {
    try {
//        val day: Int = args[0].toInt()
//        val letter: Char = args[1].toCharArray()[0]
//        val example: Boolean = args[2].toBoolean()
        val day = 1
        val letter = 'a'
        val example = true
        val source: String = FileReader.readTextFile(day, letter, example)
        val challenge: Challenge = ChallengeFactory.getChallenge(day, letter, source)
        val result: String = challenge.solve()
        println("Result for day $day-$letter: $result")
    } catch (exception: NumberFormatException) {
        println("Error: Challenge not found")
    }
}
