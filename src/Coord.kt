import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Coord(val x: Int, val y: Int) : Comparable<Coord> {
    override fun compareTo(other: Coord): Int {
        val xs = y.compareTo(other.y)
        if( xs == 0 ) return x.compareTo(other.x)
        return xs
    }

    fun move(d: Direction): Coord {
        return Coord(this.x+d.x, this.y+d.y)
    }
}

class Rect(val c1: Coord, val c2: Coord) {

    val topLeft: Coord = Coord(min(c1.x, c2.x), min(c1.y, c2.y))
    val bottomRight: Coord = Coord(max(c1.x, c2.x), max(c1.y, c2.y))

    constructor(topLeft: Coord, width: Int, height: Int) : this(topLeft, Coord(topLeft.x+width, topLeft.y+height))

    val area: Int get() = abs((bottomRight.x-topLeft.x) * (bottomRight.y-topLeft.y))

    fun overlap(other: Rect) : Boolean {
        return inside(other.topLeft) || inside(other.bottomRight)
    }



    fun inside(c: Coord) : Boolean {
        return c.x >= topLeft.x && c.x <= bottomRight.x && c.y >= topLeft.y && c.y <= bottomRight.y
    }
}


fun main() {
    val l = sortedSetOf(Coord(9, 0), Coord(1,1), Coord(5, 0))
    println(l)
}