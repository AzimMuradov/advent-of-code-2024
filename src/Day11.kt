fun main() {
    fun calculateStoneCount(stones: List<Long>, blinksCount: Int): Long {
        val mem = mutableMapOf<Pair<Long, Int>, Long>()

        val calculateStoneCount = DeepRecursiveFunction<Pair<Long, Int>, Long> { (stone, n) ->
            val s = stone.toString()

            mem[stone to n] ?: when {
                n == 0 -> 1L

                stone == 0L -> callRecursive(1L to n - 1)

                s.length % 2 == 0 -> s
                    .chunked(s.length / 2)
                    .map(String::toLong)
                    .sumOf { stonePart -> callRecursive(stonePart to n - 1) }

                else -> callRecursive(stone * 2024 to n - 1)
            }.also {
                mem[stone to n] = it
            }
        }

        return stones.sumOf { stone ->
            calculateStoneCount(stone to blinksCount)
        }
    }


    fun part1(stones: List<Long>): Long = calculateStoneCount(stones, blinksCount = 25)

    fun part2(stones: List<Long>): Long = calculateStoneCount(stones, blinksCount = 75)


    val stones = readInputText("day-11-input").toLongs()

    part1(stones).println()
    part2(stones).println()
}
