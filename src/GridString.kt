class GridString(val default: Char = '.') {

    private val chars = mutableMapOf<Coord, Char>()

    private val supplemental = mutableMapOf<Int, String>()

    fun add(coord: Coord, char: Char): GridString {
        chars.put(coord, char)
        return this
    }

    fun <T> addAll(m: Map<Coord, T>, transformer: (v: T) -> Char ): GridString {
        m.entries.forEach{ chars[it.key] = transformer.invoke(it.value)}
        return this
    }

    fun <T> addAllExtra(m: Map<Coord, T>, transformer: (v: T) -> Pair<Char, String?> ): GridString {
        m.entries.forEach{
            val r = transformer.invoke(it.value)
            chars[it.key] = r.first
            if( r.second != null ) {
                supplemental.merge(it.key.y, " ${r.second}", {a,b -> a+b })
            }
        }
        return this
    }

    override fun toString(): String {
        return toString(false)
    }

    fun toString(nums: Boolean): String {
        if( chars.isEmpty() ) return ""
        var xMin = chars.keys.map(Coord::x).min()!!
        var xMax = chars.keys.map(Coord::x).max()!!
        var yMin = chars.keys.map(Coord::y).min()!!
        var yMax = chars.keys.map(Coord::y).max()!!


        val result = StringBuilder()

        if( nums ) {
            // print header
            if( xMax > 9 ) {
                result.append("    ")
                for( x in xMin..xMax ) {
                    result.append((x /10) % 10)
                }
                result.append(System.lineSeparator())
            }
            result.append("    ")
            for( x in xMin..xMax ) {
                result.append(x % 10)
            }
            result.append(System.lineSeparator())

        }

        for( y in yMin..yMax ) {
            if( nums ) {
                result.append(" %2d ".format(y))
            }
            for( x in xMin..xMax ) {
                result.append( chars[Coord(x,y)] ?: default)
            }
            if( supplemental.containsKey(y)) {
                result.append(supplemental[y])
            }
            result.append(System.lineSeparator())
        }
        return result.toString()
    }
}

fun main() {
    val gs = GridString()

    gs.add(Coord(0,0), 'x')
    gs.add(Coord(10,14), 'Z')
    gs.add(Coord(5,3), 'Z')

    println(gs)
    println(gs.toString(true))
}