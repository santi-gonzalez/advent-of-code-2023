package instrumentation

import java.lang.reflect.Constructor
import java.util.*

object ChallengeFactory {
    fun getChallenge(day: Int, letter: Char, arg: String): Challenge {
        val className = "challenges.Challenge${day}${letter.uppercase(Locale.getDefault())}"
        val clazz: Class<*> = Class.forName(className)
        val constructor: Constructor<out Any> = clazz.getConstructor(String::class.java)
        return constructor.newInstance(arg) as Challenge
    }
}
