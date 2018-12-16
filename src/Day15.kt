import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*


internal enum class Race {
    E, G
}


internal data class Node(val position: Coord, val open: Boolean, var occupant: Entity? = null) : Comparable<Node> {

    override fun compareTo(other: Node): Int {
        return position.compareTo(other.position)
    }

    val canEnter: Boolean
        get() {
            return open && occupant == null
        }



    fun emptyNeighbours(nodes: Map<Coord, Node>): SortedSet<Node> {
        return neighbours(nodes).filter(Node::canEnter).toSortedSet()
    }

    fun occupiedNeighbours(nodes: Map<Coord, Node>): SortedSet<Node> {
        return neighbours(nodes).filter { it.occupant != null }.toSortedSet()

    }

    fun neighbours(nodes: Map<Coord, Node>): SortedSet<Node> {
        return Direction.values().map { position.move(it) }.mapNotNull { nodes[it] }.filter(Node::open).toSortedSet()
    }
}

internal data class Entity(val race: Race, var position: Coord, var hp: Int = 200) : Comparable<Entity> {
    override fun compareTo(other: Entity): Int {
        return position.compareTo(other.position)
    }

    val alive: Boolean
        get() {
            return hp > 0
        }

    fun canMove(nodes: Map<Coord, Node>): Boolean {
        return nodes[position]!!.neighbours(nodes).isNotEmpty()
    }

    fun getTarget(nodes: Map<Coord, Node>): Entity? {
        // ordered by hp then position
        return nodes[position]!!.occupiedNeighbours(nodes).mapNotNull(Node::occupant).filterNot { it.race == race }.sortedWith(
            compareBy( { it.hp }, {it.position})).firstOrNull()
    }

    fun emptyNeighbours(nodes: Map<Coord, Node>): SortedSet<Node> {
        return nodes[position]!!.emptyNeighbours(nodes)
    }

    fun move(battle: Battle) {
        // can I move?
        if( !canMove(battle.nodes)) return
        // find all enemies with empty neighbours
        val targets = battle.entities.filter(Entity::alive).filterNot { it.race == race }.flatMap { it.emptyNeighbours(battle.nodes) }.map{ it.position} .toSortedSet()
        // No viable targets
        if( targets.isEmpty() ) return

        // find closest empty neighbour
        val paths = dijkstra(position, battle.nodes)
        // combine targets with distances to find closest
        val closestTarget = targets.map{ it to paths.first[it] }.filter { it.second != null }.minBy { it.second ?: Int.MAX_VALUE }
        // can't get to target
        if( closestTarget == null || closestTarget.second ?: Int.MAX_VALUE == Int.MAX_VALUE) return

        // step towards, making sure to account for equal options
        // Get the path to this target
        val path = path(position, closestTarget.first, paths.second)
        // next step
        val step = path.first()
        battle.nodes[position]!!.occupant = null
        battle.nodes[step]!!.occupant = this
        this.position = step
    }

}

private fun dijkstra(start: Coord, nodes: Map<Coord, Node>): Pair<Map<Coord, Int>, Map<Coord, Coord>> {
    val distances = mutableMapOf<Coord, Int>()

    nodes.keys.forEach{ distances.put(it, Integer.MAX_VALUE)}
    distances[start] = 0
    val prev = mutableMapOf<Coord, Coord>()
    val unvisited = nodes.keys.toMutableSet()

    while( unvisited.isNotEmpty() ) {
        // get the closest node
        val n = unvisited.map { it to distances[it]!! }.sortedWith(compareBy({ it.second }, {it.first })).first()

        if( n.second == Int.MAX_VALUE ) {
            // finished
            break
        }
        // find available neighbours
        val neighbors = nodes[n.first]!!.emptyNeighbours(nodes)
        val dist = n.second+1
        for (neighbor in neighbors) {
            if( dist < distances[neighbor.position] ?: Integer.MAX_VALUE ) {
                distances[neighbor.position] = dist
                prev[neighbor.position] = n.first
            }
        }
        unvisited.remove(n.first)
    }

//    println(distances)
//    println(prev)
//
//    println(GridString().addAll(distances) { d -> if( d == Int.MAX_VALUE ) '#' else ('0'+d%10)}.toString(true))

    return distances to prev
}

