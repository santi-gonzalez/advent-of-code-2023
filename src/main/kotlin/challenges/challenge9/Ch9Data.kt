package challenges.challenge9

data class Ch9Data(val rows: List<List<Long>>) {
    fun getExtrapolationSum(): Long = rows.sumOf { row ->
        createExtraRows(row).reversed().sumOf { extraRow -> extraRow.last() }
    }

    fun getPreviousExtrapolationSum(): Long = rows.sumOf { row ->
        val extraRows: List<List<Long>> = createExtraRows(row).reversed()
        var newValue = 0L
        for (rowIndex in 1 until extraRows.size) {
            newValue = extraRows[rowIndex][0] - newValue
        }
        newValue
    }

    private fun createExtraRows(row: List<Long>): List<List<Long>> = mutableListOf<List<Long>>().apply {
        var currentRow: List<Long> = row
        add(currentRow)
        do {
            val nextRow: MutableList<Long> = mutableListOf<Long>()
            for (index in 1 until currentRow.size) {
                nextRow.add(currentRow[index] - currentRow[index - 1])
            }
            currentRow = nextRow
            add(currentRow)
        } while (currentRow.all { it == 0L }.not())
    }
}
