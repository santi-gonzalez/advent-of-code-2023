package challenges.challenge11

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Ch11Data(val source: String) {
    private val universe: MutableList<MutableList<Ch11Kind>> = Ch11Parser.parse(source)
    private val rowsRange: IntRange get() = universe.indices
    private val columnsRange: IntRange get() = universe[0].indices

    fun expandUniverse(): Ch11Data {
        // Find which rows and columns need to be marked as EXPANSIONS
        val (exRowIndexes: List<Int>, exColumnIndexes: List<Int>) = findExpansionIndexes()
        // Mark EXPANSION rows
        exRowIndexes.reversed().forEach { extraRowIndex ->
            val extraRow: MutableList<Ch11Kind> = mutableListOf()
            for (index in columnsRange) {
                extraRow.add(Ch11Kind.EXPANSION)
            }
            universe[extraRowIndex] = extraRow
        }
        // Mark EXPANSION columns
        exColumnIndexes.reversed().forEach { extraColumnIndex ->
            for (rowIndex in rowsRange) {
                universe[rowIndex][extraColumnIndex] = Ch11Kind.EXPANSION
            }
        }
        return this
    }

    private fun findExpansionIndexes(): Pair<List<Int>, List<Int>> {
        val exRows: MutableList<Int> = mutableListOf()
        // Find row indexes that have all values as VOID
        universe.forEachIndexed { rowIndex, rows ->
            if (rows.all { it == Ch11Kind.VOID }) {
                exRows.add(rowIndex)
            }
        }
        val exColumns: MutableList<Int> = mutableListOf()
        // Find column indexes that have all values as VOID
        for (columnIndex in columnsRange) {
            var columnIsEmpty = true
            for (rowIndex in rowsRange) {
                if (universe[rowIndex][columnIndex] != Ch11Kind.VOID) {
                    columnIsEmpty = false
                    break
                }
            }
            if (columnIsEmpty) {
                exColumns.add(columnIndex)
            }
        }
        return exRows to exColumns
    }

    fun countDistances(expansionFactor: Int = 2): Long {
        val galaxies: List<Pair<Int, Int>> = findGalaxies()
        val pending: MutableList<Pair<Int, Int>> = galaxies.toMutableList()
        var count = 0L
        galaxies.forEach { galaxy ->
            pending.remove(galaxy)
            pending.forEach { compare ->
                // Prepare the coordinates of the two relevant Galaxies
                val rowA = galaxy.first
                val rowB = compare.first
                val colA = galaxy.second
                val colB = compare.second
                // Obtain how many times the closest path traverse EXPANSION tiles
                val rowExpansions: Int = getRowExpansionsCount(rowA, rowB)
                val colExpansions: Int = getColumnExpansionsCount(colA, colB)
                // Accumulate the distance using this formula:
                // 1. Find the absolute distance
                // 2. Add the expansion factor for every row EXPANSION found
                // 3. Add the expansion factor for every column EXPANSION found
                // 4. Remove all row and column EXPANSION(s) previously counted individually
                count += abs(rowA - rowB) + abs(colA - colB) +
                        rowExpansions * expansionFactor +
                        colExpansions * expansionFactor -
                        rowExpansions - colExpansions
            }
        }
        return count
    }

    private fun findGalaxies(): List<Pair<Int, Int>> {
        val result: MutableList<Pair<Int, Int>> = mutableListOf()
        universe.forEachIndexed { rowIndex, rows ->
            rows.forEachIndexed { columnIndex, tile ->
                if (tile == Ch11Kind.GALAXY) result.add(rowIndex to columnIndex)
            }
        }
        return result
    }

    private fun getRowExpansionsCount(rowA: Int, rowB: Int): Int {
        val fromRow = min(rowA, rowB)
        val toRow = max(rowA, rowB)
        var count = 0
        for (rowIndex in fromRow until toRow) {
            if (universe[rowIndex][0] == Ch11Kind.EXPANSION) {
                count++
            }
        }
        return count
    }

    private fun getColumnExpansionsCount(colA: Int, colB: Int): Int {
        val fromColumn = min(colA, colB)
        val toColumn = max(colA, colB)
        var count = 0
        for (columnIndex in fromColumn until toColumn) {
            if (universe[0][columnIndex] == Ch11Kind.EXPANSION) {
                count++
            }
        }
        return count
    }

    fun printUniverse() {
        universe.forEach { rows ->
            rows.forEach { kind ->
                print(
                    when (kind) {
                        Ch11Kind.GALAXY -> "# "
                        Ch11Kind.EXPANSION -> "E "
                        else -> ". "
                    }
                )
            }
            println()
        }
        println()
    }

    enum class Ch11Kind {
        VOID, GALAXY, EXPANSION
    }
}
