package challenges.challenge13

import kotlin.math.min

data class Ch13Data(private val patterns: List<Ch13Pattern>) {
    fun getMirrorSummary(findSmudges: Boolean = false): Int =
        findVerticalMirrorIndexes(findSmudges).sumOf { it ?: 0 } +
                100 * findHorizontalMirrorIndexes(findSmudges).sumOf { it ?: 0 }

    private fun findVerticalMirrorIndexes(findSmudges: Boolean): List<Int?> = patterns.map { pattern ->
        val baseIndex = pattern.findVerticalMirrorIndex()
        if (findSmudges.not()) {
            return@map baseIndex
        } else {
            for (variation: Ch13Pattern in pattern.generateVariations()) {
                variation.findVerticalMirrorIndex(baseIndex)?.let { return@map it }
            }
            null
        }
    }

    private fun findHorizontalMirrorIndexes(findSmudges: Boolean): List<Int?> = patterns.map { pattern ->
        val baseIndex = pattern.findHorizontalMirrorIndex()
        if (findSmudges.not()) {
            return@map baseIndex
        } else {
            for (variation: Ch13Pattern in pattern.generateVariations()) {
                variation.findHorizontalMirrorIndex(baseIndex)?.let { return@map it }
            }
            null
        }
    }

    data class Ch13Pattern(private val tiles: List<List<Char>>) {
        fun findHorizontalMirrorIndex(disregardIndex: Int? = null): Int? {
            for (rowIndex in 1 until tiles.size) {
                if (isMirrorRow(rowIndex, disregardIndex)) {
                    return rowIndex
                }
            }
            return null
        }

        fun findVerticalMirrorIndex(disregardIndex: Int? = null): Int? {
            for (colIndex in 1 until tiles[0].size) {
                if (isMirrorColumn(colIndex, disregardIndex)) {
                    return colIndex
                }
            }
            return null
        }

        private fun isMirrorColumn(columnIndex: Int, disregardIndex: Int?): Boolean {
            if (columnIndex == disregardIndex) {
                return false
            }
            val range = min(columnIndex, tiles[0].size - columnIndex)
            for (columnOffset in 0 until range) {
                if (!areEqualColumns(
                        colIndex1 = columnIndex - (1 + columnOffset),
                        colIndex2 = columnIndex + columnOffset
                    )
                ) {
                    return false
                }
            }
            return true
        }

        private fun isMirrorRow(rowIndex: Int, disregardIndex: Int?): Boolean {
            if (rowIndex == disregardIndex) {
                return false
            }
            val range = min(rowIndex, tiles.size - rowIndex)
            for (rowOffset in 0 until range) {
                if (!areEqualRows(
                        rowIndex1 = rowIndex - (1 + rowOffset),
                        rowIndex2 = rowIndex + rowOffset
                    )
                ) {
                    return false
                }
            }
            return true
        }

        private fun areEqualColumns(colIndex1: Int, colIndex2: Int): Boolean {
            for (rowIndex in tiles.indices) {
                if (tiles[rowIndex][colIndex1] != tiles[rowIndex][colIndex2]) return false
            }
            return true
        }

        private fun areEqualRows(rowIndex1: Int, rowIndex2: Int): Boolean {
            for (colIndex in tiles[0].indices) {
                if (tiles[rowIndex1][colIndex] != tiles[rowIndex2][colIndex]) return false
            }
            return true
        }

        // improve: we could really generate variations on-the-fly when necessary
        fun generateVariations(): List<Ch13Pattern> {
            val variations = mutableListOf<List<List<Char>>>()
            for (rowIndex in tiles.indices) {
                for (colIndex in tiles[0].indices) {
                    val newTiles = tiles.map { it.toMutableList() }.toMutableList()
                    newTiles[rowIndex][colIndex] = if (newTiles[rowIndex][colIndex] == ASH) ROCKS else ASH
                    variations.add(newTiles)
                }
            }
            return variations.map { Ch13Pattern(it) }
        }
    }

    private companion object {
        const val ASH = '.'
        const val ROCKS = '#'
    }
}
