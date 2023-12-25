package challenges.challenge24

object Ch24Parser {
    fun parse(source: String): Ch24Data = Ch24Data(source.lines().filter { it.isNotEmpty() }.map { line ->
        val (position, velocity) = line.replace(" ", "").split("@")
        val (x, y, z) = position.split(",")
        val (vx, vy, vz) = velocity.split(",")
        Ch24Data.Ch24Hailstone(
            x.toString().toBigDecimal(), y.toString().toBigDecimal(), z.toString().toBigDecimal(),
            vx.toString().toBigDecimal(), vy.toString().toBigDecimal(), vz.toBigDecimal()
        )
    })
}
