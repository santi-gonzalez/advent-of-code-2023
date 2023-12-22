package challenges.challenge22

data class Ch22Data(val bricks: Set<Ch22Brick>) {
    private val settledBricks: Set<Ch22Brick>

    init {
        print("making bricks fall... ")
        var tempSettledBricks: MutableSet<Ch22Brick> = mutableSetOf()
        bricks.sortedBy { it.z.first }.forEach { brick: Ch22Brick ->
            tempSettledBricks = fallBrick(brick, tempSettledBricks).first
        }
        println("DONE!")
        println("starting challenge... ")
        settledBricks = tempSettledBricks
    }

    fun countSafeToDisintegrate(): Int = findNonSupportBricks().size

    fun countTotalBrickFalls(): Int = settledBricks
        .minus(findNonSupportBricks().toSet())
        .sumOf { supportBrick: Ch22Brick -> countFallingBricks(supportBrick) }

    private fun fallBrick(
        brick: Ch22Brick,
        settledBricks: MutableSet<Ch22Brick>,
        fallCount: Int = 0
    ): Pair<MutableSet<Ch22Brick>, Int> {
        if (1 in brick.z) {
            return settleBrick(brick, settledBricks, fallCount)
        }
        for (x in brick.x) {
            for (y in brick.y) {
                if (isEmptySpace(x, y, brick.z.first - 1, settledBricks).not()) {
                    return settleBrick(brick, settledBricks, fallCount)
                }
            }
        }
        return fallBrick(
            brick = brick.copy(z = brick.z.first - 1 until brick.z.last),
            settledBricks = settledBricks,
            fallCount = fallCount + 1
        )
    }

    private fun settleBrick(
        brick: Ch22Brick,
        settledBricks: MutableSet<Ch22Brick>,
        fallCount: Int
    ): Pair<MutableSet<Ch22Brick>, Int> = settledBricks.apply { add(brick) } to fallCount

    private fun findNonSupportBricks(): List<Ch22Brick> = settledBricks.mapNotNull { brick ->
        val otherBricks: Set<Ch22Brick> = settledBricks.filter { brick != it }.toSet()
        val relevantBricks: Set<Ch22Brick> = otherBricks.filter { isNextToDisintegratedBrick(brick, it) }.toSet()
        if (
            relevantBricks.all { otherBrick ->
                var stillSafe = false
                outerLoop@ for (x in otherBrick.x) {
                    for (y in otherBrick.y) {
                        if (isEmptySpace(x, y, otherBrick.z.first - 1, otherBricks).not()) {
                            stillSafe = true
                            break@outerLoop
                        }
                    }
                }
                stillSafe
            }
        ) {
            brick
        } else {
            null
        }
    }

    private fun isEmptySpace(x: Int, y: Int, z: Int, settledBricks: Set<Ch22Brick>): Boolean =
        z > 0 && settledBricks.find { x in it.x && y in it.y && z in it.z } == null

    private fun isNextToDisintegratedBrick(brick: Ch22Brick, it: Ch22Brick): Boolean =
        isOverlapping(brick.x, it.x) || isOverlapping(brick.y, it.y) || isOverlapping(brick.z, it.z)

    private fun isOverlapping(baseRange: IntRange, otherRange: IntRange, offset: Int = 1): Boolean {
        val expandedRange = IntRange(baseRange.first - offset, baseRange.last + offset)
        return expandedRange.first <= otherRange.last && otherRange.first <= expandedRange.last
    }

    private fun countFallingBricks(supportBrick: Ch22Brick): Int {
        var newSettledBricks: MutableSet<Ch22Brick> = mutableSetOf()
        val filteredSettledBricks: Set<Ch22Brick> = settledBricks.filter { supportBrick != it }.toSet()
        return filteredSettledBricks.count { filteredBrick: Ch22Brick ->
            val result: Pair<MutableSet<Ch22Brick>, Int> = fallBrick(filteredBrick, newSettledBricks)
            newSettledBricks = result.first
            result.second > 0
        }
    }

    @Suppress("unused")
    private fun print(bricks: List<Ch22Brick>): Ch22Data {
        bricks.forEach { brick ->
            println(brick)
        }
        return this
    }

    data class Ch22Brick(val x: IntRange, val y: IntRange, val z: IntRange)
}
