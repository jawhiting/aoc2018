import java.util.*

fun main() {
//    println(winningScore(9, 25))
//    println(winningScore(10, 1618))
//    println(winningScore(13, 7999))
//    println(winningScore(17, 1104))
//    println(winningScore(21, 6111))
//    println(winningScore(30, 5807))
//    println(winningScore(424, 71482))

    println(winningScore2(9, 25, true))
    println(winningScore2(10, 1618))
    println(winningScore2(13, 7999))
    println(winningScore2(17, 1104))
    println(winningScore2(21, 6111))
    println(winningScore2(30, 5807))
    println(winningScore2(424, 71482))
//
    println(winningScore2(424, 7148200))
}


private class Marble(val value: Int) {
    private var next: Marble = this
    private var prev: Marble = this

    fun insertAfter(marbleToInsert: Marble): Marble {
        marbleToInsert.next = next
        next.prev = marbleToInsert
        next = marbleToInsert
        marbleToInsert.prev = this
        return this
    }

    fun nextX(count: Int): Marble {
        if( count == 0 ) return this
        return next.nextX(count-1)
    }

    fun prevX(count: Int): Marble {
//        println("Prev: $count Val: $value")
        if( count == 0 ) return this
        return prev.prevX(count-1)
    }

    fun remove(): Marble {
        prev.next = next
        next.prev = prev
        return next
    }

}

fun winningScore2(playerCount: Int, lastBall: Int, log: Boolean = false): Long {
    val root = Marble(0)
    var current = root

    var currentPlayer = 0
    val scores = LongArray(playerCount)


    for( ball in 1..lastBall ) {
        if( ball % 100000 == 0 ) println("Ball $ball")
        if( ball % 23 == 0 ) {
            scores[currentPlayer] += ball.toLong()
            // 7 counterclockwise
            current = current.prevX(7)

            scores[currentPlayer] += current.value.toLong()

            current = current.remove()

        }
        else {
            // put between 1 and 2 clockwise
            current = current.nextX(1)
            current.insertAfter(Marble(ball))
            current = current.nextX(1)
        }
        if( log ) {
            val sb = StringBuffer()
            sb.append(ball).append('\t')
            var toLog = root
            do {
                var out = "" + toLog.value
                if (toLog == current) {
                    out = "($out)"
                }
                sb.append(out).append('\t')
                toLog = toLog.nextX(1)
            } while( toLog != root )

            println(sb)
        }
        currentPlayer = (currentPlayer+1)%playerCount

    }
    return scores.max()!!
}

fun winningScore(playerCount: Int, lastBall: Int): Int {
    val marbles = ArrayList<Int>(8000000)
    var current = 0
    marbles.add(0)

    val log = false

    var currentPlayer = 0
    val scores = IntArray(playerCount)



    for( ball in 1..lastBall ) {
        if( ball % 100000 == 0 ) println("Ball $ball")
        if( ball % 23 == 0 ) {
            scores[currentPlayer] += ball
            // 7 counterclockwise
            var removePos = (current - 7) % marbles.size
            if( removePos < 0 ) {
                removePos = marbles.size+removePos
            }
            scores[currentPlayer] += marbles[removePos]
            marbles.removeAt(removePos)
            current = removePos
            if( current > marbles.lastIndex ) {
                current = 0
            }
        }
        else {
            // put between 1 and 2 clockwise
            val insertPos = (current + 2) % marbles.size
            marbles.add(insertPos, ball)
            current = insertPos
        }
        if( log ) {
            val sb = StringBuffer()
            sb.append(ball).append('\t')
            for (i in marbles.indices) {
                var out = "" + marbles[i]
                if (i == current) {
                    out = "($out)"
                }
                sb.append(out).append('\t')
            }
            println(sb)
        }
        currentPlayer = (currentPlayer+1)%playerCount

    }
    //scores.forEachIndexed{ i: Int, s: Int -> println("$i has $s")}

    //println("Winning score is: ${scores.max()}")
    return scores.max()!!
}