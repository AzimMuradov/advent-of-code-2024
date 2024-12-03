fun main() {
    fun runMul(cmd: String): Int {
        val (a, b) = cmd.removeSurrounding("mul(", ")").toInts(",")
        return a * b
    }

    val mulPattern = """mul\([0-9]{1,3},[0-9]{1,3}\)"""
    val doPattern = """do\(\)"""
    val dontPattern = """don't\(\)"""

    fun String.findAll(regex: Regex): Sequence<String> = regex
        .findAll(this)
        .map(MatchResult::value)

    fun part1(input: String): Int = input.findAll(Regex(mulPattern)).sumOf(::runMul)

    fun part2(input: String): Int {
        val commands = input.findAll(Regex("($doPattern|$dontPattern|$mulPattern)"))

        var sum = 0
        var condition = true
        for (cmd in commands) {
            when (cmd) {
                "do()" -> condition = true
                "don't()" -> condition = false
                else -> if (condition) {
                    sum += runMul(cmd)
                }
            }
        }
        return sum
    }

    val input = readInputText("day-03-input")

    part1(input).println()
    part2(input).println()
}
