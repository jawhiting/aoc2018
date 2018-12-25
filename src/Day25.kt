import Coord4.Companion.parse
import kotlin.math.abs

private fun extractInts(s: String) : IntArray {
    return "(-?\\d+)".toRegex().findAll(s).asIterable().map { it.value.toInt() }.toIntArray()
}

private data class Coord4(val x: Int, val y: Int, val z: Int, val t: Int) {


    fun distance(c: Coord4): Int {
        return abs(x-c.x) + abs(y-c.y) + abs(z-c.z) + abs( t-c.t)
    }

    fun inConstellation(c: Coord4): Boolean {
        return distance(c) <= 3
    }

    companion object {
        fun parse(s: String): Coord4 {
            val i = extractInts(s)
            return Coord4(i[0], i[1], i[2], i[3])
        }
    }
}

fun main() {
    val points = input1.lines().map { Coord4.parse(it) }

    val constellations = mutableListOf<Set<Coord4>>()

    val processed = mutableSetOf<Coord4>()
    val remaining = points.toMutableSet()
    for (point in points) {
        println("Processing point $point")
        if(processed.contains(point)) continue
        remaining.remove(point)
        val con = extractConstellation(point, remaining)
        processed.addAll(con)
        remaining.removeAll(con)
        constellations.add(con)
    }

    println(constellations)
    for (constellation in constellations) {
        println("Constellation: ${constellation.size}: $constellation")
    }
    println("${constellations.size}")

}

private fun extractConstellation(start: Coord4, points: Set<Coord4>): Set<Coord4> {

    // find all near start
    val inConstellation = points.filter { start.inConstellation(it) }
    println("In: ${inConstellation.size} Remaining: ${points.size}")
    val result = mutableSetOf<Coord4>()
    result.add(start)

    val remainingPoints = points.minus(inConstellation).toMutableSet()
    for (coord4 in inConstellation) {
        val elements = extractConstellation(coord4, remainingPoints)
        remainingPoints.removeAll(elements)
        result.addAll(elements)
    }

    return result
}

private val test1 = """
0,0,0,0
3,0,0,0
0,3,0,0
0,0,3,0
0,0,0,3
0,0,0,6
9,0,0,0
12,0,0,0
""".trimIndent()

private val test2 = """
-1,2,2,0
0,0,2,-2
0,0,0,-2
-1,2,0,0
-2,-2,-2,2
3,0,2,-1
-1,3,2,2
-1,0,-1,0
0,2,1,-2
3,0,0,0
""".trimIndent()

private val test4 = """
1,-1,-1,-2
-2,-2,0,1
0,2,1,3
-2,3,-2,1
0,2,3,-2
-1,-1,1,-2
0,-2,-1,0
-2,2,3,-1
1,2,2,0
-1,-2,0,-2
""".trimIndent()

