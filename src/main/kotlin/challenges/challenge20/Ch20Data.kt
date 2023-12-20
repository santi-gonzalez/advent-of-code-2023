package challenges.challenge20

import java.math.BigInteger

data class Ch20Data(val moduleDetailsMap: Map<C20ModuleName, C20Details>) {
    private val flipFlopsStateMap: MutableMap<C20ModuleName, Boolean> = mutableMapOf()
    private val conjunctionsListeningMap: MutableMap<C20ModuleName, MutableMap<C20ModuleName, C20Signal>> = mutableMapOf()
    private val conjunctionsStateMap: MutableMap<C20ModuleName, Boolean> = mutableMapOf()
    private var circuit1Value: Long? = null
    private var circuit2Value: Long? = null
    private var circuit3Value: Long? = null
    private var circuit4Value: Long? = null

    init {
        val moduleRelations: MutableMap<C20ModuleName, C20Destinies> = mutableMapOf()
        moduleDetailsMap.forEach { module: Map.Entry<C20ModuleName, C20Details> ->
            val moduleName: C20ModuleName = module.key
            val operation: C20Operation = module.value.operation()
            val destinies: C20Destinies = module.value.destinies()
            if (operation.isFlipFlop()) {
                flipFlopsStateMap[moduleName] = false
            } else if (operation.isConjunction()) {
                conjunctionsListeningMap[moduleName] = mutableMapOf()
                conjunctionsStateMap[moduleName] = false
            }
            destinies.forEach { destiny: C20ModuleName ->
                if (moduleRelations.containsKey(destiny).not()) moduleRelations[destiny] = mutableListOf()
                moduleRelations[destiny]!!.add(moduleName)
            }
        }
        conjunctionsListeningMap.keys.forEach { moduleName ->
            conjunctionsListeningMap[moduleName] =
                moduleRelations[moduleName]?.associateWith { SIGNAL_LOW }?.toMutableMap() ?: mutableMapOf()
        }
    }

    fun multiplyPulses(): Long {
        var totalLowPulseCount = 0L
        var totalHighPulseCount = 0L
        for (index in 1..1000) {
            val (lowPulseCount, highPulseCount) = pushButton()
            totalLowPulseCount += lowPulseCount
            totalHighPulseCount += highPulseCount
        }
        return totalLowPulseCount * totalHighPulseCount
    }

    fun countPushesUntilLowSignalOnRx(): BigInteger {
        // REQUIRES MANUAL ADJUSTMENT BEFORE LAUNCH! (see companion object)
        try {
            for (pushCount in 1 until LARGE_PUSH_COUNT) {
                pushButton(stopWhenLowSignalOnRx = true, pushCount)
            }
        } catch (ignored: Exception) {
        }
        return circuit1Value!!.toBigInteger() * circuit2Value!!.toBigInteger() * circuit3Value!!.toBigInteger() * circuit4Value!!.toBigInteger()
    }

    private fun pushButton(stopWhenLowSignalOnRx: Boolean = false, buttonPushes: Int = 0): Pair<Long, Long> {
        var lowPulseCount = 0L
        var highPulseCount = 0L
        val initialState: C20State = Triple(NAME_BUTTON, SIGNAL_LOW, mutableListOf(NAME_BROADCASTER))
        var pendingSignals: MutableList<C20State> = mutableListOf(initialState)
        while (pendingSignals.isNotEmpty()) {
            val newSignals: MutableList<C20State> = mutableListOf()
            pendingSignals.forEach { (fromSource: C20ModuleName, newSignal: C20Signal, destinies: C20Destinies) ->
                newSignals.addAll(destinies.mapNotNull { toDestiny: C20ModuleName ->
                    if (newSignal == SIGNAL_LOW) lowPulseCount++
                    if (newSignal == SIGNAL_HIGH) highPulseCount++
                    send(newSignal, toDestiny, fromSource, stopWhenLowSignalOnRx, buttonPushes)
                }.map { (sourceModule: C20ModuleName, newSignal: C20Signal, newDestinies: C20Destinies) ->
                    Triple(sourceModule, newSignal, newDestinies)
                })
            }
            pendingSignals = newSignals
        }
        return lowPulseCount to highPulseCount
    }

