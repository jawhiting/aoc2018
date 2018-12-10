

data class Point(val initial: Coord, val velocity: Coord) {
    var position: Coord = initial

    fun tick(count: Int) : Coord {
        position = Coord(position.x + velocity.x*count, position.y + velocity.y*count)
        return position
    }
}

fun main() {
    val points =
        testInput.lines().map { extractInts(it) }.map { Point(Coord(it[0], it[1]), Coord(it[2], it[3])) }.toList()

    val point = Point(Coord(3, 9), Coord(1, -2))

    println(point.tick(3))

    for( i in 0..10 ) {
        points.forEach{it.tick(1)}
        println("$i rows: ${yCount(points)}")
    }

}

private fun yCount(points: List<Point>) : Int {
    points.map { it.position.y }.forEach{print("$it,")}
    println()
    return points.map { it.position.y }.toSet().size
}

private fun extractInts(s: String) : IntArray {
    return "(\\d+)".toRegex().findAll(s).asIterable().map { it.value.toInt() }.toIntArray()
}


private val testInput = "position=< 9,  1> velocity=< 0,  2>\n" +
        "position=< 7,  0> velocity=<-1,  0>\n" +
        "position=< 3, -2> velocity=<-1,  1>\n" +
        "position=< 6, 10> velocity=<-2, -1>\n" +
        "position=< 2, -4> velocity=< 2,  2>\n" +
        "position=<-6, 10> velocity=< 2, -2>\n" +
        "position=< 1,  8> velocity=< 1, -1>\n" +
        "position=< 1,  7> velocity=< 1,  0>\n" +
        "position=<-3, 11> velocity=< 1, -2>\n" +
        "position=< 7,  6> velocity=<-1, -1>\n" +
        "position=<-2,  3> velocity=< 1,  0>\n" +
        "position=<-4,  3> velocity=< 2,  0>\n" +
        "position=<10, -3> velocity=<-1,  1>\n" +
        "position=< 5, 11> velocity=< 1, -2>\n" +
        "position=< 4,  7> velocity=< 0, -1>\n" +
        "position=< 8, -2> velocity=< 0,  1>\n" +
        "position=<15,  0> velocity=<-2,  0>\n" +
        "position=< 1,  6> velocity=< 1,  0>\n" +
        "position=< 8,  9> velocity=< 0, -1>\n" +
        "position=< 3,  3> velocity=<-1,  1>\n" +
        "position=< 0,  5> velocity=< 0, -1>\n" +
        "position=<-2,  2> velocity=< 2,  0>\n" +
        "position=< 5, -2> velocity=< 1,  2>\n" +
        "position=< 1,  4> velocity=< 2,  1>\n" +
        "position=<-2,  7> velocity=< 2, -2>\n" +
        "position=< 3,  6> velocity=<-1, -1>\n" +
        "position=< 5,  0> velocity=< 1,  0>\n" +
        "position=<-6,  0> velocity=< 2,  0>\n" +
        "position=< 5,  9> velocity=< 1, -2>\n" +
        "position=<14,  7> velocity=<-2,  0>\n" +
        "position=<-3,  6> velocity=< 2, -1>"