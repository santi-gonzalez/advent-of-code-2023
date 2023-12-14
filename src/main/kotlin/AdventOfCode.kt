import instrumentation.Challenge
import instrumentation.ChallengeFactory
import instrumentation.FileReader

fun main(args: Array<String>) {
    try {
        val day = 14
        val letter = 'b'
        val example = false
        val source: String = FileReader.readTextFile(day, letter, example)
        val challenge: Challenge = ChallengeFactory.getChallenge(day, letter, source)
        val startTime = System.nanoTime()
        val result: String = challenge.solve()
        val totalTime = System.nanoTime() - startTime
        println("Result for day $day-$letter: $result ($totalTime nanos)")
    } catch (exception: NumberFormatException) {
        println("Error: Challenge not found")
    }
}
