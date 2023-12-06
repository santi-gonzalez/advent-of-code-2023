package challenges.challenge6

object Ch6Parser {
    fun parse(source: String): Ch6Data {
        val (times, records) = source
            .lines()
            .filter { it.isNotEmpty() }
            .map { line ->
                line
                    .split(" ")
                    .filter { it.toLongOrNull() != null }
            }
        return Ch6Data(
            times.zip(records).map { (time, record) ->
                Ch6Data.Ch6Race(time.toLong(), record.toLong())
            }
        )
    }
}
