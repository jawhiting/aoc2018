
private val nodes = mutableMapOf<Char, Node>()

private data class Node(val id: Char, val inputs: MutableSet<Char>, var complete: Boolean = false) {

    fun available(): Boolean {
        // available if not complete and all inputs are complete
        if( !complete ) {
            return inputs.count { !nodes[it]!!.complete } == 0
        }
        return false
    }
}

fun main() {
    input7.lines().forEach {
        val i = it[5]
        val n = it[36]
        // get node n
        val node = nodes.getOrPut(n, {Node(n, mutableSetOf())})
        // make sure input node exists
        nodes.putIfAbsent(i, Node(i, mutableSetOf()))
        node.inputs.add(i)
    }

    println(nodes)

    var availableNodes = nodes.values.filter { it.available() }

    while( availableNodes.isNotEmpty() ) {
        // just
        // output in alphabetical
        val firstNode = availableNodes.map { it.id }.sorted().first()
        print(firstNode)
        // mark them complete
        nodes[firstNode]!!.complete = true
        // get next available
        availableNodes = nodes.values.filter { it.available() }
    }
}



private val testInput = "Step C must be finished before step A can begin.\n" +
        "Step C must be finished before step F can begin.\n" +
        "Step A must be finished before step B can begin.\n" +
        "Step A must be finished before step D can begin.\n" +
        "Step B must be finished before step E can begin.\n" +
        "Step D must be finished before step E can begin.\n" +
        "Step F must be finished before step E can begin."

private val input7 = "Step B must be finished before step G can begin.\n" +
        "Step G must be finished before step J can begin.\n" +
        "Step J must be finished before step F can begin.\n" +
        "Step U must be finished before step Z can begin.\n" +
        "Step C must be finished before step M can begin.\n" +
        "Step Y must be finished before step I can begin.\n" +
        "Step Q must be finished before step A can begin.\n" +
        "Step N must be finished before step L can begin.\n" +
        "Step O must be finished before step A can begin.\n" +
        "Step Z must be finished before step T can begin.\n" +
        "Step I must be finished before step H can begin.\n" +
        "Step L must be finished before step W can begin.\n" +
        "Step F must be finished before step W can begin.\n" +
        "Step T must be finished before step X can begin.\n" +
        "Step A must be finished before step X can begin.\n" +
        "Step K must be finished before step X can begin.\n" +
        "Step S must be finished before step P can begin.\n" +
        "Step M must be finished before step E can begin.\n" +
        "Step E must be finished before step W can begin.\n" +
        "Step D must be finished before step P can begin.\n" +
        "Step P must be finished before step W can begin.\n" +
        "Step X must be finished before step H can begin.\n" +
        "Step V must be finished before step W can begin.\n" +
        "Step R must be finished before step H can begin.\n" +
        "Step H must be finished before step W can begin.\n" +
        "Step N must be finished before step I can begin.\n" +
        "Step X must be finished before step R can begin.\n" +
        "Step D must be finished before step V can begin.\n" +
        "Step V must be finished before step R can begin.\n" +
        "Step F must be finished before step K can begin.\n" +
        "Step P must be finished before step R can begin.\n" +
        "Step P must be finished before step V can begin.\n" +
        "Step S must be finished before step X can begin.\n" +
        "Step I must be finished before step S can begin.\n" +
        "Step J must be finished before step N can begin.\n" +
        "Step T must be finished before step S can begin.\n" +
        "Step T must be finished before step R can begin.\n" +
        "Step K must be finished before step P can begin.\n" +
        "Step N must be finished before step R can begin.\n" +
        "Step G must be finished before step T can begin.\n" +
        "Step I must be finished before step V can begin.\n" +
        "Step G must be finished before step Q can begin.\n" +
        "Step D must be finished before step H can begin.\n" +
        "Step V must be finished before step H can begin.\n" +
        "Step T must be finished before step K can begin.\n" +
        "Step T must be finished before step W can begin.\n" +
        "Step E must be finished before step H can begin.\n" +
        "Step C must be finished before step R can begin.\n" +
        "Step L must be finished before step K can begin.\n" +
        "Step G must be finished before step Y can begin.\n" +
        "Step Y must be finished before step O can begin.\n" +
        "Step O must be finished before step E can begin.\n" +
        "Step U must be finished before step S can begin.\n" +
        "Step X must be finished before step W can begin.\n" +
        "Step C must be finished before step D can begin.\n" +
        "Step E must be finished before step P can begin.\n" +
        "Step B must be finished before step R can begin.\n" +
        "Step F must be finished before step R can begin.\n" +
        "Step A must be finished before step D can begin.\n" +
        "Step G must be finished before step M can begin.\n" +
        "Step B must be finished before step Q can begin.\n" +
        "Step Q must be finished before step V can begin.\n" +
        "Step B must be finished before step W can begin.\n" +
        "Step S must be finished before step H can begin.\n" +
        "Step P must be finished before step X can begin.\n" +
        "Step I must be finished before step M can begin.\n" +
        "Step A must be finished before step S can begin.\n" +
        "Step M must be finished before step X can begin.\n" +
        "Step L must be finished before step S can begin.\n" +
        "Step S must be finished before step W can begin.\n" +
        "Step L must be finished before step V can begin.\n" +
        "Step Z must be finished before step X can begin.\n" +
        "Step M must be finished before step R can begin.\n" +
        "Step T must be finished before step A can begin.\n" +
        "Step N must be finished before step V can begin.\n" +
        "Step M must be finished before step H can begin.\n" +
        "Step E must be finished before step D can begin.\n" +
        "Step F must be finished before step V can begin.\n" +
        "Step B must be finished before step O can begin.\n" +
        "Step G must be finished before step U can begin.\n" +
        "Step J must be finished before step C can begin.\n" +
        "Step G must be finished before step F can begin.\n" +
        "Step Y must be finished before step M can begin.\n" +
        "Step F must be finished before step D can begin.\n" +
        "Step M must be finished before step P can begin.\n" +
        "Step F must be finished before step T can begin.\n" +
        "Step G must be finished before step A can begin.\n" +
        "Step G must be finished before step Z can begin.\n" +
        "Step K must be finished before step V can begin.\n" +
        "Step J must be finished before step Z can begin.\n" +
        "Step O must be finished before step Z can begin.\n" +
        "Step B must be finished before step E can begin.\n" +
        "Step Z must be finished before step V can begin.\n" +
        "Step Q must be finished before step O can begin.\n" +
        "Step J must be finished before step D can begin.\n" +
        "Step Y must be finished before step E can begin.\n" +
        "Step D must be finished before step R can begin.\n" +
        "Step I must be finished before step F can begin.\n" +
        "Step M must be finished before step V can begin.\n" +
        "Step I must be finished before step D can begin.\n" +
        "Step O must be finished before step P can begin."