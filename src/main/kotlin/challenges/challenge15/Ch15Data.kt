package challenges.challenge15

import java.util.*

data class Ch15Data(
    val sequence: List<String> = emptyList(),
    val boxes: Array<Stack<Pair<String, Int>>> = Array(256) { Stack() },
) {
    private val hashCache: MutableMap<String, Int> = mutableMapOf()

    fun hashSum(): Int = sequence.sumOf { segment -> hash(segment) }

    private fun hash(segment: String): Int = hashCache.getOrPut(segment) {
        computeHash(segment)
    }

    private fun computeHash(segment: String): Int {
        var currentValue = 0
        for (char in segment) {
            val asciiCode = char.code
            currentValue = ((currentValue + asciiCode) * 17) % 256
        }
        return currentValue
    }

    fun getTotalFocusingPower(): Int {
        clearBoxes()
        applyHashMap()
        return getTotalFocusingPowerInternal()
    }

    private fun clearBoxes() {
        boxes.forEach { box -> box.clear() }
    }

    private fun applyHashMap() {
        sequence.forEach { segment -> applyHashMap(segment) }
    }

    private fun applyHashMap(segment: String) {
        when {
            segment.endsWith("-") -> doRemove(segment.split("-")[0])
            else -> {
                val (label, value) = segment.split("=")
                doInsert(label, value.toInt())
            }
        }
    }

    private fun doRemove(label: String) {
        boxes[hash(label)].removeIf { (l, _) -> l == label }
    }

    private fun doInsert(label: String, value: Int) {
        val index = boxes[hash(label)].indexOfFirst { (l, _) -> l == label }
        if (index >= 0) {
            boxes[hash(label)][index] = label to value
        } else {
            boxes[hash(label)].push(label to value)
        }
    }

    private fun getTotalFocusingPowerInternal(): Int {
        var totalFocusingPower = 0
        boxes.forEachIndexed { boxIndex, box ->
            print("Box $boxIndex: ")
            totalFocusingPower += getBoxFocusingPower(boxIndex, box)
            println()
        }
        println()
        return totalFocusingPower
    }

    private fun getBoxFocusingPower(boxIndex: Int, box: Stack<Pair<String, Int>>): Int {
        var boxFocusingPower = 0
        box.forEachIndexed { lensIndex: Int, (lensLabel, lensValue) ->
            print("[$lensLabel $lensValue] ")
            boxFocusingPower += getLensFocusingPower(boxIndex, lensIndex, lensValue)
        }
        return boxFocusingPower
    }

    private fun getLensFocusingPower(boxIndex: Int, lensIndex: Int, lensValue: Int): Int =
        (boxIndex + 1) * (lensIndex + 1) * lensValue

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ch15Data

        if (sequence != other.sequence) return false
        if (!boxes.contentEquals(other.boxes)) return false
        if (hashCache != other.hashCache) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sequence.hashCode()
        result = 31 * result + boxes.contentHashCode()
        result = 31 * result + hashCache.hashCode()
        return result
    }
}
