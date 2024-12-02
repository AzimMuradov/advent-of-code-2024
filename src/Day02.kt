import kotlin.math.abs


fun main() {
    fun isReportSafe(report: List<Int>): Boolean {
        val diffs = report.zipWithNext { a, b -> b - a }
        return (diffs.all { it > 0 } || diffs.all { it > 0 }) &&
                diffs.all { abs(it) in 1..3 }
    }

    fun part1(input: List<String>): Int = input
        .map(String::toInts)
        .count(::isReportSafe)

    fun part2(input: List<String>): Int = input
        .map(String::toInts)
        .map { report ->
            List(report.size) { i ->
                report.workAsMut { removeAt(i) }
            }
        }
        .count { reportVariants ->
            reportVariants.any(::isReportSafe)
        }

    val input = readInputLines("day-02-input")

    part1(input).println()
    part2(input).println()
}
