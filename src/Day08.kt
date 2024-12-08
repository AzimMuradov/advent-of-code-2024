fun main() {
    fun findAntennas(mapYX: List<String>) =
        buildMap<Char, MutableList<Pair<Int, Int>>> {
            mapYX.forEachIndexed { y, line ->
                line.forEachIndexed { x, freq ->
                    getOrPut(freq, ::mutableListOf).add(Pair(x, y))
                }
            }
            keys.filterNot(Char::isLetterOrDigit).forEach(::remove)
        }


    fun part1(mapYX: List<String>): Int {
        val antennas = findAntennas(mapYX)

        val antinodes = mutableSetOf<Pair<Int, Int>>()

        antennas.forEach { freq, positions ->
            for ((i, a) in positions.withIndex()) {
                for (b in positions.slice(i + 1..positions.lastIndex)) {
                    val (aX, aY) = a
                    val (bX, bY) = b

                    antinodes += Pair(
                        aX + 2 * (bX - aX),
                        aY + 2 * (bY - aY),
                    )
                    antinodes += Pair(
                        bX + 2 * (aX - bX),
                        bY + 2 * (aY - bY),
                    )
                }
            }
        }

        return antinodes.count { (x, y) ->
            x in mapYX.indices && y in mapYX.indices
        }
    }

    fun part2(mapYX: List<String>): Int {
        val antennas = findAntennas(mapYX)

        val antinodes = mutableSetOf<Pair<Int, Int>>()

        antennas.forEach { freq, positions ->
            for ((i, a) in positions.withIndex()) {
                for (b in positions.slice(i + 1..positions.lastIndex)) {
                    val (aX, aY) = a
                    val (bX, bY) = b

                    run {
                        var cnt = 1
                        while ((aX + cnt * (bX - aX)) in mapYX.indices && (aY + cnt * (bY - aY)) in mapYX.indices) {
                            antinodes += Pair(
                                aX + cnt * (bX - aX),
                                aY + cnt * (bY - aY),
                            )
                            cnt++
                        }
                    }
                    run {
                        var cnt = 1
                        while ((bX + cnt * (aX - bX)) in mapYX.indices && (bY + cnt * (aY - bY)) in mapYX.indices) {
                            antinodes += Pair(
                                bX + cnt * (aX - bX),
                                bY + cnt * (aY - bY),
                            )
                            cnt++
                        }
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
