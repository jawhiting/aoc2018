import javafx.geometry.Pos
import java.util.*
import kotlin.math.min

private enum class Region(val e1: Equip, val e2: Equip) {
    ROCKY(Equip.CLIMBING, Equip.TORCH),
    WET(Equip.CLIMBING, Equip.NEITHER),
    NARROW(Equip.TORCH, Equip.NEITHER);

    val equip: Set<Equip>
        get() {
            return setOf(e1, e2)
        }
}

private enum class Equip {
    TORCH, CLIMBING, NEITHER
}


private class Calculator(val depth: Long, val start: Coord, val target: Coord) {

    private val geoScores = mutableMapOf<Coord, Long>()
    private val eroScores = mutableMapOf<Coord, Long>()
    private val risks = mutableMapOf<Coord, Region>()

    init {
        geoScores[start] = 0
        geoScores[target] = 0
    }

    fun part1() {
        var total = 0

        for( x in start.x..target.x) {
            for( y in start.y..target.y) {
                val c = Coord(x,y)
                val risk = risk(c, depth)
                total += risk.ordinal
            }
        }
        println(total)
    }

    fun part2() {
        val start = Position(Coord(0,0), Equip.TORCH)

        val result = PathFinder.dijkstra2(start, Position(target, Equip.TORCH), this::nextMoves)
//        val result = dijkstra2(start)
        println(result.first[Position(target, Equip.TORCH)])
        // 995 too low
        // 1003 too low
        // 1004 is correct
        var pos: Position? = Position(target, Equip.TORCH)
        val route = LinkedList<Position>()
        while( pos != null ) {
            val nextPos = result.second[pos]
            route.addFirst(pos)
            pos = nextPos
        }
        for (position in route) {
            println("Dist: ${result.first[position]} Pos: $position Reg: ${risk(position.c, depth)}")
        }
    }

    data class Position(val c: Coord, val e: Equip) {

    }

    private fun dijkstra2(start: Position): Pair<Map<Position, Int>, Map<Position, Position>> {
        val distances = mutableMapOf<Position, Int>()

        distances[start] = 0
        val prev = mutableMapOf<Position, Position>()
        val unvisited = PriorityQueue<Pair<Position, Int>> { a, b -> a.second.compareTo(b.second)}
        unvisited.add(start to 0)
        var count = 0
        while( unvisited.isNotEmpty() ) {

            if( count++% 100 == 0) {
                println("Count: $count Unvisited: ${unvisited.size}")
            }
            // get the closest node
            val n = unvisited.poll()!!

            if( n.second == Int.MAX_VALUE ) {
                // finished
                break
            }
            if( n.first.c == target && n.first.e == Equip.TORCH) {
                println("Reached")
                break
            }
            // find available neighbours
            val neighbors = nextMoves(n.first)
            for (neighbor in neighbors) {

                val dist = n.second + neighbor.second
                if( dist < distances[neighbor.first] ?: Integer.MAX_VALUE ) {
                    distances[neighbor.first] = dist
                    prev[neighbor.first] = n.first
                    unvisited.add(neighbor.first to dist)
                }
            }
        }

        return distances to prev
    }



    private fun nextMoves(current: Position): List<Pair<Position, Int>> {
        val moves = mutableListOf<Pair<Position, Int>>()
        val currentRegion = risk(current.c, depth)
        for (direction in Direction.values()) {
            val nextCell = current.c.move(direction)
            if( nextCell.x < 0 || nextCell.y < 0 ) continue
            if( nextCell.x > target.x*2 || nextCell.y > target.y*2) continue
            val nextRegion = risk(nextCell, depth)
            for (equip in nextRegion.equip.intersect(currentRegion.equip)) {
                val nextPosition = Position(nextCell, equip)
                val cost = if(current.e == equip ) 1 else 8
                moves.add( nextPosition to cost)
            }
        }
        return moves
    }

    private fun geologic(c: Coord, d: Long): Long {
        val gs = geoScores[c]
        if (gs != null) return gs
        val s2 =
            if (c.x == 0) {
                c.y * 48271L
            } else if (c.y == 0) {
                c.x * 16807L
            } else {
                erosion(Coord(c.x - 1, c.y), d) * erosion(Coord(c.x, c.y - 1), d)
            }

        geoScores[c] = s2
        return s2
    }


    private fun erosion(c: Coord, depth: Long): Long {
        val es = eroScores[c]
        if (es != null) return es
        val s = (geologic(c, depth) + depth) % 20183
        eroScores[c] = s
        return s
    }

    private fun risk(c: Coord, depth: Long): Region {
        val r = risks[c]
        if( r != null ) return r
        val r2 = erosion(c, depth) % 3L
        val r3 = Region.values()[r2.toInt()]
        risks[c] = r3
        return r3
    }

}

private fun main() {

    val calculator = Calculator(510, Coord(0, 0), Coord(10, 10))
    calculator.part1()
    calculator.part2()
    Calculator(7305, Coord(0,0), Coord(13, 734)).part2()

}