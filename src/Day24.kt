import kotlin.math.min

private enum class Type {
    IMMUNE,
    INFECT
}

private enum class Attack {
    FIRE,
    COLD,
    SLASHING,
    RADIATION,
    BLUDGEONING
}

private fun extractInts(s: String) : IntArray {
    return "(-?\\d+)".toRegex().findAll(s).asIterable().map { it.value.toInt() }.toIntArray()
}

private var boost = 0

private data class Group(val id: Int, val type: Type, val units: Int, val hp: Int, val dmg: Int, val attack: Attack, val initiative: Int, val weaknesses: Set<Attack>,
                         val immunities: Set<Attack>) {
    var remaining = units

    val effectivePower: Int
        get() {
            return remaining * (if( type == Type.IMMUNE) dmg+boost else dmg)
        }

    val name: String
        get() = "$type-$id"

    val alive: Boolean
        get() = remaining > 0

    fun selectTarget(opponents: List<Group>): Group? {
        // find one
        val firstOrNull = opponents.filter { it.predictedDamage(effectivePower, attack) > 0 }
            .sortedWith(compareBy<Group>(
                { it.predictedDamage(effectivePower, attack) },
                { it.effectivePower },
                { it.initiative }).reversed())
            .firstOrNull()
//        println("$name selected target ${firstOrNull?.name}")
        return firstOrNull
    }

    fun predictedDamage(dmg: Int, type: Attack) : Int {
        if( immunities.contains(type ) ) return 0
        return if( weaknesses.contains(type) ) dmg*2 else dmg
    }

    fun applyDamage(dmg: Int, type: Attack) : Int {
        val d = predictedDamage(dmg, type)
        val unitsToRemove = min(remaining, d / hp)
        remaining -= unitsToRemove
        return unitsToRemove
    }

    companion object {

        var infId = 1
        var immId = 1
        fun parse(type: Type, s: String): Group {
            var id = 0
            if( type == Type.INFECT) {
                id = infId++
            }
            else {
                id = immId++
            }
            // 18 units each with 729 hit points (weak to fire; immune to cold, slashing)
            // with an attack that does 8 radiation damage at initiative 10
            val nums = extractInts(s)
            val dmgType = Attack.valueOf("\\d+ ([a-z]+) damage".toRegex().find(s)!!.groupValues[1].toUpperCase())
            val imm = "immune to ([a-z, ]+)+[;)]".toRegex().find(s)
            val immunities = mutableSetOf<Attack>()
            if( imm != null ) {
                imm.groupValues[1].split(", ").map { Attack.valueOf(it.toUpperCase()) }.forEach { immunities.add(it) }
            }
            val weak = "weak to ([a-z, ]+)+[;)]".toRegex().find(s)
            val weaknesses = mutableSetOf<Attack>()
            if( weak != null ) {
                weak.groupValues[1].split(", ").map { Attack.valueOf(it.toUpperCase()) }.forEach { weaknesses.add(it) }
            }
            return Group(id, type, nums[0], nums[1], nums[2], dmgType, nums[3], weaknesses, immunities)
        }
    }
}

private class Battle24(val groups: List<Group>) {

    fun part1(b: Int): Type {
        boost = b
        val remaining = groups.map { it.copy() }.toMutableList()

        var inf = remaining.filter { it.alive }.filter { it.type == Type.INFECT }
        var imm = remaining.filter { it.alive }.filter { it.type == Type.IMMUNE }

        var round = 1
        var totalUnits = remaining.map{ it.remaining}.sum()
        while( inf.isNotEmpty() && imm.isNotEmpty() ) {
            println("Starting round ${round++}")
            inf.forEach { println("${it.name} has ${it.remaining} ep ${it.effectivePower} ini ${it.initiative}") }
            imm.forEach { println("${it.name} has ${it.remaining} ep ${it.effectivePower} ini ${it.initiative}") }
            val targets = selectTargets(imm, inf)
            // attack order
            val attackOrder = remaining.sortedByDescending{ it.initiative }

            for (group in attackOrder) {
                if( !group.alive) continue
                val target = targets[group]
                if( target != null ) {
                    val killed = target.applyDamage(group.effectivePower, group.attack)
//                    println("${group.name} attacks ${target.name} for ${target.predictedDamage(group.effectivePower, group.attack)} killing ${killed}")
                    if( !target.alive){
                        // remove from remaining
//                        println("Killed: ${target.name}")
                        remaining.remove(target)
                    }
                }
            }
            inf = remaining.filter { it.alive }.filter { it.type == Type.INFECT }
            imm = remaining.filter { it.alive }.filter { it.type == Type.IMMUNE }
            val tu2 = remaining.map{ it.remaining}.sum()

            // noone killed
            if( totalUnits == tu2) return Type.INFECT
            totalUnits = tu2
        }
        // battle over
        println(remaining)
        for (group in remaining) {
            println("${group.remaining} left in $group")
        }
        println(" " + remaining.map{ it.remaining }.sum())
        return remaining.first().type
    }

