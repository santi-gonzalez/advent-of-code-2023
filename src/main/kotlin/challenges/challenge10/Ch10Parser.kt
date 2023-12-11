package challenges.challenge10

object Ch10Parser {
    private lateinit var tileMap: Array<Array<Ch10Data.Ch10Tile>>
    private lateinit var expandedMap: Array<Array<Int>>
    private lateinit var start: Pair<Int, Int>

    fun parse(source: String): Ch10Data {
        val lines: List<String> = source.lines().filter { it.isNotEmpty() }
        val size: Int = lines[0].length
        tileMap = Array(size) { Array(size) { Ch10Data.Ch10Tile.Ground } }
        expandedMap = Array(size * 3 + 2) { Array(size * 3 + 2) { 0 } }
        lines.forEachIndexed { rowIndex, line ->
            line.forEachIndexed { columnIndex, tile ->
                setTile(rowIndex, columnIndex, Ch10Data.Ch10Tile.from(tile))
            }
        }
        return Ch10Data(size, tileMap, expandedMap, start)
    }

    private fun setTile(rowIndex: Int, columnIndex: Int, tile: Ch10Data.Ch10Tile) {
        tileMap[rowIndex][columnIndex] = tile
        setExpandedTile(rowIndex, columnIndex, tile)
        if (tile == Ch10Data.Ch10Tile.Start) {
            start = Pair(rowIndex, columnIndex)
        }
    }

    private fun setExpandedTile(rowIndex: Int, columnIndex: Int, tile: Ch10Data.Ch10Tile) {
        val expansionList: List<Int> = tile.expand()
        expandedMap[1 + (rowIndex * 3) + 0][1 + (columnIndex * 3) + 0] = expansionList[0]
        expandedMap[1 + (rowIndex * 3) + 1][1 + (columnIndex * 3) + 0] = expansionList[1]
        expandedMap[1 + (rowIndex * 3) + 2][1 + (columnIndex * 3) + 0] = expansionList[2]
        expandedMap[1 + (rowIndex * 3) + 0][1 + (columnIndex * 3) + 1] = expansionList[3]
        expandedMap[1 + (rowIndex * 3) + 1][1 + (columnIndex * 3) + 1] = expansionList[4]
        expandedMap[1 + (rowIndex * 3) + 2][1 + (columnIndex * 3) + 1] = expansionList[5]
        expandedMap[1 + (rowIndex * 3) + 0][1 + (columnIndex * 3) + 2] = expansionList[6]
        expandedMap[1 + (rowIndex * 3) + 1][1 + (columnIndex * 3) + 2] = expansionList[7]
        expandedMap[1 + (rowIndex * 3) + 2][1 + (columnIndex * 3) + 2] = expansionList[8]
    }
}
