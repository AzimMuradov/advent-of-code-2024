private typealias Rule = Pair<Int, Int>
private typealias Update = List<Int>


fun main() {
    fun Update.isCorrect(rules: Set<Rule>): Boolean {
        iterateOrderedCombinations { a, b ->
            if (Rule(b, a) in rules) return false
        }
        return true
    }

    fun List<Update>.sumMiddlePageNumbers() = sumOf { update ->
        update[(update.size - 1) / 2]
    }


    fun part1(rules: Set<Rule>, updates: List<Update>): Int =
        updates
            .filter { update -> update.isCorrect(rules) }
            .sumMiddlePageNumbers()

    fun part2(rules: Set<Rule>, updates: List<Update>): Int =
        updates
            .filterNot { update -> update.isCorrect(rules) }
            .map { update ->
                update.sortedWith { a, b ->
                    if (Rule(a, b) in rules) -1 else 1
                }
            }
            .sumMiddlePageNumbers()


    val input = readInputText("day-05-input")

    val (rulesText, updatesText) = input.split("\n\n")

    val rules: Set<Rule> = rulesText.lines().mapTo(mutableSetOf()) { line ->
        line.toInts("|").toPair()
    }
    val updates = updatesText.lines().map { line ->
        line.toInts(",")
    }

    part1(rules, updates).println()
    part2(rules, updates).println()
}
