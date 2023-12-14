package challenges.challenge12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Ch12DataTest {
    @Test
    fun test() {
        assertEquals(1, Ch12Data(listOf("???.###" to listOf(1, 1, 3))).countArrangements())
        assertEquals(4, Ch12Data(listOf(".??..??...?##." to listOf(1, 1, 3))).countArrangements())
        assertEquals(1, Ch12Data(listOf("?#?#?#?#?#?#?#?" to listOf(1, 3, 1, 6))).countArrangements())
        assertEquals(1, Ch12Data(listOf("????.#...#..." to listOf(4, 1, 1))).countArrangements())
        assertEquals(4, Ch12Data(listOf("????.######..#####." to listOf(1, 6, 5))).countArrangements())
        assertEquals(10, Ch12Data(listOf("?###????????" to listOf(3, 2, 1))).countArrangements())
    }
}
