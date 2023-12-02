package challenges.challenge2

data class Ch2Game(val id: Int, val draws: List<Draw>) {
    fun getValue(maxRed: Int, maxGreen: Int, maxBlue: Int): Int =
        if (isValid(maxRed, maxGreen, maxBlue)) id else 0

    private fun isValid(maxRed: Int, maxGreen: Int, maxBlue: Int): Boolean =
        draws.none { draw: Draw -> draw.red > maxRed || draw.green > maxGreen || draw.blue > maxBlue }

    fun getPower(): Int {
        val red: Int = draws.maxOf { it.red }
        val green: Int = draws.maxOf { it.green }
        val blue: Int = draws.maxOf { it.blue }
        return red * green * blue
    }

    data class Draw(val red: Int, val green: Int, val blue: Int)
}
