fun main() {
    fun findAntennas(mapYX: List<String>): Map<Char, List<Pos>> =
        buildMap<Char, MutableList<Pos>> {
            mapYX.iterateMap { pos, freq ->
                getOrPut(freq, ::mutableListOf) += pos
            }
            keys.filterNot(Char::isLetterOrDigit).forEach(::remove)
        }


    fun part1(mapYX: List<String>): Int {
        val rect = Rect.from(mapYX)

        val antennas = findAntennas(mapYX)

        val antinodes = buildSet {
            antennas.forEach { freq, positions ->
                positions.iterateOrderedCombinations { a, b ->
                    add(a + 2 * (b - a))
                    add(b + 2 * (a - b))
                }
            }
        }

        return antinodes.count(rect::contains)
    }

    fun part2(mapYX: List<String>): Int {
        val rect = Rect.from(mapYX)

        val antennas = findAntennas(mapYX)

        val antinodes = buildSet {
            antennas.forEach { freq, positions ->
                positions.iterateOrderedCombinations { a, b ->
                    fun Sequence<Int>.addAntinodes() = this
                        .map { n -> a + n * (b - a) }
                        .takeWhile(rect::contains)
                        .forEach(::add)

                    generateSequence(0, Int::dec).addAntinodes()
                    generateSequence(1, Int::inc).addAntinodes()
                }
            }
        }

        return antinodes.size
    }


    val input = readInputLines("day-08-input")

    part1(input).println()
    part2(input).println()
}
