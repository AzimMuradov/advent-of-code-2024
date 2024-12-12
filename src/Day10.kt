private typealias TopographicMap = List<String>


fun main() {
    fun getNextPositions(map: TopographicMap, positions: Iterable<Pos>, height: Int): Sequence<Pos> {
        val rect = Rect.from(map)

        return positions
            .asSequence()
            .flatMap { pos -> Vec.MOVES.map { move -> pos + move } }
            .filter { pos -> pos in rect }
            .filter { (x, y) -> map[y][x] == (height + 1).digitToChar() }
    }

    fun sumCalculations(map: TopographicMap, calculate: (Pos) -> Int): Int = sequence {
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == '0') {
                    yield(calculate(Pos(x, y)))
                }
            }
        }
    }.sum()


    fun part1(map: TopographicMap): Int {
        fun calculateScoreOf(trailhead: Pos): Int =
            (0..8).fold(setOf(trailhead)) { positions, height ->
                getNextPositions(map, positions, height).toSet()
            }.size

        return sumCalculations(map, ::calculateScoreOf)
    }

    fun part2(map: TopographicMap): Int {
        fun calculateRatingOf(trailhead: Pos): Int {
            val calculateRating = DeepRecursiveFunction<Pair<Pos, Int>, Int> { (pos, height) ->
                if (height < 9) {
                    getNextPositions(map, listOf(pos), height).sumOf { nextPos ->
                        callRecursive(nextPos to height + 1)
                    }
                } else {
                    1
                }
            }
            return calculateRating(trailhead to 0)
        }

        return sumCalculations(map, ::calculateRatingOf)
    }


    val map = readInputLines("day-10-input")

    part1(map).println()
    part2(map).println()
}
