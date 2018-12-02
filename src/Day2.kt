import java.io.File
import kotlin.streams.toList

fun main() {
    val codes = File("/Users/jwhiting/IdeaProjects/aoc2018/src/Day2.txt").bufferedReader().lines().toList().filterNotNull();

    val twoCount = codes.map{ counts(it) }.filter { hasCount(it, 2) }.count()
    val threeCount = codes.map{ counts(it) }.filter { hasCount(it, 3) }.count()

    println(twoCount)
    println(threeCount)
    println(twoCount*threeCount)
    // 54 wrong

    for( i in 0..codes.lastIndex-1) {
        for( j in i+1..codes.lastIndex) {
            if( oneDiff(codes[i], codes[j])) {
                println(codes[i])
                println(codes[j])
            }
        }
    }
}

fun oneDiff(s1: String, s2: String ) : Boolean {
    if ( s1.length != s2.length ) return false

    var diffFound = false

    for( i in 0..s1.lastIndex ) {
        if( s1[i] != s2[i] ) {
            if( diffFound ) {
                return false
            }
            else {
                diffFound = true
            }
        }
    }
    return diffFound
}

fun hasCount(counts: Map<Char, Int>, want: Int): Boolean {
    return counts.containsValue(want)
}

fun counts(s: String): Map<Char, Int> {
     return s.groupingBy { it }.eachCount()
}