private val input1 = """
1,-7,1,1
-3,4,3,4
-6,2,4,8
7,-1,-5,5
-8,-7,-6,5
2,0,-3,3
0,-3,0,-5
0,-8,-2,1
4,3,6,8
-5,8,-1,6
-5,8,3,5
5,-7,-5,1
-3,1,-1,-3
-6,-2,2,0
7,4,-7,-1
2,-6,3,-7
0,4,0,6
-1,2,-7,-4
-6,-7,-4,-5
2,0,-2,5
-6,1,-8,6
-8,-5,-7,-6
3,3,-2,0
5,-1,-6,-2
2,1,-7,7
0,0,-8,-2
1,-1,6,8
7,-7,-2,2
-2,3,-1,0
6,8,0,-8
2,2,7,-5
-8,5,5,-8
3,0,8,2
3,1,-6,-4
-4,-7,5,0
-2,7,5,5
-6,0,0,1
0,-6,2,4
-1,-5,8,0
-2,0,0,-1
1,-1,-1,-7
-3,-4,6,-1
-1,8,3,7
2,3,-4,0
0,8,4,-6
-6,6,0,8
0,5,-2,-8
-1,3,-3,-3
6,0,6,-1
-5,4,3,0
6,2,3,-5
-3,-7,-5,4
3,2,6,0
1,4,-4,-8
2,-3,0,-2
4,-2,-1,4
-7,-3,6,0
-3,7,-5,-5
2,5,-5,7
0,-6,0,-5
-8,0,-2,-8
5,-5,1,-1
6,-5,-1,0
0,5,-8,5
-7,7,1,1
-3,6,6,-1
5,-1,8,7
-6,-4,1,1
8,0,6,0
6,-2,-7,-3
7,-7,-5,-5
-2,6,-7,6
5,-5,-6,-7
4,7,-5,-1
2,7,2,-1
2,-3,-1,-6
8,-5,-7,-6
-8,-7,-6,-7
3,-8,-6,-7
5,-2,1,-8
0,2,1,8
-7,0,4,-4
7,-4,-7,7
2,0,-7,-6
-6,8,-7,4
-6,1,-3,-5
-6,-1,6,3
-1,1,7,-1
-8,-5,1,5
-7,0,0,-7
0,1,6,-6
-8,-1,0,8
2,-1,1,7
-6,6,3,3
5,1,-1,0
-1,-6,-5,4
-4,5,3,6
-3,6,-8,-7
-6,7,-2,1
4,-3,-5,1
3,2,3,8
-1,0,-1,8
-4,0,-7,3
7,-8,0,5
3,4,4,-7
-8,7,-5,-6
3,2,0,7
-4,3,-3,4
-3,0,6,-5
0,2,-8,6
5,4,-6,7
2,8,-4,6
-5,-8,-4,7
6,0,-3,3
-4,-8,6,-8
6,0,-6,-8
5,-8,-3,6
-6,1,-7,0
1,1,-7,0
-6,3,-2,-5
0,4,-6,0
-7,-7,-7,8
-5,7,-8,-2
-8,-7,0,7
-6,-2,0,6
0,-7,-1,5
-6,6,0,-4
7,-4,1,1
0,5,8,-1
5,2,-4,-7
0,-2,-8,-6
-4,-6,0,7
-5,-3,0,3
4,-3,-1,6
-3,-6,4,5
4,-2,-3,0
-3,-6,4,-3
5,4,3,-1
7,3,1,2
-4,3,-6,6
5,6,0,8
5,-5,3,3
-5,4,-1,-4
0,-1,0,1
8,-3,6,5
2,7,-6,-1
-3,6,8,0
0,-4,-8,-3
1,2,8,-3
-7,2,-4,1
-8,-6,-2,8
3,-1,7,-8
-8,-5,3,2
0,-5,5,6
0,1,8,-7
0,7,0,1
-7,7,2,2
4,-7,-1,-4
6,8,0,-5
-7,2,-3,-7
1,0,-3,0
0,4,-3,4
1,-5,-8,7
6,-8,0,0
-3,-1,6,-4
5,-5,5,0
-2,6,-2,7
2,1,5,-7
-7,-4,2,-1
7,-7,5,-5
-4,0,-6,5
7,5,0,8
2,4,-1,8
-2,0,2,0
1,0,-1,-3
-6,3,2,4
-6,4,7,7
-6,-3,8,-8
7,2,2,5
-6,0,-2,5
1,0,5,-5
0,-2,-1,7
6,6,0,5
4,0,-7,0
-2,1,-2,-1
0,-3,1,-1
3,4,8,8
-7,6,2,4
3,7,-8,2
2,-5,-2,7
3,-8,5,-5
5,0,2,2
0,2,3,4
6,0,3,8
8,6,5,2
-3,6,-3,-2
-4,0,5,-1
6,6,8,4
5,-5,-1,0
-7,1,2,8
-7,-6,-7,-2
1,5,4,5
1,0,0,-4
4,-4,4,-2
0,1,1,-3
7,-4,-3,1
2,0,5,4
3,-8,-6,2
-2,0,-5,-1
-5,-3,-4,-1
8,-1,5,3
3,-3,5,-1
4,-8,-6,2
6,-3,0,6
-3,-1,7,-4
3,-1,2,1
2,8,-3,-7
6,-3,-3,-8
-6,-4,7,0
7,5,3,2
-5,-3,-3,2
0,-7,-7,4
1,0,8,0
2,-6,4,7
-5,1,7,8
2,6,6,-5
0,-2,1,0
3,-7,6,4
5,-2,-5,0
1,-6,0,-6
6,-5,3,2
-2,2,1,4
2,-4,4,2
2,7,-2,4
1,6,6,0
5,-8,0,-2
8,-2,-8,4
7,-8,-2,-8
3,-8,8,-5
-8,-5,1,0
-8,0,7,-2
6,3,2,1
4,4,7,2
-3,0,-6,-4
4,-3,6,-5
0,0,0,7
6,8,-2,2
-6,6,4,-1
6,-6,1,0
-6,7,-5,0
3,-4,-7,3
-1,0,1,-3
-6,8,-2,7
0,-7,3,8
-6,7,2,8
-6,-3,8,6
6,8,6,-8
-4,-4,-4,0
4,-4,-3,0
-1,4,0,-1
3,-8,0,5
8,6,1,4
3,5,1,-2
5,-6,-1,-3
-7,1,-8,-7
8,-5,0,6
-5,3,7,7
-7,-3,-4,-2
1,-7,-5,0
5,5,-7,-8
-8,-3,-1,4
-8,3,6,8
0,-6,6,-3
1,5,2,-4
-4,-3,2,5
3,-6,1,0
-2,0,-3,7
-3,1,-1,2
5,5,4,1
4,-8,2,6
0,-6,-8,-8
-2,-1,6,-1
-1,3,2,3
-3,-7,0,-3
7,-3,1,-7
3,3,-2,2
4,3,8,-5
-5,-1,5,5
-7,-7,7,6
-8,-1,-2,0
-6,3,0,8
1,2,6,8
-7,7,1,-1
0,3,0,7
-6,-4,-1,1
-7,-8,-2,5
5,3,-6,5
4,6,0,2
-1,-7,0,5
8,-8,0,6
-8,7,6,7
0,3,-5,0
1,2,6,-8
-6,-5,7,0
-1,-7,5,0
-3,-4,7,0
8,1,0,0
0,-1,1,2
1,2,-4,1
4,-7,1,-7
7,0,0,2
6,8,3,0
-2,8,-5,-2
2,-3,3,1
-3,5,0,2
3,-7,6,1
3,1,5,0
-6,3,7,0
-2,2,4,5
-1,3,0,0
-8,2,1,7
-1,-4,-3,-2
-5,-1,-7,-1
-3,7,-1,0
4,-7,0,-5
2,5,-2,-3
0,6,-3,-6
4,3,-7,0
3,-5,-5,4
4,-7,-5,6
-4,-2,7,-6
-2,5,-1,-7
-8,0,-3,0
-2,4,-6,-6
3,5,6,8
2,1,-3,-2
-7,8,-8,-2
-7,3,-8,5
5,1,-4,1
-2,-6,7,-3
-7,8,7,-2
-1,3,-2,0
0,5,-4,2
2,-2,-1,3
-5,-3,1,3
-4,5,2,-6
-8,-8,0,3
-3,6,-6,0
-6,8,-4,-2
-7,-3,-6,-7
-6,6,-2,0
-4,2,2,-6
-8,8,5,2
3,-1,-7,-5
7,5,6,-5
-7,7,6,6
2,-6,-8,7
3,-3,-1,2
-2,0,-3,-2
-8,0,3,5
-7,-1,8,-6
3,3,3,1
-6,-8,4,-1
-6,-1,2,-4
-3,7,-2,7
6,1,5,-4
8,7,-3,8
4,-4,0,5
1,4,-5,-6
8,-5,2,4
0,-4,7,7
1,2,4,-5
-6,8,8,8
5,8,2,-5
7,-5,-6,7
-4,-2,1,-7
1,-4,1,-5
-5,5,0,2
6,-7,-2,3
-6,-5,-6,3
-5,4,3,4
-2,-4,1,0
1,-7,5,7
-5,4,1,-6
-1,-2,7,2
6,-8,-2,3
-6,-1,-4,5
7,-2,8,-4
0,3,-2,3
0,-5,3,6
-1,0,-3,-2
4,-4,2,-8
0,8,-2,8
-2,7,1,-7
8,-5,-6,6
-5,-1,-2,-8
-3,4,-4,4
4,5,8,2
8,5,-4,-1
8,6,0,2
-3,5,-3,5
3,-5,0,-4
8,2,0,5
-7,-8,4,4
-1,0,3,-4
0,-5,-4,0
2,2,3,-7
-3,7,-7,5
3,0,-3,-2
-8,-4,3,0
-3,0,5,-2
-3,-6,-8,7
5,-3,0,-1
4,-4,7,-1
6,5,8,6
0,-6,-6,8
-2,-8,1,-3
-8,-5,7,-1
-2,4,-4,-3
4,1,5,-2
-7,-3,-2,-1
2,5,1,4
-3,-3,-3,2
8,-3,8,-1
-1,4,8,-5
-6,7,-6,4
7,2,0,7
8,7,-4,8
3,2,5,-3
8,8,5,4
-6,-7,8,-7
0,-7,5,-3
5,8,4,7
4,-8,1,-5
-1,7,-2,-4
5,-5,6,0
3,-5,5,1
-4,3,3,5
4,-8,8,-8
-5,3,-4,-1
-6,8,-8,0
0,7,-2,0
-7,-3,-1,7
0,0,-4,-8
6,1,0,0
-7,0,0,5
7,-4,-5,1
5,-5,6,-4
1,0,-8,-5
4,5,-7,-7
4,2,3,6
1,5,0,8
0,4,-5,-6
0,1,-4,-8
8,5,-7,-4
3,5,-5,7
-4,-8,0,-5
-7,0,4,5
-8,6,-8,1
2,-6,2,8
3,8,0,6
0,-5,0,-5
-2,-5,5,-1
0,-4,-6,5
2,-8,-1,6
0,0,4,0
3,0,8,-1
8,-1,2,2
5,-4,4,-8
-1,7,0,-5
5,-3,0,2
-7,1,-2,2
2,-4,6,-8
7,-5,0,-8
1,0,5,-2
-1,3,1,5
6,6,-6,6
1,3,6,7
-5,4,5,0
-6,-4,6,0
-6,-2,6,-3
-4,-5,0,-5
3,-2,-7,6
8,5,1,-2
-2,-6,8,0
4,3,-3,-4
-6,-4,0,-3
-8,2,-1,-4
4,4,2,-2
-4,5,5,1
-6,-2,-7,6
-2,-5,-2,0
-2,-1,-4,-4
-3,0,3,7
-8,5,2,-5
8,7,3,-1
-7,0,4,-5
7,6,0,8
5,0,-5,-3
-2,6,8,6
-6,6,0,-6
-2,2,5,0
4,-2,-6,-8
0,0,-1,-3
4,-4,8,8
7,-2,6,1
7,-6,4,6
2,-1,0,0
-5,-1,-1,-2
-3,5,3,-5
0,-4,-7,-4
-5,6,-5,1
5,1,-1,-3
3,2,4,1
-8,5,2,3
-5,8,-7,-6
2,2,-7,1
-2,8,2,1
-2,4,-4,4
-7,2,7,-3
8,8,-8,-7
1,4,5,-4
0,2,-6,-6
0,6,6,-7
1,6,-7,-8
-1,6,-1,1
4,0,-6,7
8,7,3,-6
-2,8,7,-8
2,0,-6,1
3,-4,1,2
2,-3,4,-8
0,-8,8,-4
1,0,2,7
-2,5,0,-2
8,-4,0,0
-3,3,-6,-3
3,-1,6,-7
2,8,3,5
-3,-8,-1,-8
5,6,6,4
7,-5,5,-5
2,-2,-7,1
6,5,4,-1
7,-6,-3,-1
-7,-7,7,2
-8,6,3,-4
0,1,7,-6
-7,7,0,6
0,4,-1,0
7,-4,-7,-7
0,-5,-6,-2
-6,3,5,-6
-8,6,6,7
-8,-8,-8,8
1,1,2,-6
-4,3,7,3
8,3,0,8
-7,-5,2,-4
-3,-5,1,7
-5,-3,-8,-8
-8,8,-4,2
5,5,-2,5
1,-6,3,7
1,8,-7,0
-5,0,-6,-5
0,-4,-7,3
-8,7,1,7
-7,4,6,-8
1,7,-8,8
-8,-7,-8,3
7,5,-3,6
3,0,1,6
0,-2,4,-7
-5,4,-4,0
8,5,4,6
1,-7,0,-1
8,-7,0,-8
-1,6,-7,-5
-1,8,-3,-5
-5,6,0,-2
6,5,7,-8
1,1,3,0
-4,-6,8,7
3,8,-7,-8
-3,-8,0,-2
5,7,-6,8
-8,0,0,-7
3,-8,3,7
-2,-5,7,-1
-1,0,0,-2
7,0,1,4
-7,-2,-2,0
5,1,-7,0
-4,-6,0,2
-3,8,-2,6
7,4,-1,-2
6,-1,-6,8
-5,3,3,0
1,-2,-2,-6
-1,2,-5,3
4,-5,5,-2
-4,5,-4,6
-1,5,-1,-4
-8,-6,-6,-5
7,-8,3,-4
5,-1,-7,-6
2,8,-1,3
3,-6,0,8
-5,4,0,0
1,-7,7,0
-2,-7,-6,5
0,-5,-1,1
4,2,5,0
-6,-2,-2,6
-1,2,4,4
8,-6,0,3
6,-6,-8,-1
-4,-3,-1,8
8,0,5,-3
-6,2,-7,6
-2,4,-5,0
-4,0,-5,8
1,-3,-8,1
0,-4,2,-1
2,-6,-8,-3
5,3,7,4
-5,2,4,-5
5,-2,7,0
1,-1,-5,4
2,5,-5,5
-6,8,-5,1
1,-1,-1,-3
7,-8,8,-7
2,0,-4,2
1,3,-7,-1
8,-1,-3,4
7,-6,0,8
7,-4,-6,8
-8,4,1,8
8,7,2,-8
-3,0,7,6
6,-7,-5,0
2,-7,7,-4
1,3,-3,5
-6,-8,1,6
-2,-7,-3,-8
4,-4,-6,2
-2,4,1,8
0,3,-3,-2
2,1,6,-2
-1,-3,7,-4
8,7,-4,1
-5,2,-2,2
1,2,6,1
6,0,6,-5
7,-1,5,-2
-4,0,-4,7
7,-8,0,-3
-6,0,1,1
7,-3,5,0
0,3,6,-5
-1,-2,4,4
-7,0,1,1
-5,4,-7,-7
-4,0,-5,-1
3,8,8,8
5,0,4,1
7,6,5,0
-7,6,-1,-8
5,-4,-6,-5
-7,6,3,-1
-1,-1,3,1
2,0,-8,3
-7,-2,-2,-8
8,-5,2,-8
2,-6,-5,7
-5,-8,-4,-2
0,-4,-8,2
4,1,-1,-3
-4,0,7,4
3,-4,-7,4
-3,7,-6,8
0,4,2,-2
-8,-1,-5,-3
8,3,5,2
0,-8,0,4
0,-1,-6,4
7,-7,7,-1
3,-2,-4,-8
4,7,1,7
0,-2,7,-1
-4,4,7,-2
-1,-1,4,-8
-6,2,-8,0
8,8,7,3
2,-6,1,8
-5,3,-1,0
-4,2,7,-5
-7,-3,-1,2
-3,-2,7,4
-3,-1,-3,8
6,2,7,7
5,4,-2,0
0,-5,-1,-3
1,5,1,7
7,2,-1,-8
1,8,-3,-1
4,-1,8,7
2,8,0,6
-7,-3,5,0
0,-6,0,4
1,4,2,0
2,8,3,-1
-2,-6,0,7
0,-3,7,7
7,6,-2,-4
8,7,5,-5
-1,6,6,-7
2,-8,-5,-1
3,-5,1,6
4,-4,0,-1
3,0,-1,0
0,0,-4,-5
4,-7,5,2
-2,-1,0,1
8,-5,-7,0
-7,8,2,4
-2,-4,4,7
-1,0,1,0
7,8,-1,2
-8,7,2,8
-3,-8,-8,-2
3,-1,1,4
-5,-2,8,-8
-6,-3,3,7
-6,-1,0,-1
-4,7,-7,5
0,-2,-4,0
-8,7,0,8
7,7,3,-4
-3,-4,8,-4
0,2,-2,3
-4,-4,-4,-8
-4,0,-2,5
0,-6,6,3
-7,0,6,-8
3,5,0,0
6,-3,5,-4
5,0,-8,2
2,0,-3,0
5,4,-8,3
7,-3,8,-8
7,4,-6,5
-8,3,-8,-1
1,4,6,-3
5,-8,7,-2
3,1,7,-2
-5,8,-6,-5
2,3,-5,1
5,8,2,-2
7,-5,7,5
7,-3,3,-8
-3,-6,-8,0
2,-1,2,2
8,-6,-7,-2
-7,7,2,1
-4,8,2,-5
3,-6,2,-1
3,-6,0,-5
0,2,2,2
5,-3,5,-8
0,-2,5,-8
5,1,-7,-6
0,1,0,3
6,-4,-7,-3
-7,3,4,-8
-6,0,4,1
8,4,-4,-4
-4,-6,0,-2
-1,-3,-7,5
4,-7,-7,6
-4,1,-7,-5
1,-6,4,-7
1,7,3,0
4,-2,-7,2
4,-7,-1,6
0,-1,0,-8
-8,6,-2,-3
4,3,4,-2
8,2,4,-6
-8,-4,-7,-7
8,4,1,1
-2,7,0,3
-8,-8,8,5
2,5,3,2
-5,3,-7,8
2,0,-4,5
-7,5,-8,-3
5,7,4,-6
2,-2,6,8
-6,6,-2,7
1,0,-4,-6
-2,0,4,5
6,-7,8,0
2,7,-4,8
0,4,0,8
-8,-1,0,2
-2,-7,-5,-1
-5,0,7,3
-5,-3,0,-8
-6,7,2,6
-1,-5,-1,4
-6,8,-7,-5
7,-6,-5,-4
2,2,-2,6
0,1,-8,-2
-3,-6,7,-7
-1,0,0,0
2,-7,6,0
-2,2,-5,5
4,-6,6,3
-3,-1,4,0
8,-1,-6,-8
-3,0,-4,-6
-1,4,8,2
-5,7,-4,4
0,-4,4,4
-8,3,-8,-6
-2,0,4,-8
3,0,-8,6
-4,-8,0,-3
-5,-4,7,6
-1,0,1,-4
-1,-8,3,6
-1,6,6,-3
4,-4,-4,-1
-2,-7,1,6
4,7,4,-1
1,0,7,8
4,8,6,8
-2,-1,-4,5
3,-1,0,2
-3,-7,-7,1
0,8,0,0
0,8,-3,-1
2,6,-1,2
8,8,8,-3
-2,-7,7,8
-5,-2,-1,-7
-8,0,-7,5
0,6,-8,3
-1,-7,-3,4
-3,-3,0,1
4,0,-4,3
-5,-1,-6,-7
3,-8,3,-5
7,-8,8,3
-3,-7,1,-7
6,8,8,4
8,-4,-5,6
-6,6,-7,5
-7,4,-1,-2
-5,0,6,-8
0,-4,-8,8
-7,5,-7,-2
-5,0,1,-6
-2,0,0,-5
-3,8,-5,1
-5,-2,-3,-2
4,1,-6,1
4,0,1,-5
8,0,-8,-3
4,-5,0,-5
1,-2,7,4
-4,2,7,0
-8,2,3,0
8,-7,-1,1
0,-4,0,0
0,-6,3,-1
1,0,5,-8
-6,-7,5,6
-1,0,-7,-4
2,0,-6,6
2,-4,0,-3
2,-1,6,-2
-6,-3,7,-2
-5,-2,-6,0
4,6,0,-5
-2,-8,0,7
-5,-5,-7,-5
8,-4,-2,-2
-1,-5,-1,8
3,-7,-6,3
-8,0,5,2
-1,-2,-6,-1
8,-5,-1,-8
6,5,8,-1
6,2,1,-6
0,1,-6,4
1,0,1,-6
7,-7,-2,3
-1,1,7,-3
-4,-5,1,1
4,6,2,-1
-7,-3,0,-5
-1,1,1,4
-4,-6,0,-3
-8,0,-6,5
-1,-5,-4,7
2,3,-8,-2
7,3,6,5
2,1,7,-5
6,7,1,-4
0,1,-3,8
-1,4,8,-7
5,-8,-1,-6
5,-4,0,-6
-3,-6,1,-6
8,-7,-1,-1
-6,4,-1,5
6,5,3,5
-7,3,4,4
-3,-8,0,0
-4,0,4,-6
3,0,1,7
0,-3,6,6
0,-3,6,-6
-3,2,-1,3
5,-1,0,0
-1,0,-3,-5
-4,-3,-2,1
3,-6,5,7
0,0,-8,-5
-3,4,4,5
-8,6,-2,-5
0,-3,5,8
-6,-8,8,-2
0,5,-1,8
-2,1,-4,7
2,6,2,-6
5,-1,-5,5
2,2,1,4
-3,3,3,4
-2,3,-5,-2
4,3,8,-2
2,-4,5,-7
3,8,-5,4
-8,6,5,7
-8,1,-5,8
-6,8,3,7
-8,5,1,-5
8,1,7,4
3,3,-4,1
2,7,4,-3
-7,-4,-8,6
0,3,-1,7
-7,-4,0,-7
0,-2,8,5
6,-7,-7,0
-3,4,4,-6
2,7,-8,2
-8,-8,0,0
0,-3,4,5
8,5,2,8
-8,-4,-4,4
-6,5,5,-1
-7,8,7,2
7,0,0,5
5,-1,-8,-4
7,-5,-2,0
-6,7,-8,8
-5,3,3,2
2,-5,8,1
2,4,0,-1
-2,-4,6,7
-2,7,-3,-5
-8,8,-4,1
7,-7,0,-8
2,-5,-6,1
-3,0,-2,3
-6,-7,-8,2
-7,-7,-7,-3
4,2,-2,8
3,3,7,8
0,-6,8,6
2,-5,-2,2
-3,2,1,-8
-3,-5,7,0
-6,2,2,-6
-7,0,-4,8
0,5,-2,1
-8,6,-4,-4
0,6,-8,-5
7,2,8,5
3,7,-1,6
-1,-7,-4,-3
5,1,1,6
8,5,4,3
-4,-2,3,-5
0,4,0,-4
-2,2,-2,-3
0,3,1,6
4,0,2,8
6,2,3,0
-7,-5,-1,-8
0,-5,7,-1
-6,0,3,-3
5,4,1,-1
-1,-7,2,-7
2,-5,-3,2
1,2,4,-4
0,5,-2,-1
7,2,-7,7
3,-3,-8,8
8,8,5,-5
0,-8,-2,3
5,-6,7,-4
-3,0,6,-3
6,8,-1,7
0,7,-5,4
5,-5,-2,-7
-2,8,-7,2
-8,8,-2,-2
5,1,-3,2
0,2,5,8
-8,-7,7,-5
5,1,-8,-4
-3,0,-5,6
7,-6,-4,1
-7,8,-7,3
1,2,0,-4
-2,-2,5,4
3,4,4,0
-1,4,-3,4
-7,2,0,-7
0,-2,-8,2
-2,0,4,-5
-5,4,8,0
-1,6,0,-8
3,-2,-1,0
-5,7,-1,2
7,4,5,7
-4,0,5,1
2,6,7,-2
7,0,2,7
5,7,7,6
-2,6,0,-5
-1,3,7,0
4,-4,0,1
7,-6,2,-2
-6,-7,5,2
0,-8,3,4
0,6,-6,4
-1,3,-1,2
-3,5,-5,0
5,-3,8,-2
5,7,1,1
4,4,5,-6
0,0,1,0
3,8,0,-5
5,4,-7,0
4,-5,-7,4
-8,8,4,-5
2,7,1,-4
-1,-6,-4,-4
6,7,-7,6
2,-6,-8,-4
0,-7,8,4
-1,0,7,-1
7,4,7,-2
-5,7,-1,4
-1,3,7,4
-7,-6,-2,-4
-4,-6,4,5
-8,0,-6,-3
6,-1,-8,-2
-6,4,2,-4
0,-4,8,-4
-1,-4,-6,-4
-6,-8,-3,-4
6,1,-5,6
5,0,3,-6
-1,0,-7,-2
0,1,-6,1
-4,8,-4,2
3,-1,3,5
1,5,6,-4
5,5,-3,2
-8,0,7,-5
-3,8,3,-6
6,-5,-1,-8
-8,0,3,-3
0,-7,1,-4
-4,-1,-5,7
-6,8,-7,0
2,1,-1,1
-8,-2,2,4
0,3,-1,1
6,-4,7,-4
-6,-5,0,5
-6,3,5,8
1,-3,4,-1
7,-5,-5,0
0,-5,4,-6
-6,-6,-2,6
3,8,0,0
-4,3,1,-1
1,0,1,-3
-7,-8,-2,1
2,2,8,-6
6,8,0,-2
8,-2,-7,-4
-3,8,-8,0
6,-4,6,-1
2,1,-2,7
-2,-8,0,0
-5,-8,-4,-5
-6,-8,2,-4
7,7,-8,-6
-4,-8,-1,8
-1,-6,8,0
1,4,-1,2
-5,0,-8,-3
-7,-1,-1,-7
1,-8,0,3
-7,-5,3,0
2,3,0,-5
-2,-1,7,-4
-2,3,-2,-8
-6,0,-1,6
-8,-7,-6,6
2,-3,-3,-2
3,-8,-8,7
8,2,-4,6
-6,-6,5,-5
5,5,0,-5
-6,-8,8,-3
-5,3,7,0
7,0,3,-3
6,5,-1,0
-6,-4,0,5
-5,-8,-2,1
7,3,-5,6
-7,7,7,3
4,-7,-4,4
1,8,5,0
-6,1,-4,-2
-7,-5,-3,8
0,-6,-8,7
-5,6,-1,-3
0,2,-3,5
4,0,4,8
-2,-8,-3,1
0,8,0,-1
-1,3,2,4
-1,-3,2,5
-7,-4,-2,5
-1,-4,7,-5
-2,-3,8,6
6,-7,6,-6
-2,-7,-7,-2
-5,-5,3,-7
1,8,1,-1
6,2,0,7
1,-7,-2,0
8,-7,-3,1
4,-8,-2,-4
7,1,-1,2
2,6,0,0
8,-2,2,8
7,2,4,-6
-3,-3,0,3
-1,-4,3,-5
7,-3,0,-8
0,-1,2,-3
7,-5,6,-6
-3,-3,3,-4
-1,6,-4,0
-3,3,5,-4
-3,-7,-6,-5
8,-6,3,6
7,8,3,7
2,-7,5,-1
-6,-2,-3,6
8,-1,8,-8
-7,4,-7,2
-7,0,0,6
0,6,4,8
-8,7,8,-3
2,2,-7,-7
0,4,8,1
-4,2,-5,-3
4,6,-3,-7
4,4,-3,-4
-5,-6,0,5
5,-3,0,0
-8,-4,3,-7
2,5,0,-7
0,0,3,-8
5,-3,0,-3
7,-1,0,-5
-6,2,4,2
-2,-3,6,-2
4,-2,-7,-7
5,-5,8,6
-7,-5,3,1
5,8,-2,-5
0,3,0,3
-3,8,0,-4
7,8,-7,6
8,7,-6,-7
3,-7,6,6
-1,-2,4,7
6,-6,7,-8
-7,-2,0,-4
3,7,4,6
-3,8,0,7
-4,3,0,-4
0,0,-5,1
-6,0,-4,0
0,-6,-7,0
-3,0,-5,-6
-2,0,1,6
-1,-5,1,-7
-8,2,2,4
-1,0,2,1
5,-8,-5,-5
0,-5,-5,7
5,8,3,-3
-4,2,7,5
0,-3,2,-3
5,1,2,-1
-2,-2,-8,-1
-2,1,-1,-2
0,-3,0,2
4,0,4,1
0,0,-2,-6
3,-2,-2,6
-5,-6,8,0
-5,7,-3,3
-8,-5,8,-6
-3,-3,-3,-3
6,-3,-4,-6
5,3,-1,-6
-7,7,7,1
-8,-8,-6,7
4,7,1,-2
-6,3,-6,-1
-6,-3,6,-1
3,0,0,-7
1,-3,8,6
0,-6,3,2
2,-8,6,-8
-1,7,0,-6
6,-4,-6,0
-7,-4,-7,-1
8,0,8,4
1,1,-5,5
-4,4,1,2
1,0,7,0
-3,6,6,-8
-8,-8,-7,0
5,-4,-5,5
-6,3,-6,-3
-4,0,-2,1
2,8,-2,0
-1,3,-3,-4
-8,-4,1,2
-7,3,-1,6
-2,2,6,-2
4,6,7,0
-1,0,-5,-5
-6,0,1,-4
-8,-6,-1,-5
-8,-7,0,8
-8,-1,7,1
2,-2,0,-4
-4,5,-6,-1
-5,-4,3,3
-7,-8,-4,-3
0,5,-3,5
2,7,7,0
4,7,5,6
3,-7,4,-2
4,2,2,1
0,0,-1,-6
-3,-2,1,3
-8,1,-4,-7
5,1,-6,5
5,-4,-6,6
0,-7,7,-5
2,8,-5,2
8,3,3,-3
-2,-5,-5,-7
-1,2,8,7
-3,-8,2,-3
5,6,-8,-5
-4,2,-2,-5
6,-3,-4,-3
-1,1,1,2
-7,0,8,-3
-3,-4,-5,-1
-1,-1,-8,0
-3,-8,8,1
2,5,5,4
-2,-5,6,-4
3,0,-8,1
8,0,5,4
3,4,-1,7
-5,-4,7,1
8,-4,-1,2
-7,-6,7,8
0,2,4,0
2,-3,-2,-2
5,4,8,1
7,4,0,-5
-2,6,3,-8
-6,3,-4,5
-8,-8,-4,5
7,-4,-6,3
-4,1,1,-4
-1,5,1,3
-8,-6,3,1
-2,0,2,2
-2,-6,-5,8
8,4,8,6
-5,3,-4,7
7,-2,5,5
-1,-4,1,2
-7,-5,-6,-2
6,4,-4,8
2,7,-7,-7
-2,-2,-2,-6
1,-2,4,0
-7,-1,-2,-2
-6,-7,-3,0
-5,7,6,-7
8,-4,3,2
5,-4,-5,-4
-1,1,-4,4
1,-7,5,4
4,1,-3,5
7,-1,-2,5
7,-4,0,0
7,8,-2,-7
5,-2,7,7
-4,1,0,1
-3,-5,-2,0
3,1,6,-7
-4,-3,-5,2
-5,0,-6,-3
6,-2,5,-2
-6,5,-4,6
0,0,-2,7
-8,4,-3,6
-3,0,-2,-3
-1,-3,4,0
8,6,3,0
5,5,2,2
2,7,2,-3
1,5,-5,3
4,-4,-6,-2
0,-1,-2,6
0,8,4,3
-1,0,2,4
4,3,-6,4
-4,5,-2,-4
-3,5,0,6
8,-2,-3,0
0,-7,-3,2
0,-3,0,5
5,6,8,-6
-4,-8,8,6
0,5,-1,3
-1,8,-2,1
5,-5,-8,-2
-5,-2,-8,-1
7,5,3,5
2,1,2,1
1,-2,-5,0
-3,1,4,5
-3,5,-4,-4
1,-2,3,4
-2,5,4,0
0,5,6,-1
-3,-3,-8,-7
-4,7,7,8
-3,1,-7,-1
6,1,-6,6
1,2,8,-8
0,-7,0,2
-8,0,-8,-2
6,8,0,-7
-6,-8,-3,1
7,1,-5,8
6,0,-7,-5
-3,4,4,7
-7,4,-3,-8
-8,-4,3,-2
6,4,-2,-6
-3,-8,-3,-6
-3,6,5,2
-4,0,0,-4
-3,-7,-3,4
7,0,-5,0
2,-8,8,-7
-8,6,0,-4
0,2,2,-8
-6,8,-1,-6
-2,3,-3,5
-3,2,0,7
5,5,-1,-3
-8,2,7,0
4,4,4,1
7,3,8,8
4,-1,-7,-6
2,4,-7,-5
8,-3,-1,-8
-5,-7,-7,-7
8,7,0,2
-1,-4,-8,-5
1,0,8,-3
2,0,6,2
1,3,8,6
3,-6,7,-1
0,-6,-2,-6
-1,-5,-2,0
6,-5,-4,-2
0,7,-6,3
4,-2,0,-3
-4,1,0,-2
1,6,-2,0
-2,-3,5,7
-2,6,7,-5
8,7,6,-2
4,-5,-2,6
-7,0,-4,-6
-7,-6,-8,1
-5,-2,-6,6
-5,3,0,4
4,0,1,-6
-1,0,6,0
4,6,-4,0
6,-4,-1,5
-7,0,4,-7
7,-5,8,5
2,-2,8,-5
-7,-2,4,4
-1,1,-5,1
4,-2,1,3
-7,-3,7,-5
4,-8,-7,-4
7,4,1,-7
-4,3,6,0
0,-6,-7,6
-3,-7,-1,-5
3,-5,0,0
0,3,2,7
-5,8,-8,-6
3,0,6,6
2,-3,-6,0
-6,8,2,-5
-4,8,-5,0
-8,6,0,4
-2,-2,-3,2
0,-4,3,-7
0,7,1,2
-1,-2,1,-4
0,4,-3,-2
8,6,0,-2
-7,-8,6,0
8,-8,-5,7
-5,-3,6,0
-4,-1,-8,0
5,6,-1,6
1,7,-5,6
1,-8,0,-6
8,0,1,0
5,-2,0,4
-6,8,6,4
3,0,4,5
-5,-3,-5,-7
""".trimIndent()