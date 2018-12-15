fun main() {
    println(score(3, 5, 8))
    println(score(122, 79, 57))
    println(score(217, 196, 39))
    println(score(101, 153, 71))

    part1(5468)
    part2(5468)
}

private fun part1(serial: Int) {
    val grid = Array(301, { x -> IntArray(301, {y -> score(x, y, serial)})})

    var maxPower = 0
    var maxCoord = Coord(0,0)

    for( x in 1..298 ) {
        for( y in 1..298) {
            val score = scoreGrid(x, y, 3, grid)
            if( score > maxPower ) {
                maxPower = score
                maxCoord = Coord(x, y)
            }
        }
    }
    println(maxPower)
    println(maxCoord)
}

private fun part2(serial: Int) {
    val grid = Array(301, { x -> IntArray(301, {y -> score(x, y, serial)})})

    var maxPower = 0
    var maxPos = ""

    for( s in 1..300 ) {
        println("Size $s")
        for( x in 1..(300-s+1)) {
            for( y in 1..(300-s+1)) {
                val score = scoreGrid(x, y, s, grid)
                if( score > maxPower ) {
                    println("$score at $x,$y,$s")
                    maxPower = score
                    maxPos = "$x,$y,$s"
                }
            }
        }
    }
    println(maxPos)
}

private fun scoreGrid(x:Int, y:Int, s:Int, grid:Array<IntArray>):Int {
    var score = 0
    for (x2 in 0..s-1) {
        for (y2 in 0..s-1) {
            score += grid[x + x2][y + y2]
        }
    }
    return score
}

private fun score(x:Int, y:Int, serial:Int):Int {
    val rack = x+10
    var power = y * rack
    power += serial
    power *= rack
    power = (power/100) % 10
    power -= 5
    return power
}