private fun path(start: Coord, end: Coord, prev: Map<Coord, Coord>) : List<Coord> {
    val result = LinkedList<Coord>()

    var current = end
    while( current != start ) {
        result.addFirst(current)
        current = prev[current]!!
    }

    return result
}


internal class Battle(val nodes: Map<Coord, Node>, val entities: List<Entity>) {

    fun part1():Int {
        val score = run()
        println(score)
        return score
    }

    fun part2(elfAttack: Int, log: Boolean = false) : Int? {
        println("Starting with attack power $elfAttack")
        var cycles = 0

        val elves = entities.filter { it.race == Race.E }

        while(tick(elfAttack)) {
            println("Cycle $cycles")

            if( elves.filterNot( Entity::alive).isNotEmpty() ) {
                println("Elf died on power $elfAttack")
                return null
            }
            if( log) println("Ended cycle $cycles")
            if( log) println(this)
            cycles++

        }
        println("Combat ended after $cycles")
        println(this)
        return cycles * entities.filter(Entity::alive).map { it.hp }.sum()
    }

    fun run(log: Boolean = false, elfAttack: Int = 3): Int {
        var cycles = 0
        val frames = mutableListOf<BufferedImage>()

        while(tick(elfAttack)) {
            println("Cycle $cycles")

            if( log) println("Ended cycle $cycles")
            if( log) println(this)
            frames.add(toImage().toImage())
            cycles++

        }
        GridImage.animate(frames, "day15.gif")
        println("Combat ended after $cycles")
        println(this)
        return cycles * entities.filter(Entity::alive).map { it.hp }.sum()
    }
    
    fun tick(elfAttack: Int = 3): Boolean {
        // get all entities in order
        var toAct = entities.filter(Entity::alive).sorted()

        for (entity in toAct) {
            // skip if dead
            if( !entity.alive ) continue
            // Are there any targets for me
            if( entities.filter(Entity::alive).filterNot{ entity.race == it.race }.isEmpty() ) {
                println("Combat over")
                println(this)
                return false
            }
            // does it need to move?
            if( entity.getTarget(nodes) == null ) {
                // move
                entity.move(this)
            }

            val target = entity.getTarget(nodes)
            if( target != null ) {

                // attack
                target.hp -= if( entity.race == Race.E ) elfAttack else 3
                if( !target.alive ) {
                    println("Killed $target")
                    nodes[target.position]!!.occupant = null
                }
            }
        }
        return true
    }


    companion object {
        fun parse(grid: String): Battle {
            val entities = mutableMapOf<Coord, Entity>()
            val nodes = mutableMapOf<Coord, Node>()

            grid.lines().forEachIndexed { y, s ->
                s.forEachIndexed { x, c ->
                    run {
                        val coord = Coord(x, y)
                        when (c) {
                            'E', 'G' -> {
                                entities[coord] = Entity(Race.valueOf("" + c), coord)
                                nodes[coord] = Node(coord, true, entities[coord])
                            }
                            '#', '.' -> nodes[coord] = Node(coord, c == '.')
                        }
                    }
                }
            }



            return Battle(nodes, entities.values.toList())
        }
    }

    override fun toString(): String {
        return GridString().addAllExtra(nodes) {
            if( !it.open ) {
                Pair('#', null)
            } else {
                if( it.occupant != null ) {
                    Pair(it.occupant!!.race.name.first(), "${it.occupant!!.hp}")
                }
                else {
                    Pair('.', null)
                }
            }
        }.toString(true)
        //return GridString().addAll(nodes) { if( !it.open ) '#' else it.occupant?.race?.name?.first() ?: '.'}.toString(true)
    }

    fun toImage(): GridImage {
        val grid = GridImage(10)
        grid.addAll(nodes) {
            if( !it.open ) {
                Color.DARK_GRAY
            } else {
                if( it.occupant != null ) {
                    when(it.occupant!!.race) {
                        Race.E -> Color.GREEN
                        Race.G -> Color.RED
                    }
                }
                else {
                    Color.LIGHT_GRAY
                }
            }
        }
        return grid
    }
}

fun main() {
    testRun()
//    part2()
}

