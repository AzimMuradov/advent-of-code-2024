fun main() {
    fun runMul(mulCmd: String): Int = mulCmd
        .removeSurrounding("mul(", ")")
        .toInts(",")
        .product()

    fun String.findAll(regex: Regex): Sequence<String> = regex
        .findAll(this)
        .map(MatchResult::value)

    fun part1(input: String): Int = input
        .findAll(Regex(MUL_PATTERN))
        .sumOf(::runMul)

    fun part2(input: String): Int {
        val commands = run {
            val regex = Regex("($DO_PATTERN|$DONT_PATTERN|$MUL_PATTERN)")
            input.findAll(regex)
        }

        var sum = 0
        var condition = true
        for (cmd in commands) {
            when (cmd) {
                "do()" -> condition = true
                "don't()" -> condition = false
                else if condition -> sum += runMul(cmd)
            }
        }
        return sum
    }

    val input = readInputText("day-03-input")

    part1(input).println()
    part2(input).println()
}

const val DO_PATTERN = """do\(\)"""
const val DONT_PATTERN = """don't\(\)"""
const val MUL_PATTERN = """mul\([0-9]{1,3},[0-9]{1,3}\)"""
