package challenges.challenge12

data class Ch12Data(private val springRows: List<Pair<String, List<Int>>>) {
    private val expandedRows: List<Pair<String, List<Int>>> = springRows.map { (springRow, validGroups) ->
        val expRow: String = springRow + UNKNOWN + springRow + UNKNOWN + springRow + UNKNOWN + springRow + UNKNOWN + springRow
        val expGroups: List<Int> = validGroups + validGroups + validGroups + validGroups + validGroups
        expRow to expGroups
    }

    fun countArrangements(expanded: Boolean = false): Long =
        (if (expanded.not()) springRows else expandedRows).sumOf { (springRow, validGroups) ->
            countArrangements(springRow, validGroups)
        }

    private val cache: MutableMap<Pair<String, List<Int>>, Long> = mutableMapOf()
    private fun countArrangements(remainingSpringRow: String, remainingGroups: List<Int>): Long =
        // gotta love these caches!!
        cache.getOrPut(remainingSpringRow to remainingGroups) {
            computeCountArrangements(remainingSpringRow, remainingGroups)
        }

    private fun computeCountArrangements(remainingSpringRow: String, remainingGroups: List<Int>): Long {
        // if all groups have been processed...
        if (remainingGroups.isEmpty()) {
            return if (remainingSpringRow.contains(DAMAGED)) {
                // but there are still damaged springs to be assigned, then fail
                0
            } else {
                // and all damaged springs have been assigned, then succeed
                1
            }
        } else if (remainingSpringRow.isEmpty()) {
            // if there are groups but no more springRow, then fail
            return 0
        }

        // process next spring character
        return when (remainingSpringRow[0]) {
            OPERATIONAL -> onOperational(remainingSpringRow, remainingGroups)
            DAMAGED -> onDamaged(remainingSpringRow, remainingGroups)
            UNKNOWN -> onOperational(remainingSpringRow, remainingGroups) + onDamaged(remainingSpringRow, remainingGroups)
            else -> throw IllegalStateException()
        }
    }

    private fun onOperational(candidate: String, groups: List<Int>): Long {
        // if we don't have enough room to fit what's pending, then fail
        // (room = sum + size - 1:  #.##.# -> 4 + 3 - 1 = 6 tiles needed)
        if (candidate.drop(1).length < groups.sum() + groups.size - 1) {
            return 0
        }

        // else, proceed with the analysis...
        return countArrangements(candidate.drop(1), groups)
    }

    private fun onDamaged(candidate: String, groups: List<Int>): Long {
        // if the first n tiles can't support a damaged spring, then fail
        if (candidate.take(groups[0]).replace(UNKNOWN, DAMAGED).count { it == DAMAGED } != groups[0]) {
            return 0
        }

        // the next tile after the damaged row is not an operative (or unknown), then fail
        if (candidate.length > groups[0] && listOf(UNKNOWN, OPERATIONAL).contains(candidate[groups[0]]).not()) {
            return 0
        }

        // else, proceed with the analysis...
        return countArrangements(candidate.drop(groups[0] + 1), groups.drop(1))
    }

    private companion object {
        const val OPERATIONAL = '.'
        const val DAMAGED = '#'
        const val UNKNOWN = '?'
    }
}
