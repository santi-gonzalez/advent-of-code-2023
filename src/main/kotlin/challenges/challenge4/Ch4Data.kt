package challenges.challenge4

data class Ch4Data(val game: Int, val winning: List<Int>, val owned: List<Int>) {
    val matchingCount = owned.intersect(winning.toSet()).size
}
