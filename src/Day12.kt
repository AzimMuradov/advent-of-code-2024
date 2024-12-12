fun main() {
    fun discoverPlants(garden: List<String>): List<Set<Pos>> = buildList {
        val rect = Rect.from(garden)

        val visited = mutableSetOf<Pos>()

        fun searchForPlants(start: Pos, plant: Char): Set<Pos> {
            val searchSequence = generateSequence(setOf(start) to setOf(start)) { (searchWave, plantPositions) ->
                if (searchWave.isNotEmpty()) {
                    val nextSearchWave = searchWave
                        .asSequence()
                        .flatMap { pos -> Vec.MOVES.map { move -> pos + move } }
                        .filter { pos -> pos in rect && garden[pos.y][pos.x] == plant }
                        .filter { pos -> pos !in plantPositions }
                        .toSet()
                    nextSearchWave to plantPositions + nextSearchWave
                } else {
                    null
                }
            }
            return searchSequence.last().second
        }

        for (x in garden.indices) {
            for (y in garden.indices) {
                if (Pos(x, y) in visited) continue

                val plantPositions = searchForPlants(
                    start = Pos(x, y),
                    plant = garden[y][x],
                )
                visited += plantPositions

                add(plantPositions)
            }
        }
    }

    fun calculateTotalArea(plantPositions: Set<Pos>): Int = plantPositions.size

    // total top perimeter === total down perimeter
    // total left perimeter === total right perimeter
    fun calculateTotalPerimeter(plantPositions: Set<Pos>): Int = plantPositions.sumOf { pos ->
        val neighbours = Vec.MOVES.map(pos::plus).count(plantPositions::contains)
        4 - neighbours
    }

    // total horizontal sides === total vertical sides
    fun calculateTotalSides(plantPositions: Set<Pos>): Int {
        val minX = plantPositions.minOf(Pos::x)
        val maxX = plantPositions.maxOf(Pos::x)
        val minY = plantPositions.minOf(Pos::y)
        val maxY = plantPositions.maxOf(Pos::y)

        var totalSides = 0

        fun correctCondition(pos: Pos, move: Vec): Boolean =
            pos in plantPositions && pos + move !in plantPositions

        fun skipCondition(pos: Pos, move: Vec): Boolean =
            pos.x <= maxX && !correctCondition(pos, move)

        for (move in listOf(Vec.MOVE_UP, Vec.MOVE_DOWN)) {
            for (y in minY..maxY) {
                var x = minX
                while (skipCondition(Pos(x, y), move)) x++
                while (x <= maxX) {
                    while (correctCondition(Pos(x, y), move)) x++
                    while (skipCondition(Pos(x, y), move)) x++
                    totalSides++
                }
            }
        }

        return totalSides * 2
    }


    fun part1(garden: List<String>): Int = discoverPlants(garden).sumOf { plantPositions ->
        calculateTotalArea(plantPositions) * calculateTotalPerimeter(plantPositions)
    }

    fun part2(garden: List<String>): Int = discoverPlants(garden).sumOf { plantPositions ->
        calculateTotalArea(plantPositions) * calculateTotalSides(plantPositions)
    }


    val input = readInputLines("day-12-input")

    part1(input).println()
    part2(input).println()
}
