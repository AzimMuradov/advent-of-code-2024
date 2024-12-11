fun main() {
    fun calculateStoneCount(stones: List<Long>, blinkCount: Int): Long {
        val mem = mutableMapOf<Pair<Long, Int>, Long>()

        val calculateStoneCount = DeepRecursiveFunction<Pair<Long, Int>, Long> { (stone, n) ->
            mem.getOrPut(stone to n) {
                if (n != 0) {
                    val s = stone.toString()
                    val nextStones = when {
                        stone == 0L -> listOf(1L)
                        s.length % 2 == 0 -> s.chunked(size = s.length / 2).map(String::toLong)
                        else -> listOf(stone * 2024)
                    }
                    nextStones.sumOf { stone -> callRecursive(stone to n - 1) }
                } else {
                    1L
                }
            }
        }

        return stones.sumOf { stone ->
            calculateStoneCount(stone to blinkCount)
        }
    }


    fun part1(stones: List<Long>): Long = calculateStoneCount(stones, blinkCount = 25)

    fun part2(stones: List<Long>): Long = calculateStoneCount(stones, blinkCount = 75)


    val stones = readInputText("day-11-input").toLongs()

    part1(stones).println()
    part2(stones).println()
}
