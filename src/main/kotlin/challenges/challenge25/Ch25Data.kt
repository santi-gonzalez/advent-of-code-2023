package challenges.challenge25

class Ch25Data(private val baseSystem: Ch25System) {
    fun solveA(): Int {
        // First, uncomment and execute "runHelp()". That will find the most visited connections from
        // a random 40 pairs of points, sorting the most appearing nodes so that they can later be
        // tested.
        // After that, find the connections manually, add them in the "cutAndEvaluate" and swap the
        // comments. If everything went right, the result should be printed on the console.
//        return runHelp()
        return cutAndEvaluate("tzj" to "gtj", "jzv" to "qvq", "bbp" to "dvr")
    }

    private fun runHelp(repetitions: Int = 40): Int {
        val results = mutableListOf<List<String>>()
        println("Finding random paths...")
        repeat(repetitions) { count ->
            getRandomNodes(baseSystem).let {
                print(">> (${count + 1}/$repetitions) evaluating... $it")
                getShortPath(baseSystem, it.first, it.second).let {
                    println(": $it")
                    if (it.size > 2) {
                        results.add(it)
                    }
                }
            }
        }
        println("")
        println("Most occurrences: ${countOccurrences(results).take(6)}")
        throw Exception("Find the connections from these 6 elements, modify the program to make it cut by them, then run again.")
    }

    private fun getRandomNodes(system: Ch25System): Pair<String, String> {
        val key1: String = system.keys.random()
        var key2: String? = null
        while (key2 == null || key1 == key2) {
            key2 = system.keys.random()
        }
        return key1 to key2
    }

    private fun getShortPath(
        system: Ch25System,
        from: String,
        to: String,
        visited: MutableMap<String, Int> = mutableMapOf(),
        path: MutableList<String> = mutableListOf(),
    ): List<String> {
        val newPath = path.toMutableList().apply { add(from) }
        return if (from == to) {
            newPath
        } else if (visited.getOrElse(from) { Int.MAX_VALUE } >= path.size) {
            visited[from] = path.size
            val morePaths = system[from]!!.map { connection ->
                getShortPath(system, connection, to, visited, newPath)
            }.filter { it.isNotEmpty() }
            val shortPaths = morePaths.filter { it.size == morePaths.minOf { it.size } }
            if (shortPaths.isNotEmpty()) shortPaths[0] else emptyList()
        } else {
            emptyList()
        }
    }

    private fun countOccurrences(lists: List<List<String>>): List<Pair<String, Int>> {
        val counts = lists.flatten().groupingBy { it }.eachCount()
        return counts.toList().sortedWith(compareByDescending<Pair<String, Int>> { it.second }.thenBy { it.first })
    }

    private fun cutAndEvaluate(
        connection1: Pair<String, String>,
        connection2: Pair<String, String>,
        connection3: Pair<String, String>
    ): Int {
        println("Cutting from connections ($connection1, $connection2, $connection3)")
        val reducedSystem: MutableMap<String, MutableSet<String>> =
            removeConnection(
                removeConnection(
                    removeConnection(
                        baseSystem.mapValues { it.value.toMutableSet() }.toMutableMap(),
                        connection1.first,
                        connection1.second
                    ),
                    connection2.first,
                    connection2.second
                ),
                connection3.first,
                connection3.second
            )
        getSubgroups(reducedSystem).let { groups ->
            if (groups.size == 2) {
                return groups[0].size * groups[1].size
            }
        }
        throw Exception("Error: Not a valid setup! ($connection1, $connection2, $connection3)")
    }

    private fun removeConnection(
        system: MutableMap<String, MutableSet<String>>,
        elementA: String,
        elementB: String
    ): MutableMap<String, MutableSet<String>> {
        system[elementA]?.remove(elementB)
        system[elementB]?.remove(elementA)
        return system
    }

    private fun getSubgroups(system: Ch25System): List<MutableSet<String>> {
        val groups = mutableListOf<MutableSet<String>>()
        val resolved = mutableSetOf<String>()
        system.keys.forEach { key ->
            groups.add(mutableSetOf())
            addToGroup(system, groups.last(), key, resolved)
        }
        return groups.filter { it.isNotEmpty() }
    }

    private fun addToGroup(
        system: Ch25System,
        currentGroup: MutableSet<String>,
        element: String,
        resolved: MutableSet<String>
    ) {
        if (resolved.contains(element)) {
            return
        }
        resolved.add(element)
        currentGroup.add(element)
        system[element]!!.forEach { connection -> addToGroup(system, currentGroup, connection, resolved) }
    }

    fun print(system: Ch25System): Ch25Data {
        system.forEach { connection ->
            println(connection)
        }
        return this
    }
}

typealias Ch25System = Map<String, Set<String>>
