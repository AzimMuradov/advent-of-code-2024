import kotlin.math.abs


fun main() {
    fun isReportSafe(report: List<Int>): Boolean {
        val diffs = report.zipWithNext { a, b -> b - a }
        val isInc = (diffs.firstOrNull() ?: 0) > 0
        val isSafe = diffs.all { diff ->
            isInc == diff > 0 && abs(diff) in 1..3
        }
        return isSafe
    }

    fun part1(input: List<String>): Int = input
        .map { line -> line.toInts() }
        .count(::isReportSafe)

    fun part2(input: List<String>): Int = input
        .map { line -> line.toInts() }
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
