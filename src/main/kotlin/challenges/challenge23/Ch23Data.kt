package challenges.challenge23

import java.util.*

data class Ch23Data(private val tiles: Map<Ch23Position, Char>, private val size: Int) {
    private val startTile: Ch23Position = tiles.filter { it.key.row() == 0 && it.value == '.' }.keys.first()
    private val endTile: Ch23Position = tiles.filter { it.key.row() == size - 1 && it.value == '.' }.keys.first()

    init {
        println("start is at: $startTile")
        println("end is at: $endTile")
    }

    fun countLongestPathSteps(): Int =
        findLongestRoute(buildPaths(ignoreSlopes = false).also { println("walking, please wait...") }, mutableSetOf(startTile))

    fun countLongestPathStepsNoSlopes(): Int =
        findLongestRoute(buildPaths(ignoreSlopes = true).also { println("walking, please wait...") }, mutableSetOf(startTile))

    private fun buildPaths(ignoreSlopes: Boolean): Map<Ch23Position, Set<Ch23Path>> {
        val paths: MutableMap<Ch23Position, MutableSet<Ch23Path>> = mutableMapOf()
        val visitedNodes: MutableSet<Ch23Position> = mutableSetOf()
        val pendingNodes: Stack<Ch23Position> = Stack()
        pendingNodes.push(startTile)
        while (pendingNodes.isNotEmpty()) {
            val currentNode: Ch23Position = pendingNodes.pop()
            visitedNodes.add(currentNode)
            findNewPathsToExplore(currentNode, ignoreSlopes).forEach { firstStep: Ch23Position ->
                val exploration: Ch23Path = exploreNewPath(currentNode, firstStep, ignoreSlopes)
                paths.getOrPut(currentNode) { mutableSetOf() }.let { paths: MutableSet<Ch23Path> ->
                    paths.find { path: Ch23Path ->
                        path.position() == exploration.position()
                    }?.let { existing: Ch23Path ->
                        if (existing.steps() < exploration.steps()) {
                            paths.remove(existing)
                            paths.add(exploration)
                        }
                    } ?: run { paths.add(exploration) }
                }
                exploration.position().let { endNode: Pair<Int, Int> ->
                    if (!visitedNodes.contains(endNode)) {
                        pendingNodes.push(endNode)
                    }
                }
            }
        }
        println("paths: $paths")
        return paths
    }

    private fun findNewPathsToExplore(position: Ch23Position, ignoreSlopes: Boolean): MutableSet<Ch23Position> {
        val paths: MutableSet<Ch23Position> = mutableSetOf()
        position.up().let { up: Ch23Position -> if (isInBounds(up) && canGoUp(up, ignoreSlopes)) paths.add(up) }
        position.down().let { down: Ch23Position -> if (isInBounds(down) && canGoDown(down, ignoreSlopes)) paths.add(down) }
        position.left().let { left: Ch23Position -> if (isInBounds(left) && canGoLeft(left, ignoreSlopes)) paths.add(left) }
        position.right().let { right: Ch23Position -> if (isInBounds(right) && canGoRight(right, ignoreSlopes)) paths.add(right) }
        return paths
    }

    private fun isInBounds(position: Ch23Position): Boolean =
        position.first in 0 until size && position.second in 0 until size

    private fun exploreNewPath(
        previous: Ch23Position,
        current: Ch23Position,
        ignoreSlopes: Boolean,
        steps: Int = 1
    ): Ch23Path = if (current.row() == size - 1 || current.row() == 0) {
        current to steps
    } else {
        val newPaths: MutableSet<Pair<Int, Int>> = findNewPathsToExplore(current, ignoreSlopes)
        newPaths.remove(previous)
        if (newPaths.size == 1) {
            exploreNewPath(current, newPaths.toList().first(), ignoreSlopes, steps + 1)
        } else {
            current to steps
        }
    }

    private fun canGoUp(position: Ch23Position, ignoreSlopes: Boolean): Boolean =
        (ignoreSlopes && !isForest(position)) || isGround(position) || isUp(position)

    private fun canGoDown(position: Ch23Position, ignoreSlopes: Boolean): Boolean =
        (ignoreSlopes && !isForest(position)) || isGround(position) || isDown(position)

    private fun canGoLeft(position: Ch23Position, ignoreSlopes: Boolean): Boolean =
        (ignoreSlopes && !isForest(position)) || isGround(position) || isLeft(position)

    private fun canGoRight(position: Ch23Position, ignoreSlopes: Boolean): Boolean =
        (ignoreSlopes && !isForest(position)) || isGround(position) || isRight(position)

    private fun isForest(position: Ch23Position): Boolean = tiles[position] == FOREST

    private fun isGround(position: Ch23Position): Boolean = tiles[position] == GROUND

    private fun isUp(position: Ch23Position): Boolean = tiles[position] == UP

    private fun isDown(position: Ch23Position): Boolean = tiles[position] == DOWN

    private fun isLeft(position: Ch23Position): Boolean = tiles[position] == LEFT

    private fun isRight(position: Ch23Position): Boolean = tiles[position] == RIGHT

    private fun findLongestRoute(
        paths: Map<Ch23Position, Set<Ch23Path>>,
        visited: MutableSet<Ch23Position>,
        stepsSoFar: Int = 0
    ): Int {
        var result = 0
        val currentNode: Ch23Position = visited.last()
        if (currentNode == endTile) {
            result = stepsSoFar
        } else {
            paths[currentNode]!!.forEach { (nextNode: Ch23Position, nextSteps: Int) ->
                if (!visited.contains(nextNode)) {
                    result = maxOf(
                        result, findLongestRoute(
                            paths = paths,
                            visited = visited.toMutableSet().apply { add(nextNode) },
                            stepsSoFar = stepsSoFar + nextSteps
                        )
                    )
                }
            }
        }
        return result
    }

    private fun Ch23Position.row(): Int = first
    private fun Ch23Position.col(): Int = second
    private fun Ch23Position.down(): Ch23Position = row() + 1 to col()
    private fun Ch23Position.up(): Ch23Position = row() - 1 to col()
    private fun Ch23Position.right(): Ch23Position = row() to col() + 1
    private fun Ch23Position.left(): Ch23Position = row() to col() - 1
    private fun Ch23Path.position(): Ch23Position = first
    private fun Ch23Path.steps(): Int = second

    private companion object {
        const val FOREST = '#'
        const val GROUND = '.'
        const val UP = '^'
        const val DOWN = 'v'
        const val LEFT = '<'
        const val RIGHT = '>'
    }
}

typealias Ch23Position = Pair<Int, Int>
typealias Ch23Path = Pair<Ch23Position, Int>
