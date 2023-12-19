package challenges.challenge18

import java.math.BigInteger

data class Ch18Data(val instructions: List<Triple<Char, Int, String>>) {
    private fun buildVertices(movements: List<Pair<Char, Int>>): List<Pair<Int, Int>> {
        val vertices: MutableList<Pair<Int, Int>> = mutableListOf()
        var (x, y) = 0 to 0
        movements.forEach { (direction, count) ->
            when (direction) {
                'U' -> y += count
                'D' -> y -= count
                'L' -> x -= count
                'R' -> x += count
            }
            vertices.add(Pair(x, y))
        }
        return vertices
    }

    // magic shoelace formula
    private fun calculatePolygonArea(coordinates: List<Pair<Int, Int>>): BigInteger {
        var area: BigInteger = BigInteger.ZERO
        for (i in coordinates.indices) {
            val x1: BigInteger = BigInteger.valueOf(coordinates[i].first.toLong())
            val y1: BigInteger = BigInteger.valueOf(coordinates[i].second.toLong())
            val x2: BigInteger = BigInteger.valueOf(coordinates[(i + 1) % coordinates.size].first.toLong())
            val y2: BigInteger = BigInteger.valueOf(coordinates[(i + 1) % coordinates.size].second.toLong())
            area += (x1 * y2) - (x2 * y1)
        }
        return area.abs() / BigInteger.valueOf(2)
    }

    private fun calculatePerimeter(vertices: List<Pair<Int, Int>>): BigInteger {
        var perimeter: BigInteger = BigInteger.ZERO
        for (i in vertices.indices) {
            val (x1, y1) = vertices[i]
            val (x2, y2) = vertices[(i + 1) % vertices.size]
            val bigX1: BigInteger = BigInteger.valueOf(x1.toLong())
            val bigY1: BigInteger = BigInteger.valueOf(y1.toLong())
            val bigX2: BigInteger = BigInteger.valueOf(x2.toLong())
            val bigY2: BigInteger = BigInteger.valueOf(y2.toLong())
            perimeter += (bigX1 - bigX2).abs() + (bigY1 - bigY2).abs()
        }
        return perimeter
    }

    private fun parseInstructions(): List<Pair<Int, Int>> = buildVertices(instructions.map {
        it.first to it.second
    })

    private fun parseColorList(): List<Pair<Int, Int>> = buildVertices(instructions.map {
        val hex = it.third.removePrefix("(#").removeSuffix(")")
        val dir = when (hex.last().toString().toInt()) {
            0 -> 'R'
            1 -> 'D'
            2 -> 'L'
            3 -> 'U'
            else -> throw IllegalArgumentException()
        }
        val count = hex.dropLast(1).toInt(16)
        dir to count
    })

    private fun calculateInteriorArea(list: List<Pair<Int, Int>>): BigInteger {
        val pa: BigInteger = calculatePolygonArea(list)
        val pe: BigInteger = calculatePerimeter(list)
        return pa.plus(pe.divide(BigInteger.TWO)).plus(BigInteger.ONE)
    }

    fun countAvailableTilesFromInstructions(): BigInteger = calculateInteriorArea(parseInstructions())

    fun countAvailableTilesFromColors(): BigInteger = calculateInteriorArea(parseColorList())
}
