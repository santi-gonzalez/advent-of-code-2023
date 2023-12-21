package challenges.challenge21

class Ch21Data(rows: List<String>) {
    private val tilesMap = Array(rows.size) { Array(rows[0].length) { '.' } }
    private val maxRow: Int = tilesMap.size
    private val maxCol: Int = tilesMap[0].size
    private val startPosition: Pair<Int, Int>

    private var evenVisitedTiles: MutableSet<Pair<Int, Int>> = mutableSetOf()
    private var oddVisitedTiles: MutableSet<Pair<Int, Int>> = mutableSetOf()

    init {
        var startPosition = 0 to 0
        rows.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, char ->
                if (char == 'S') {
                    startPosition = rowIndex to colIndex
                    tilesMap[rowIndex][colIndex] = '.'
                } else {
                    tilesMap[rowIndex][colIndex] = char
                }
            }
        }
        this.startPosition = startPosition
    }

    fun countReachableTiles(): Long {
        move(steps = 64, wrapAroundEdges = false)
        return evenVisitedTiles.size.toLong()
    }

    fun countReachableTilesInfinite() {
        move(steps = 5000, wrapAroundEdges = true)
        // use some third party library to obtain the quadratic correlation for the values printed in the console. Then,
        // simply resolve the resulting quadratic formula by calculating f(26501365)
        // a = 0.8402191 b = 2.14556261 c = 13.61272653
    }

    private fun move(steps: Int, wrapAroundEdges: Boolean) {
        val pendingTiles: MutableSet<Pair<Int, Int>> = mutableSetOf(startPosition)
        var currentStep = 0
        evenVisitedTiles.clear()
        oddVisitedTiles.clear()
        while (currentStep <= steps) {
            val isEven: Boolean = currentStep % 2 == 0
            val nextOccupiedTiles: MutableSet<Pair<Int, Int>> = mutableSetOf()
            pendingTiles.forEach { currentTile ->
                updateReachableTiles(isEven, currentTile)
                getAdjacentTiles(currentTile)
                    .filter { isInBounds(it, wrapAroundEdges) }
                    .filter { isNotRock(it) }
                    .filter { isNotVisited(it, isEven) }.forEach { targetTile ->
                        nextOccupiedTiles.add(targetTile)
                    }
            }
            if (currentStep % 262 == 65) {
                println("f($currentStep) = ${oddVisitedTiles.size.toLong()}")
            }
            pendingTiles.clear()
            pendingTiles.addAll(nextOccupiedTiles)
            currentStep++
        }
    }

    private fun updateReachableTiles(isEven: Boolean, currentTile: Pair<Int, Int>) {
        if (isEven) {
            evenVisitedTiles.add(currentTile)
        } else {
            oddVisitedTiles.add(currentTile)
        }
    }

    private fun getAdjacentTiles(tile: Pair<Int, Int>): List<Pair<Int, Int>> = listOf(
        tile.first - 1 to tile.second, tile.first + 1 to tile.second, tile.first to tile.second - 1, tile.first to tile.second + 1
    )

    private fun isInBounds(tile: Pair<Int, Int>, wrapAroundEdges: Boolean): Boolean =
        wrapAroundEdges || (tile.first in tilesMap.indices && tile.second in tilesMap[0].indices)

    private fun isNotRock(tile: Pair<Int, Int>): Boolean {
        val wrappedTile: Pair<Int, Int> = applyWrapAround(tile)
        return tilesMap[wrappedTile.first][wrappedTile.second] != '#'
    }

    private fun isNotVisited(targetTile: Pair<Int, Int>, isEven: Boolean): Boolean =
        !isEven && !evenVisitedTiles.contains(targetTile) || isEven && !oddVisitedTiles.contains(targetTile)

    private fun applyWrapAround(tile: Pair<Int, Int>): Pair<Int, Int> {
        val (row, col) = tile
        val wrappedRow: Int = ((row % maxRow) + maxRow) % maxRow
        val wrappedCol: Int = ((col % maxCol) + maxCol) % maxCol
        return wrappedRow to wrappedCol
    }

    @Suppress("unused")
    private fun print(step: Int, occupiedTiles: Set<Pair<Int, Int>>) {
        println("STEP $step")
        tilesMap.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, char ->
                if (occupiedTiles.contains(rowIndex to colIndex)) {
                    print("O ")
                } else {
                    print("$char ")
                }
            }
            println()
        }
        println()
    }
}
