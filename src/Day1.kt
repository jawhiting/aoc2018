import java.io.File
import java.util.function.ToIntFunction

fun main() {
    val deltas = File("/Users/jwhiting/IdeaProjects/aoc2018/src/Day1.txt").bufferedReader().lines()
        .mapToInt({ Integer.valueOf(it) }).toArray();

    println("Part 1 ${deltas.sum()}")

    val seen = HashSet<Int>()
    var current = 0
    for( i in 1..1000000 ) {
        current += deltas[i%deltas.size]
        if( !seen.add(current) ) {
            println("Part 2 ${current} in ${i}")
            break
        }
    }
}