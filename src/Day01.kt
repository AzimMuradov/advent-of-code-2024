import kotlin.math.abs


fun main() {
    fun readLists(input: List<String>) = input
        .map { line ->
            line.toInts("   ").toPair()
        }
        .unzip()


    fun part1(input: List<String>): Int = readLists(input)
        .let { (xs1, xs2) ->
            xs1.sorted() zip xs2.sorted()
        }
        .sumOf { (x1, x2) ->
            abs(x1 - x2)
        }

    fun part2(input: List<String>): Int = readLists(input)
        .let { (xs1, xs2) ->
            val frequencyMap = xs2.groupingBy { it }.eachCount()
            xs1.sumOf { x -> x * frequencyMap.getOrDefault(x, 0) }
        }


    val input = readInputLines("day-01-input")

    part1(input).println()
    part2(input).println()
}
