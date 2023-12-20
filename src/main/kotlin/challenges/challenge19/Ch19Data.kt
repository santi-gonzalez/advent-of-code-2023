package challenges.challenge19

import challenges.challenge19.Ch19Data.Ch19Workflow.*
import java.math.BigInteger

data class Ch19Data(
    val parts: List<Ch19Part>,
    val workflows: List<Ch19Workflow>
) {
    private val startingWorkflow: Ch19Workflow =
        workflows.find { it.name == STARTING_WORKFLOW_NAME } ?: throw IllegalStateException()
    private val bResults: MutableList<Ch19Result> = mutableListOf<Ch19Result>()

    fun getAllAcceptedPartsSum(): Int = parts
        .sumOf { part -> process(part, startingWorkflow) }

    fun getCompatiblePartsCount(): BigInteger {
        probe(
            workflow = startingWorkflow,
            xRange = 1..4000,
            mRange = 1..4000,
            aRange = 1..4000,
            sRange = 1..4000
        )
        var sum: BigInteger = BigInteger.ZERO
        for (result: Ch19Result in bResults) {
            sum += result.totalCombinations()
        }
        return sum
    }

    private fun process(part: Ch19Part, workflow: Ch19Workflow): Int {
        workflow.instructions.forEach { instruction ->
            val componentValue: Int = part.getComponentValue(instruction.param)
            val compareValue: Int = instruction.compareValue
            var output: String? = null
            if (instruction.componentIsGreaterThan) {
                if (componentValue > compareValue) {
                    output = instruction.output
                }
            } else {
                if (componentValue < compareValue) {
                    output = instruction.output
                }
            }
            if (output != null) {
                return when (output) {
                    OUTPUT_ACCEPT -> part.sumOfComponents()
                    OUTPUT_REJECT -> 0
                    else -> process(part, workflows.find { it.name == output } ?: throw IllegalStateException())
                }
            }
        }
        return when {
            workflow.isAcceptOutput -> part.sumOfComponents()
            workflow.isRejectOutput -> 0
            else -> process(part, workflows.find { it.name == workflow.output } ?: throw IllegalStateException())
        }
    }

    private fun probe(
        workflow: Ch19Workflow,
        xRange: IntRange,
        mRange: IntRange,
        aRange: IntRange,
        sRange: IntRange
    ) {
        var missXRange: IntRange = xRange
        var missMRange: IntRange = mRange
        var missARange: IntRange = aRange
        var missSRange: IntRange = sRange
        workflow.instructions.forEach { instruction: Ch19Instruction ->
            var hitXRange: IntRange = missXRange
            var hitMRange: IntRange = missMRange
            var hitARange: IntRange = missARange
            var hitSRange: IntRange = missSRange
            val relevantRange: IntRange = when (instruction.param) {
                'x' -> hitXRange
                'm' -> hitMRange
                'a' -> hitARange
                's' -> hitSRange
                else -> throw IllegalArgumentException()
            }
            val (first: IntRange, second: IntRange) = splitRange(
                relevantRange,
                instruction.compareValue,
                instruction.componentIsGreaterThan
            )
            val (hitRange: IntRange, missRange: IntRange) = if (instruction.componentIsGreaterThan) {
                second to first
            } else {
                first to second
            }
            when (instruction.param) {
                'x' -> missXRange = missRange
                'm' -> missMRange = missRange
                'a' -> missARange = missRange
                's' -> missSRange = missRange
                else -> throw IllegalArgumentException()
            }
            if (hitRange.isEmpty().not()) {
                when (instruction.param) {
                    'x' -> hitXRange = hitRange
                    'm' -> hitMRange = hitRange
                    'a' -> hitARange = hitRange
                    's' -> hitSRange = hitRange
                    else -> throw IllegalArgumentException()
                }
                when {
                    instruction.isAcceptOutput -> Ch19Result(
                        hitXRange,
                        hitMRange,
                        hitARange,
                        hitSRange
                    ).also { println(it); bResults.add(it) }

                    instruction.isRejectOutput -> Unit

                    else -> probe(
                        getWorkflow(instruction.output),
                        hitXRange,
                        hitMRange,
                        hitARange,
                        hitSRange
                    )
                }
            }
        }
        if (missXRange.isEmpty() || missMRange.isEmpty() || missARange.isEmpty() || missSRange.isEmpty()) {
            return
        }
        when {
            workflow.isAcceptOutput -> Ch19Result(
                missXRange,
                missMRange,
                missARange,
                missSRange
            ).also { println(it); bResults.add(it) }

            workflow.isRejectOutput -> Unit

            else -> probe(
                getWorkflow(workflow.output),
                missXRange,
                missMRange,
                missARange,
                missSRange
            )
        }
    }

    private fun splitRange(range: IntRange, at: Int, atOnFirstResult: Boolean = true): Pair<IntRange, IntRange> =
        with(if (atOnFirstResult) at else at - 1) {
            val first: IntRange = subtractRange(range, this + 1..range.last)
            val second: IntRange = subtractRange(range, range.first..this)
            first to second
        }

    private fun subtractRange(baseRange: IntRange, subtractRange: IntRange): IntRange {
        return when {
            subtractRange.first > baseRange.last || subtractRange.last < baseRange.first -> baseRange
            subtractRange.first <= baseRange.first && subtractRange.last >= baseRange.last -> 0..0
            subtractRange.first <= baseRange.first -> (subtractRange.last + 1)..baseRange.last
            subtractRange.last >= baseRange.last -> baseRange.first until subtractRange.first
            else -> throw IllegalArgumentException("can't subtract $subtractRange from $baseRange (disjoint ranges)")
        }
    }

    private fun getWorkflow(name: String): Ch19Workflow =
        workflows.find { it.name == name } ?: throw IllegalStateException()

    @Suppress("unused")
    fun print() {
        println(parts)
        println(workflows)
    }

    data class Ch19Part(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun getComponentValue(param: Char) = when (param) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw IllegalArgumentException()
        }

        fun sumOfComponents() = x + m + a + s
    }

    data class Ch19Workflow(
        val name: String,
        val instructions: List<Ch19Instruction>,
        val output: String
    ) {
        val isAcceptOutput = output == OUTPUT_ACCEPT
        val isRejectOutput = output == OUTPUT_REJECT

        data class Ch19Instruction(
            val param: Char,
            val componentIsGreaterThan: Boolean,
            val compareValue: Int,
            val output: String
        ) {
            val isAcceptOutput = output == OUTPUT_ACCEPT
            val isRejectOutput = output == OUTPUT_REJECT
        }
    }

    data class Ch19Result(
        val x: IntRange,
        val m: IntRange,
        val a: IntRange,
        val s: IntRange,
    ) {
        fun totalCombinations(): BigInteger =
            countRange(x) * countRange(m) * countRange(a) * countRange(s)

        private fun countRange(range: IntRange): BigInteger =
            BigInteger.valueOf(range.count().toLong())
    }

    private companion object {
        const val STARTING_WORKFLOW_NAME = "in"
        const val OUTPUT_ACCEPT = "A"
        const val OUTPUT_REJECT = "R"
    }
}