    private fun send(
        signal: C20Signal,
        toModule: C20ModuleName,
        fromModule: C20ModuleName,
        stopWhenLowSignalOnRx: Boolean,
        buttonPushes: Int,
    ): Triple<C20ModuleName, C20Signal, C20Destinies>? {
//        println("$fromModule -> [${if (signal == SIGNAL_LOW) "low" else "high"}] -> $toModule")
        when {
            stopWhenLowSignalOnRx && toModule == NAME_RELEVANT_1 && signal == SIGNAL_LOW -> circuit1Value = buttonPushes.toLong()
            stopWhenLowSignalOnRx && toModule == NAME_RELEVANT_2 && signal == SIGNAL_LOW -> circuit2Value = buttonPushes.toLong()
            stopWhenLowSignalOnRx && toModule == NAME_RELEVANT_3 && signal == SIGNAL_LOW -> circuit3Value = buttonPushes.toLong()
            stopWhenLowSignalOnRx && toModule == NAME_RELEVANT_4 && signal == SIGNAL_LOW -> circuit4Value = buttonPushes.toLong()
        }
        if (circuit1Value != null && circuit2Value != null && circuit3Value != null && circuit4Value != null) {
            throw Exception()
        }
        if (moduleDetailsMap.containsKey(toModule)) {
            val details: C20Details = moduleDetailsMap[toModule]!!
            val newSignal: C20Signal = when {
                details.operation().isFlipFlop() -> processFlipFlop(signal, toModule)
                details.operation().isConjunction() -> processConjunction(signal, toModule, fromModule)
                else -> signal
            }
            return when {
                newSignal != SIGNAL_NONE -> Triple(toModule, newSignal, details.destinies())
                else -> null
            }
        }
        return null
    }

    private fun processFlipFlop(signal: C20Signal, name: C20ModuleName): C20Signal {
        if (signal == SIGNAL_LOW) {
            flipFlopsStateMap[name] = !flipFlopsStateMap[name]!!
            return if (flipFlopsStateMap[name]!!) SIGNAL_HIGH else SIGNAL_LOW
        }
        return SIGNAL_NONE
    }

    private fun processConjunction(pulse: Int, moduleName: String, sourceModule: String): C20Signal {
        if (pulse != SIGNAL_NONE) {
            conjunctionsListeningMap[moduleName]!![sourceModule] = pulse
            conjunctionsStateMap[moduleName] = conjunctionsListeningMap[moduleName]!!.values.all { it == SIGNAL_HIGH }
            return if (conjunctionsStateMap[moduleName]!!) SIGNAL_LOW else SIGNAL_HIGH
        }
        return SIGNAL_NONE
    }

    @Suppress("unused")
    private fun print(modules: Boolean = false, flipFlops: Boolean = true, conjunctions: Boolean = true): Ch20Data {
        if (modules) {
            println("MODULES:")
            moduleDetailsMap.forEach { module -> println(module) }
            println()
        }
        if (flipFlops) {
            println("FLIP FLOPS:")
            flipFlopsStateMap.forEach { flipFlop -> println(flipFlop) }
            println()
        }
        if (conjunctions) {
            println("CONJUNCTIONS:")
            conjunctionsStateMap.forEach { conjunction -> println(conjunction) }
            println()
        }
        return this
    }

    private companion object {
        const val NAME_BUTTON = "button"
        const val NAME_BROADCASTER = "broadcaster"

        // Identify the 4 modules that reference into the proxy module connected to 'rx', and then put their names here
        const val NAME_RELEVANT_1 = "sj"
        const val NAME_RELEVANT_2 = "qq"
        const val NAME_RELEVANT_3 = "ls"
        const val NAME_RELEVANT_4 = "bg"

        const val SIGNAL_NONE = 0
        const val SIGNAL_LOW = 1
        const val SIGNAL_HIGH = 2
        const val LARGE_PUSH_COUNT = 5000
    }
}

typealias C20Signal = Int
typealias C20ModuleName = String
typealias C20Operation = Char
typealias C20Destinies = MutableList<C20ModuleName>
typealias C20Details = Pair<C20Operation, C20Destinies>
typealias C20State = Triple<C20ModuleName, C20Signal, C20Destinies>

fun C20Operation.isFlipFlop(): Boolean = this == '%'
fun C20Operation.isConjunction(): Boolean = this == '&'
fun C20Details.operation(): C20Operation = this.first
fun C20Details.destinies(): C20Destinies = this.second
