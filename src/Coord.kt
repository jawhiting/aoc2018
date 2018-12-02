class Coord(val x: Int, val y: Int) {

    fun move(d: Direction): Coord {
        return Coord(this.x+d.x, this.y+d.y)
    }
}