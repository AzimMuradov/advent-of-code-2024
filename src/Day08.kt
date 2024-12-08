fun main() {
    fun findAntennas(mapYX: List<String>): Map<Char, List<Pos>> =
        buildMap<Char, MutableList<Pos>> {
            mapYX.forEachIndexed { y, line ->
                line.forEachIndexed { x, freq ->
                    getOrPut(freq, ::mutableListOf) += Pos(x, y)
                }
            }
            keys.filterNot(Char::isLetterOrDigit).forEach(::remove)
        }


    fun part1(mapYX: List<String>): Int {
        val rect = Rect(mapYX.size, mapYX.size)

        val antennas = findAntennas(mapYX)

        val antinodes = buildSet {
            antennas.forEach { freq, positions ->
                for ((i, a) in positions.withIndex()) {
                    for (b in positions.slice(i + 1..positions.lastIndex)) {
                        add(a + 2 * (b - a))
                        add(b + 2 * (a - b))
                    }
                }
            }
        }

        return antinodes.count(rect::contains)
    }

    fun part2(mapYX: List<String>): Int {
        val rect = Rect(mapYX.size, mapYX.size)

        val antennas = findAntennas(mapYX)

        val antinodes = buildSet {
            antennas.forEach { freq, positions ->
                for ((i, a) in positions.withIndex()) {
                    for (b in positions.slice(i + 1..positions.lastIndex)) {
                        generateSequence(1, Int::inc)
                            .map { n -> a + n * (b - a) }
                            .takeWhile(rect::contains)
                            .forEach(::add)

                        generateSequence(1, Int::inc)
                            .map { n -> b + n * (a - b) }
                            .takeWhile(rect::contains)
                            .forEach(::add)
                    }
                }
            }
        }

        return antinodes.size
    }


    val input = readInputLines("day-08-input")

    part1(input).println()
    part2(input).println()
}
