package instrumentation

import java.io.InputStream

object FileReader {
    fun readTextFile(day: Int, letter: Char, example: Boolean = false): String = this::class
        .java
        .classLoader
        .getResourceAsStream(
            "${
                if (example) {
                    "examples"
                } else {
                    "challenges"
                }
            }/${
                if (example) {
                    "example"
                } else {
                    "challenge"
                }
            }-${day}-${letter}.txt"
        ).use { stream: InputStream? ->
            stream?.bufferedReader().use { it?.readText() } ?: throw IllegalArgumentException("Error: File not found")
        }
}