private fun part2() {
    for( ap in 4..100 ) {
        val b = Battle.parse(input)
        val score = b.part2(ap)
        if( score != null ) {
            println("Attack $ap Score $score")
            break
        }
    }
}

private fun testMovement() {
    val b = Battle.parse(test1)
    println(b)
    //    dijkstra(Coord(1,1), b.nodes)
    val routes = dijkstra(Coord(3,2), b.nodes)
    println(path(Coord(3,2), Coord(1,1), routes.second))

    val b2 = Battle.parse(testMovement1)
    print(b2)
    b2.tick()
    print(b2)
    b2.tick()
    print(b2)
    b2.tick()
    print(b2)
}

private fun testCombat() {
    val b = Battle.parse(testCombat1)
    b.entities.forEach{ it.hp = 2}
    b.entities.find { it.position == Coord(2,1) }!!.hp = 4
    b.entities.filter { it.race == Race.E }.forEach{ it.hp = 200}
    println(b)
    b.tick()
    println(b)
}

private fun testRun() {
    val b = Battle.parse(input2)

    b.toImage().write("day15.gif")
    println(b.run())
}

private val testRun = """
#######
#.G...#
#...EG#
#.#.#G#
#..G#E#
#.....#
#######
""".trimIndent()

private val testRun2 = """
#######
#G..#E#
#E#E.E#
#G.##.#
#...#E#
#...E.#
#######
""".trimIndent()

private val testRun3 = """
#######
#E..EG#
#.#G.E#
#E.##E#
#G..#.#
#..E#.#
#######
""".trimIndent()

private val testRun4 = """
#######
#E.G#.#
#.#G..#
#G.#.G#
#G..#.#
#...E.#
#######
""".trimIndent()

private val testRun5 = """
#######
#.E...#
#.#..G#
#.###.#
#E#G#G#
#...#G#
#######
""".trimIndent()

private val testRun6 = """
#########
#G......#
#.E.#...#
#..##..G#
#...##..#
#...#...#
#.G...G.#
#.....G.#
#########
""".trimIndent()

private val testCombat1 = """
G....  9
..G..  4
..EG.  2
..G..  2
...G.  1
""".trimIndent()

private val testX = """
#######
#.G.E.#
#E.G.E#
#.G.E.#
#######
""".trimIndent()

private val test1 = """
#######
#...E.#
#....E#
#.G.E.#
#######
""".trimIndent()

private val testMovement1 = """
#########
#G..G..G#
#.......#
#.......#
#G..E..G#
#.......#
#.......#
#G..G..G#
#########
""".trimIndent()

private val input = """
################################
###################..###########
#################.....##########
################.......#########
################......#####...##
#################.....G###.....#
###########.#####....#####..####
###########..####.#.###....#####
##########.GG#.....##......#####
###########.........#...G..#####
###########....GG..........#####
##########G.GG....G....GG..#####
#########.G...#####........#####
#######.G...G#######.E...E..####
###########.#########.......####
##########..#########......#####
##...#####..#########.G....#####
####..#..#..#########.#.....####
###.G.#.....#########..#..#.E###
#####........#######.......E.###
#####.........#####.......######
######............E....E..######
#G.###..G.................######
#..####...............#....#####
#....E#...G.......######...#####
#.............E..#######.#######
###.........E#....##############
######.E.....#..################
#######..........###############
#######......#.#################
#####...#...####################
################################
""".trimIndent()

private val input2 = """
################################
#################.....##########
#################..#.###########
#################.........######
##################......########
#################G.GG###########
###############...#..###########
###############......G..########
############..G.........########
##########.G.....G......########
##########......#.........#..###
##########...................###
#########G..G.#####....E.G.E..##
######..G....#######...........#
#######.....#########.........##
#######..#..#########.....#.####
##########..#########..G.##..###
###########G#########...E...E.##
#########.G.#########..........#
#########GG..#######.......##.E#
######.G......#####...##########
#...##..G..............#########
#...#...........###..E.#########
#.G.............###...##########
#................###############
##.........E.....###############
###.#..............#############
###..G........E.....############
###......E..........############
###......#....#E#...############
###....####.#...##.#############
################################
""".trimIndent()