package challenges.challenge17

import challenges.challenge17.Ch17Data.Direction.*
import kotlin.math.min

data class Ch17Data(val rows: List<String>) {
    private val cache: MutableMap<Triple<Pair<Int, Int>, Direction, Int>, Int> = mutableMapOf()

    fun findLeastHeatLoss(): Int = findLeastHeatLossInternal()

    fun findLeastHeatLossUltra(): Int = findLeastHeatLossInternal(ultraCrucible = true)

    private fun findLeastHeatLossInternal(ultraCrucible: Boolean = false): Int {
        val maxSteps = if (ultraCrucible) MAX_STEPS_ULTRA else MAX_STEPS
        val stepsToTurn = if (ultraCrucible) STEPS_TO_TURN_ULTRA else STEPS_TO_TURN

        var leastHeatLoss = Int.MAX_VALUE
        var states: List<Ch17State> = listOf(
            Ch17State(row = 0, col = 0, dir = DOWN, stepsLeft = maxSteps, accum = 0),
            Ch17State(row = 0, col = 0, dir = RIGHT, stepsLeft = maxSteps, accum = 0)
        )


        while (states.isNotEmpty()) {
            println("> pending states: ${states.size}")

            val newStates: MutableList<Ch17State> = mutableListOf()
            states.forEach { state ->

                // check if we already found a better way
                if (leastHeatLoss < state.accum)
                    return@forEach

                // check if we already visited this square
                if (cache.contains(state.key) && cache[state.key]!! <= state.accum)
                    return@forEach
                else
                    cache[state.key] = state.accum

                // check if this is the end tile
                if (isEndTile(state)) {
                    // check if we are able to stop
                    if (state.stepsLeft <= maxSteps - stepsToTurn) {
                        leastHeatLoss = min(leastHeatLoss, state.accum)
                    }
                    return@forEach
                }

                // check if we can still move forward
                if (state.stepsLeft > 0)
                    moveForward(state)?.let { newStates.add(it) }

                // check if we are able to turn
                if (state.stepsLeft <= maxSteps - stepsToTurn) {
                    turnRight(state, maxSteps)?.let { newStates.add(it) }
                    turnLeft(state, maxSteps)?.let { newStates.add(it) }
                }
            }
            states = newStates
        }
        return leastHeatLoss
    }

    private fun isEndTile(state: Ch17State): Boolean =
        state.row == rows.size - 1 && state.col == rows[0].length - 1

    private fun moveForward(state: Ch17State): Ch17State? =
        getNextTile(state.row, state.col, state.dir)?.let { (newRow, newCol) ->
            Ch17State(
                row = newRow,
                col = newCol,
                dir = state.dir,
                stepsLeft = state.stepsLeft - 1,
                accum = state.accum + rows[newRow][newCol].toString().toInt()
            )
        }

    private fun turnLeft(state: Ch17State, maxSteps: Int): Ch17State? =
        getNextTile(state.row, state.col, state.dir.turnLeft())?.let { (newRow, newCol) ->
            Ch17State(
                row = newRow,
                col = newCol,
                dir = state.dir.turnLeft(),
                stepsLeft = maxSteps - 1,
                accum = state.accum + rows[newRow][newCol].toString().toInt()
            )
        }

    private fun turnRight(state: Ch17State, maxSteps: Int): Ch17State? =
        getNextTile(state.row, state.col, state.dir.turnRight())?.let { (newRow, newCol) ->
            Ch17State(
                row = newRow,
                col = newCol,
                dir = state.dir.turnRight(),
                stepsLeft = maxSteps - 1,
                accum = state.accum + rows[newRow][newCol].toString().toInt()
            )
        }

    private fun getNextTile(row: Int, col: Int, dir: Direction): Pair<Int, Int>? {
        var newRow = row
        var newCol = col
        when (dir) {
            DOWN -> newRow++
            RIGHT -> newCol++
            UP -> newRow--
            LEFT -> newCol--
        }
        return if (newRow < 0 || newRow >= rows.size || newCol < 0 || newCol >= rows[0].length) {
            null
        } else {
            newRow to newCol
        }
    }

    @Suppress("unused")
    private fun print(): Ch17Data {
        rows.forEach { it.forEach { char: Char -> print("$char  ") }.also { println() } }
        println()
        return this
    }

    enum class Direction {
        DOWN, RIGHT, UP, LEFT;

        fun turnLeft(): Direction = when (this) {
            DOWN -> RIGHT
            RIGHT -> UP
            UP -> LEFT
            LEFT -> DOWN
        }

        fun turnRight(): Direction = when (this) {
            DOWN -> LEFT
            RIGHT -> DOWN
            UP -> RIGHT
            LEFT -> UP
        }
    }

    data class Ch17State(
        val row: Int,
        val col: Int,
        val dir: Direction,
        val stepsLeft: Int,
        val accum: Int
    ) {
        val key: Triple<Pair<Int, Int>, Direction, Int> =
            Triple(first = row to col, second = dir, third = stepsLeft)
    }

    private companion object {
        const val MAX_STEPS = 3
        const val MAX_STEPS_ULTRA = 10
        const val STEPS_TO_TURN = 1
        const val STEPS_TO_TURN_ULTRA = 4
    }
}