    fun part2() {
        var min = 0
        var max = 97

        while( max-min > 1) {
            val t = min + (max-min)/2
            println("$min $t $max")
            if( testBoost(t)) {
                println("$t is a win")
                max = t
            }
            else {
                println("$t is a loss")
                min = t
            }
        }
    }

    fun testBoost(b: Int): Boolean {
        return part1(b) == Type.IMMUNE
    }

    fun selectTargets(imm: List<Group>, inf: List<Group>): Map<Group, Group?> {
        val result = mutableMapOf<Group, Group?>()
        val availableInf = inf.toMutableList()
        for (group in imm.sortedWith( compareBy<Group>({it.effectivePower}, {it.initiative}).reversed() )) {
            val t = group.selectTarget(availableInf)
            result[group] = t
            availableInf.remove(t)
        }

        val availableImm = imm.toMutableList()
        for( group in inf.sortedWith(compareBy<Group>({it.effectivePower}, {it.initiative}).reversed() )) {
            val t = group.selectTarget(availableImm)
            result[group] = t
            availableImm.remove(t)
        }
        return result
    }
}

fun main() {

    val imm = immune1.lines().map { Group.parse(Type.IMMUNE, it) }
    val inf = infect1.lines().map { Group.parse(Type.INFECT, it) }

    val all = imm.plus(inf)
    val b = Battle24(all)
//    b.part1(0)
//    b.part1(40)
    b.part2()
}

private val testImm1 = """
17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3
""".trimIndent()

private val testInf1 = """
801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
""".trimIndent()

private val testImm2 = """
17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3
""".trimIndent()

private val testInf2 = """
801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
""".trimIndent()

private val immune1 = """
2208 units each with 6238 hit points (immune to slashing) with an attack that does 23 bludgeoning damage at initiative 20
7603 units each with 6395 hit points (weak to radiation) with an attack that does 6 cold damage at initiative 15
4859 units each with 5904 hit points (weak to fire) with an attack that does 12 cold damage at initiative 11
1608 units each with 7045 hit points (weak to fire, cold; immune to bludgeoning, radiation) with an attack that does 31 radiation damage at initiative 10
39 units each with 4208 hit points with an attack that does 903 radiation damage at initiative 7
6969 units each with 9562 hit points (immune to slashing, cold) with an attack that does 13 slashing damage at initiative 3
2483 units each with 6054 hit points (immune to fire) with an attack that does 20 cold damage at initiative 19
506 units each with 3336 hit points with an attack that does 64 radiation damage at initiative 6
2260 units each with 10174 hit points (weak to fire) with an attack that does 34 slashing damage at initiative 5
2817 units each with 9549 hit points (immune to cold, fire; weak to bludgeoning) with an attack that does 31 cold damage at initiative 2
""".trimIndent()

private val infect1 = """
3650 units each with 25061 hit points (weak to fire, bludgeoning) with an attack that does 11 slashing damage at initiative 12
508 units each with 48731 hit points (weak to bludgeoning) with an attack that does 172 cold damage at initiative 13
724 units each with 27385 hit points with an attack that does 69 radiation damage at initiative 1
188 units each with 41786 hit points with an attack that does 416 bludgeoning damage at initiative 4
3045 units each with 36947 hit points (weak to slashing; immune to fire, bludgeoning) with an attack that does 24 slashing damage at initiative 9
7006 units each with 42545 hit points (immune to cold, slashing, fire) with an attack that does 9 fire damage at initiative 16
853 units each with 55723 hit points (weak to cold, fire) with an attack that does 114 bludgeoning damage at initiative 17
3268 units each with 43027 hit points (immune to slashing, fire) with an attack that does 25 slashing damage at initiative 8
1630 units each with 47273 hit points (weak to cold, bludgeoning) with an attack that does 57 slashing damage at initiative 14
3383 units each with 12238 hit points with an attack that does 7 radiation damage at initiative 18
""".trimIndent()