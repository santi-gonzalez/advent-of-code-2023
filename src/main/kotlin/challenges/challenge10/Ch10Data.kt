package challenges.challenge10

import challenges.challenge10.Ch10Data.Ch10Direction.BOTTOM
import challenges.challenge10.Ch10Data.Ch10Direction.LEFT
import challenges.challenge10.Ch10Data.Ch10Direction.NONE
import challenges.challenge10.Ch10Data.Ch10Direction.RIGHT
import challenges.challenge10.Ch10Data.Ch10Direction.TOP
import java.util.*

data class Ch10Data(
    private val size: Int,
    private val tileMap: Array<Array<Ch10Tile>>,
    private val expandedMap: Array<Array<Int>>,
    private val start: Pair<Int, Int>,
) {
    private val pathMap: Array<Array<Int>> = Array(size) { Array(size) { 0 } }
    private val reducedMap: MutableList<MutableList<Int>> = mutableListOf()
    private var pipeLength = 0

    fun getFurthestDistance(): Int = traverseMainPipe().let { pipeLength / 2 }

    fun countInsideGround(): Int {
        traverseMainPipe()
        findOutsideGround()
        reduce()
        return countInsideGroundInternal()
    }

    private fun traverseMainPipe() {
        var currentPosition: Pair<Int, Int> = start
        var fromDirection: Ch10Direction
        markMainPipe(currentPosition)
        resolveStartPosition(currentPosition).let { (nextPosition, previousDirection) ->
            currentPosition = nextPosition
            fromDirection = previousDirection
            pipeLength++
            markMainPipe(currentPosition)
        }
        while (currentPosition != start) {
            val nextDirection = getTile(currentPosition).getNextDirection(fromDirection)
            currentPosition = getNextPosition(currentPosition, nextDirection)
            fromDirection = nextDirection.reverse()
            pipeLength++
            markMainPipe(currentPosition)
        }
    }

    private fun markMainPipe(position: Pair<Int, Int>) {
        pathMap[position.first][position.second] = 1
    }

    @Suppress("unused")
    private fun findOutsideGroundRecursive(row: Int = 0, column: Int = 0): Int {
        var count = 0
        if (expandedMap[row][column] == 1) count++
        expandedMap[row][column] = 2
        if (row + 1 < expandedMap.size && expandedMap[row + 1][column] < 2) {
            count += findOutsideGroundRecursive(row + 1, column)
        }
        if (row - 1 > 0 && expandedMap[row + -1][column] < 2) {
            count += findOutsideGroundRecursive(row - 1, column)
        }
        if (column + 1 < expandedMap[0].size && expandedMap[row][column + 1] < 2) {
            count += findOutsideGroundRecursive(row, column + 1)
        }
        if (column - 1 > 0 && expandedMap[row][column - 1] < 2) {
            count += findOutsideGroundRecursive(row, column - 1)
        }
        return count
    }

    private fun findOutsideGround(recursive: Boolean = false): Int =
        if (recursive) findOutsideGroundRecursive() else findOutsideGroundIterative()

    private fun resolveStartPosition(currentPosition: Pair<Int, Int>) = when {
        currentPosition.first > 0 && getNextTile(currentPosition, TOP).hasConnectionTo(BOTTOM) ->
            getNextPosition(currentPosition, TOP) to BOTTOM

        currentPosition.first < tileMap.size - 1 && getNextTile(currentPosition, BOTTOM).hasConnectionTo(TOP) ->
            getNextPosition(currentPosition, BOTTOM) to TOP

        currentPosition.second > 0 && getNextTile(currentPosition, LEFT).hasConnectionTo(RIGHT) ->
            getNextPosition(currentPosition, LEFT) to RIGHT

        currentPosition.second < tileMap[0].size - 1 && getNextTile(currentPosition, RIGHT).hasConnectionTo(LEFT) ->
            getNextPosition(currentPosition, RIGHT) to LEFT

        else -> throw IllegalStateException()
    }

    private fun getTile(position: Pair<Int, Int>): Ch10Tile =
        getTile(position.first, position.second)

    private fun getTile(rowIndex: Int, columnIndex: Int): Ch10Tile = tileMap[rowIndex][columnIndex]

    private fun getNextTile(position: Pair<Int, Int>, direction: Ch10Direction): Ch10Tile =
        getNextTile(position.first, position.second, direction)

    private fun getNextTile(rowIndex: Int, columnIndex: Int, direction: Ch10Direction): Ch10Tile =
        getTile(getNextPosition(rowIndex, columnIndex, direction))

    private fun getNextPosition(position: Pair<Int, Int>, direction: Ch10Direction): Pair<Int, Int> =
        getNextPosition(position.first, position.second, direction)

    private fun getNextPosition(rowIndex: Int, columnIndex: Int, direction: Ch10Direction): Pair<Int, Int> {
        val (newRowIndex, newColumnIndex) = when (direction) {
            TOP -> rowIndex - 1 to columnIndex
            BOTTOM -> rowIndex + 1 to columnIndex
            LEFT -> rowIndex to columnIndex - 1
            RIGHT -> rowIndex to columnIndex + 1
            NONE -> rowIndex to columnIndex
        }
        return newRowIndex to newColumnIndex
    }

    private fun findOutsideGroundIterative(): Int {
        var count = 0
        val stack = Stack<Pair<Int, Int>>()
        stack.push(Pair(0, 0))
        while (stack.isNotEmpty()) {
            val (currentRow, currentColumn) = stack.pop()
            if (expandedMap[currentRow][currentColumn] == 1) count++
            expandedMap[currentRow][currentColumn] = 2
            if (currentRow + 1 < expandedMap.size && expandedMap[currentRow + 1][currentColumn] < 2) {
                stack.push(Pair(currentRow + 1, currentColumn))
            }
            if (currentRow - 1 >= 0 && expandedMap[currentRow - 1][currentColumn] < 2) {
                stack.push(Pair(currentRow - 1, currentColumn))
            }
            if (currentColumn + 1 < expandedMap[0].size && expandedMap[currentRow][currentColumn + 1] < 2) {
                stack.push(Pair(currentRow, currentColumn + 1))
            }
            if (currentColumn - 1 >= 0 && expandedMap[currentRow][currentColumn - 1] < 2) {
                stack.push(Pair(currentRow, currentColumn - 1))
            }
        }
        return count
    }

    private fun reduce() {
        val temporalMap = expandedMap
            .filterIndexed { index, _ ->
                index != 0 && index != expandedMap.size - 1
            }.map { array ->
                array.mapIndexedNotNull { index, value ->
                    if (index == 0 || index == array.size - 1) null else value
                }
            }
        reducedMap.clear()
        for (rowIndex in 0 until temporalMap.size / 3) {
            val finalRow: MutableList<Int> = mutableListOf()
            for (colIndex in 0 until temporalMap[rowIndex].size / 3) {
                if (
                    listOf(
                        temporalMap[(rowIndex * 3) + 0][(colIndex * 3) + 0] == 0,
                        temporalMap[(rowIndex * 3) + 1][(colIndex * 3) + 0] == 0,
                        temporalMap[(rowIndex * 3) + 2][(colIndex * 3) + 0] == 0,
                        temporalMap[(rowIndex * 3) + 0][(colIndex * 3) + 1] == 0,
                        temporalMap[(rowIndex * 3) + 1][(colIndex * 3) + 1] == 0,
                        temporalMap[(rowIndex * 3) + 2][(colIndex * 3) + 1] == 0,
                        temporalMap[(rowIndex * 3) + 0][(colIndex * 3) + 2] == 0,
                        temporalMap[(rowIndex * 3) + 1][(colIndex * 3) + 2] == 0,
                        temporalMap[(rowIndex * 3) + 2][(colIndex * 3) + 2] == 0,
                    ).count { it } >= 6
                ) {
                    finalRow.add(1)
                } else {
                    finalRow.add(0)
                }
            }
            reducedMap.add(finalRow)
        }
    }

    private fun countInsideGroundInternal(): Int =
        reducedMap.sumOf { row -> row.count { it == 1 } }

    fun print() {
        tileMap.forEach { rows ->
            rows.forEach { tile ->
                print(tile)
                print(" ")
            }
            println()
        }
        println()

        pathMap.forEach { rows ->
            rows.forEach { paths ->
                print(paths)
                print(" ")
            }
            println()
        }
        println()

        expandedMap.forEach { rows ->
            rows.forEach { tiles ->
                print(tiles)
                print(" ")
            }
            println()
        }
        println()

        reducedMap.forEach { rows ->
            rows.forEach { tiles ->
                print(tiles)
                print(" ")
            }
            println()
        }
        println()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ch10Data

        if (!tileMap.contentDeepEquals(other.tileMap)) return false
        if (!pathMap.contentDeepEquals(other.pathMap)) return false
        if (!expandedMap.contentDeepEquals(other.expandedMap)) return false
        if (start != other.start) return false
        if (pipeLength != other.pipeLength) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tileMap.contentDeepHashCode()
        result = 31 * result + pathMap.contentDeepHashCode()
        result = 31 * result + expandedMap.contentDeepHashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + pipeLength
        return result
    }

    sealed class Ch10Tile {
        abstract fun hasConnectionTo(direction: Ch10Direction): Boolean
        // returns the other direction different from the given one
        abstract fun getNextDirection(from: Ch10Direction): Ch10Direction
        // returns 9 integers in order:
        // [top-left,mid-left,bot-left,top-mid,mid-mid,bot-mid,top-right,mid-right,bot-right]
        abstract fun expand(): List<Int>

        object Ground : Ch10Tile() {
            override fun hasConnectionTo(direction: Ch10Direction): Boolean = false
            override fun getNextDirection(from: Ch10Direction): Ch10Direction = NONE
            override fun expand(): List<Int> = listOf(0, 0, 0, 0, 1, 0, 0, 0, 0)
            override fun toString(): String = GROUND.toString()
        }

        object Vertical : Ch10Tile() {
            override fun hasConnectionTo(direction: Ch10Direction): Boolean = when (direction) {
                TOP -> true
                BOTTOM -> true
                LEFT,
                RIGHT,
                NONE -> false
            }

            override fun getNextDirection(from: Ch10Direction): Ch10Direction = when (from) {
                TOP -> BOTTOM
                BOTTOM -> TOP
                LEFT,
                RIGHT,
                NONE -> throw IllegalArgumentException()
            }

            override fun expand(): List<Int> = listOf(0, 0, 0, 3, 3, 3, 0, 0, 0)
            override fun toString(): String = VERTICAL.toString()
        }

        object Horizontal : Ch10Tile() {
            override fun hasConnectionTo(direction: Ch10Direction): Boolean = when (direction) {
                LEFT -> true
                RIGHT -> true
                TOP,
                BOTTOM,
                NONE -> false
            }

            override fun getNextDirection(from: Ch10Direction): Ch10Direction = when (from) {
                LEFT -> RIGHT
                RIGHT -> LEFT
                TOP,
                BOTTOM,
                NONE -> throw IllegalArgumentException()
            }

            override fun expand(): List<Int> = listOf(0, 3, 0, 0, 3, 0, 0, 3, 0)
            override fun toString(): String = HORIZONTAL.toString()
        }

        object TopLeft : Ch10Tile() {
            override fun hasConnectionTo(direction: Ch10Direction): Boolean = when (direction) {
                TOP -> true
                LEFT -> true
                BOTTOM,
                RIGHT,
                NONE -> false
            }

            override fun getNextDirection(from: Ch10Direction): Ch10Direction = when (from) {
                TOP -> LEFT
                LEFT -> TOP
                BOTTOM -> throw IllegalArgumentException()
                RIGHT -> throw IllegalArgumentException()
                NONE -> throw IllegalArgumentException()
            }

            override fun expand(): List<Int> = listOf(0, 3, 0, 3, 3, 0, 0, 0, 0)
            override fun toString(): String = TOP_LEFT.toString()
        }

        object TopRight : Ch10Tile() {
            override fun hasConnectionTo(direction: Ch10Direction): Boolean = when (direction) {
                TOP -> true
                RIGHT -> true
                LEFT,
                BOTTOM,
                NONE -> false
            }

            override fun getNextDirection(from: Ch10Direction): Ch10Direction = when (from) {
                TOP -> RIGHT
                RIGHT -> TOP
                BOTTOM,
                LEFT,
                NONE -> throw IllegalArgumentException()
            }

            override fun expand(): List<Int> = listOf(0, 0, 0, 3, 3, 0, 0, 3, 0)
            override fun toString(): String = TOP_RIGHT.toString()
        }

        object BottomLeft : Ch10Tile() {
            override fun hasConnectionTo(direction: Ch10Direction): Boolean = when (direction) {
                LEFT -> true
                BOTTOM -> true
                TOP,
                RIGHT,
                NONE -> false
            }

            override fun getNextDirection(from: Ch10Direction): Ch10Direction = when (from) {
                BOTTOM -> LEFT
                LEFT -> BOTTOM
                TOP,
                RIGHT,
                NONE -> throw IllegalArgumentException()
            }

            override fun expand(): List<Int> = listOf(0, 3, 0, 0, 3, 3, 0, 0, 0)
            override fun toString(): String = BOTTOM_LEFT.toString()
        }

        object BottomRight : Ch10Tile() {
            override fun hasConnectionTo(direction: Ch10Direction): Boolean = when (direction) {
                RIGHT -> true
                BOTTOM -> true
                LEFT,
                TOP,
                NONE -> false
            }

            override fun getNextDirection(from: Ch10Direction): Ch10Direction = when (from) {
                BOTTOM -> RIGHT
                RIGHT -> BOTTOM
                TOP,
                LEFT,
                NONE -> throw IllegalArgumentException()
            }

            override fun expand(): List<Int> = listOf(0, 0, 0, 0, 3, 3, 0, 3, 0)
            override fun toString(): String = BOTTOM_RIGHT.toString()
        }

        object Start : Ch10Tile() {
            override fun hasConnectionTo(direction: Ch10Direction): Boolean = true
            override fun getNextDirection(from: Ch10Direction): Ch10Direction = NONE
            override fun expand(): List<Int> = listOf(0, 3, 0, 3, 3, 3, 0, 3, 0)
            override fun toString(): String = START.toString()
        }

        companion object {
            fun from(tile: Char): Ch10Tile = when (tile) {
                VERTICAL -> Vertical
                HORIZONTAL -> Horizontal
                TOP_LEFT -> TopLeft
                TOP_RIGHT -> TopRight
                BOTTOM_LEFT -> BottomLeft
                BOTTOM_RIGHT -> BottomRight
                START -> Start
                else -> Ground
            }
        }
    }

    enum class Ch10Direction {
        TOP, BOTTOM, LEFT, RIGHT, NONE, ;

        fun reverse(): Ch10Direction = when (this) {
            TOP -> BOTTOM
            BOTTOM -> TOP
            LEFT -> RIGHT
            RIGHT -> LEFT
            NONE -> NONE
        }
    }

    private companion object {
        const val GROUND = '.'
        const val VERTICAL = '|'
        const val HORIZONTAL = '-'
        const val TOP_LEFT = 'J'
        const val TOP_RIGHT = 'L'
        const val BOTTOM_LEFT = '7'
        const val BOTTOM_RIGHT = 'F'
        const val START = 'S'
    }
}
