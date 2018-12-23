import java.util.*

class PathFinder {


    companion object {


        fun <T> dijkstra2(start: T, target: T, nextMoves: (T) -> Iterable<Pair<T, Int>>): Pair<Map<T, Int>, Map<T, T>> {
            val distances = mutableMapOf<T, Int>()

            distances[start] = 0
            val prev = mutableMapOf<T, T>()
            val unvisited = PriorityQueue<Pair<T, Int>> { a, b -> a.second.compareTo(b.second) }
            unvisited.add(start to 0)
            var count = 0
            while (unvisited.isNotEmpty()) {

                if (count++ % 100 == 0) {
                    println("Count: $count Unvisited: ${unvisited.size}")
                }
                // get the closest node
                val n = unvisited.poll()!!

                if( n.first == target ) {
                    break
                }
                if (n.second == Int.MAX_VALUE) {
                    // finished
                    break
                }
                // find available neighbours
                val neighbors = nextMoves(n.first)
                for (neighbor in neighbors) {

                    val dist = n.second + neighbor.second
                    if (dist < distances[neighbor.first] ?: Integer.MAX_VALUE) {
                        distances[neighbor.first] = dist
                        prev[neighbor.first] = n.first
                        unvisited.add(neighbor.first to dist)
                    }
                }
            }
            return distances to prev
        }
    }
}