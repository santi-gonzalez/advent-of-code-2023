package challenges.challenge19

object Ch19Parser {
    fun parse(source: String): Ch19Data {
        val partsList = mutableListOf<Ch19Data.Ch19Part>()
        val workflowList = mutableListOf<Ch19Data.Ch19Workflow>()
        var workflows = true
        source.lines().forEach { line ->
            if (line.isEmpty()) workflows = !workflows
            else if (workflows) {
                val (name, instructionsLine) = line.removeSuffix("}").split("{")
                val instructionWords = instructionsLine.split(",")
                val instructions = instructionWords.mapNotNull {
                    if (it.contains(":").not()) null else {
                        val (comparator, output) = it.split(":")
                        Ch19Data.Ch19Workflow.Ch19Instruction(
                            param = comparator[0],
                            componentIsGreaterThan = comparator[1] == '>',
                            compareValue = comparator.drop(2).toInt(),
                            output = output
                        )
                    }
                }
                val defaultOutput = instructionWords.last()
                val workflow = Ch19Data.Ch19Workflow(
                    name = name,
                    instructions = instructions,
                    output = defaultOutput,
                )
                workflowList.add(workflow)
            } else {
                val components = line
                    .removePrefix("{")
                    .removeSuffix("}")
                    .split(",")
                val part = Ch19Data.Ch19Part(
                    x = components[0].drop(2).toInt(),
                    m = components[1].drop(2).toInt(),
                    a = components[2].drop(2).toInt(),
                    s = components[3].drop(2).toInt(),
                )
                partsList.add(part)
            }
        }
        return Ch19Data(partsList, workflowList)
    }
}
