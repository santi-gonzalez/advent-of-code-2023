package challenges.challenge14

data class Ch14Data(private var rows: List<String>) {
    private val spinsMap: MutableMap<List<String>, List<String>> = mutableMapOf()
    private val tiltStartMap: MutableMap<String, String> = mutableMapOf()
    private val tiltEndMap: MutableMap<String, String> = mutableMapOf()

    fun spinCycle(repetitions: Int = 1000000000): Ch14Data {
        var matchIndex = 0
        var iteration = 0
        for (repetition in 0 until repetitions) {
            if (spinsMap.containsKey(rows)) {
                matchIndex = spinsMap.keys.indexOf(rows)
                iteration = repetition
                break
            } else {
                rows = computeSpinCycle(rows)
            }
        }

        val loopLength: Int = iteration - matchIndex
        if (loopLength > 0) {
            val extraRepetitions: Int = (repetitions - iteration) % loopLength
            repeat(extraRepetitions) { rows = computeSpinCycle(rows) }
        }
        return this
    }

    private fun computeSpinCycle(rows: List<String>): List<String> = spinsMap.getOrPut(rows) {
        tilt(Direction.NORTH)
            .tilt(Direction.WEST)
            .tilt(Direction.SOUTH)
            .tilt(Direction.EAST)
            .rows.toList()
    }

    fun tilt(direction: Direction): Ch14Data = when (direction) {
        Direction.NORTH -> tiltNorthInternal()
        Direction.WEST -> tiltWestInternal()
        Direction.SOUTH -> tiltSouthInternal()
        Direction.EAST -> tiltEastInternal()
    }

    private fun tiltNorthInternal(): Ch14Data {
        val newColumns: MutableList<String> = mutableListOf()
        forAllColumns { column -> newColumns.add(tiltStart(column)) }
        rows = createNewRows(newColumns)
        return this
    }

    private fun tiltSouthInternal(): Ch14Data {
        val newColumns: MutableList<String> = mutableListOf()
        forAllColumns { column -> newColumns.add(tiltEnd(column)) }
        rows = createNewRows(newColumns)
        return this
    }

    private fun tiltWestInternal(): Ch14Data {
        val newRows: MutableList<String> = mutableListOf()
        forAllRows { row -> newRows.add(tiltStart(row)) }
        rows = newRows
        return this
    }

    private fun tiltEastInternal(): Ch14Data {
        val newRows: MutableList<String> = mutableListOf()
        forAllRows { row -> newRows.add(tiltEnd(row)) }
        rows = newRows
        return this
    }

    private fun forAllRows(block: (String) -> Unit) {
        for (rowIndex in rows.indices) {
            block(rows[rowIndex])
        }
    }

    private fun forAllColumns(block: (String) -> Unit) {
        for (columnIndex in rows[0].indices) {
            block(getColumn(columnIndex))
        }
    }

    private fun getColumn(columnIndex: Int): String {
        val result = StringBuilder()
        for (rowIndex in rows.indices) {
            result.append(rows[rowIndex][columnIndex])
        }
        return result.toString()
    }

    private fun createNewRows(newColumns: List<String>): List<String> {
        val newRows: MutableList<StringBuilder> = mutableListOf()
        for (rowIndex in rows.indices) {
            val newRow = StringBuilder()
            for (columnIndex in rows[0].indices) {
                newRow.append(newColumns[columnIndex][rowIndex])
            }
            newRows.add(newRow)
        }
        return newRows.map { it.toString() }
    }

    fun geNorthLoad(): Int {
        var result = 0
        for (columnIndex in rows[0].indices) {
            for (rowIndex in rows.indices) {
                result += if (rows[rowIndex][columnIndex] == BOULDER) rows.size - rowIndex else 0
            }
        }
        return result
    }

    // Pair<Int, Int> -> (boulders count in column, next rock row index in column)
    private fun tiltStart(line: String): String = tiltStartMap.getOrPut(line) { computeTiltStart(line) }
    private fun tiltEnd(line: String): String = tiltEndMap.getOrPut(line) { computeTiltEnd(line) }

    private fun computeTiltStart(line: String): String {
        val result = CharArray(line.length) { GROUND }
        var nextBoulderIndex = 0

        line.forEachIndexed { index, char ->
            when (char) {
                BOULDER -> {
                    result[nextBoulderIndex] = BOULDER
                    nextBoulderIndex++
                }

                ROCK -> {
                    result[index] = ROCK
                    nextBoulderIndex = index + 1
                }
            }
        }

        return String(result)
    }

    private fun computeTiltEnd(line: String): String {
        val result = CharArray(line.length) { GROUND }
        var nextBoulderIndex = line.length - 1

        for (index in line.indices.reversed()) {
            when (line[index]) {
                BOULDER -> {
                    result[nextBoulderIndex] = BOULDER
                    nextBoulderIndex--
                }

                ROCK -> {
                    result[index] = ROCK
                    nextBoulderIndex = index - 1
                }
            }
        }

        return String(result)
    }

    @Suppress("unused")
    private fun print(): Ch14Data {
        rows.forEach { line ->
            line.forEach { char ->
                print("$char ")
            }
            println()
        }
        println()
        return this
    }

    enum class Direction {
        NORTH, WEST, SOUTH, EAST
    }

    private companion object {
        const val BOULDER = 'O'
        const val ROCK = '#'
        const val GROUND = '.'
    }
}
