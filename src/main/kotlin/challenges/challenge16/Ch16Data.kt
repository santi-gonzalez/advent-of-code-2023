package challenges.challenge16

import challenges.challenge16.Ch16Data.Direction.*
import challenges.challenge16.Ch16Data.TileKind.*

data class Ch16Data(val lines: List<String>) {
    private val processed: Array<Array<MutableList<Direction>>> = Array(lines.size) { Array(lines[0].length) { mutableListOf() } }
    private val energized: Array<Array<Boolean>> = Array(lines.size) { Array(lines[0].length) { false } }

    fun getMaxEnergizedTiles(): Int {
        var max = 0
        for (rowIndex in lines.indices) max = maxOf(max, countEnergizedTiles(rowIndex, -1, RIGHT))
        for (rowIndex in lines.indices) max = maxOf(max, countEnergizedTiles(rowIndex, lines[0].length, LEFT))
        for (colIndex in lines[0].indices) max = maxOf(max, countEnergizedTiles(-1, colIndex, DOWN))
        for (colIndex in lines[0].indices) max = maxOf(max, countEnergizedTiles(lines.size, colIndex, UP))
        return max
    }

    fun countEnergizedTiles(row: Int = 0, col: Int = -1, dir: Direction = RIGHT): Int {
        initialize()
        computeBean(row, col, dir)
        return energized.sumOf { line -> line.count { isEnergized -> isEnergized } }
    }

    private fun initialize() {
        for (rowIndex in lines.indices) {
            for (colIndex in lines[0].indices) {
                processed[rowIndex][colIndex] = mutableListOf()
                energized[rowIndex][colIndex] = false
            }
        }
    }

    private fun computeBean(row: Int, col: Int, dir: Direction) {
        if (row in lines.indices && col in lines[0].indices && alreadyProcessed(row, col, dir)) {
            return
        }
        getNextTileOrNull(row, col, dir)?.let { (nextRow, nextCol, tileKind) ->
            energized[nextRow][nextCol] = true
            when (tileKind) {
                GROUND -> {
                    computeBean(nextRow, nextCol, dir)
                }

                RIGHT_GOES_UP_MIRROR -> {
                    when (dir) {
                        UP -> computeBean(nextRow, nextCol, RIGHT)
                        DOWN -> computeBean(nextRow, nextCol, LEFT)
                        LEFT -> computeBean(nextRow, nextCol, DOWN)
                        RIGHT -> computeBean(nextRow, nextCol, UP)
                    }
                }

                RIGHT_GOES_DOWN_MIRROR -> {
                    when (dir) {
                        UP -> computeBean(nextRow, nextCol, LEFT)
                        DOWN -> computeBean(nextRow, nextCol, RIGHT)
                        LEFT -> computeBean(nextRow, nextCol, UP)
                        RIGHT -> computeBean(nextRow, nextCol, DOWN)
                    }
                }

                HORIZONTAL_SPLITTER -> {
                    if (dir.isHorizontal()) {
                        computeBean(nextRow, nextCol, dir)
                    } else {
                        computeBean(nextRow, nextCol, LEFT)
                        computeBean(nextRow, nextCol, RIGHT)
                    }
                }

                VERTICAL_SPLITTER -> {
                    if (dir.isVertical()) {
                        computeBean(nextRow, nextCol, dir)
                    } else {
                        computeBean(nextRow, nextCol, UP)
                        computeBean(nextRow, nextCol, DOWN)
                    }
                }
            }
        }
    }

    private fun alreadyProcessed(row: Int, col: Int, dir: Direction): Boolean =
        processed[row][col].contains(dir).also { processed[row][col].add(dir) }

    private fun getNextTileOrNull(row: Int, col: Int, dir: Direction): Triple<Int, Int, TileKind>? {
        var nextRow = row
        var nextCol = col
        when (dir) {
            UP -> nextRow--
            DOWN -> nextRow++
            LEFT -> nextCol--
            RIGHT -> nextCol++
        }
        return if (nextRow < 0 || nextRow >= lines.size || nextCol < 0 || nextCol >= lines[0].length) {
            null
        } else {
            Triple(nextRow, nextCol, TileKind.from(lines[nextRow][nextCol], dir))
        }
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun isHorizontal() = this == LEFT || this == RIGHT
        fun isVertical() = this == UP || this == DOWN
    }

    enum class TileKind {
        GROUND, RIGHT_GOES_UP_MIRROR, RIGHT_GOES_DOWN_MIRROR, HORIZONTAL_SPLITTER, VERTICAL_SPLITTER;

        companion object {
            fun from(char: Char, dir: Direction): TileKind = when (char) {
                '.' -> GROUND
                '\\' -> RIGHT_GOES_DOWN_MIRROR
                '/' -> RIGHT_GOES_UP_MIRROR
                '-' -> HORIZONTAL_SPLITTER
                '|' -> VERTICAL_SPLITTER
                else -> throw IllegalArgumentException()
            }
        }
    }
}
