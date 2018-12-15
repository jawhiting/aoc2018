import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BattleTest {
    @Test
    fun part1() {
        val tests = mapOf(
            """
                #######
                #.G...#
                #...EG#
                #.#.#G#
                #..G#E#
                #.....#
                #######
            """.trimIndent() to 27730,
            """
                #######
                #G..#E#
                #E#E.E#
                #G.##.#
                #...#E#
                #...E.#
                #######
            """.trimIndent() to 36334
        )

        tests.entries.forEach {
            val b = Battle.parse(it.key)
            assertEquals(it.value, b.run(log = false), "")
        }
    }
}