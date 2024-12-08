import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


private data class CalibrationEquation(
    val testValue: Long,
    val operands: List<Long>,
)


suspend fun main() = coroutineScope {
    suspend fun calculateTotalCalibrationResult(
        calibrationEquations: List<CalibrationEquation>,
        operators: List<(Long, Long) -> Long>,
    ): Long = calibrationEquations
        .map { equation ->
            async { equation to equation.canBeProducedWith(operators) }
        }
        .awaitAll()
        .filter { (_, cond) -> cond }
        .sumOf { (eq, _) -> eq.testValue }


    suspend fun part1(calibrationEquations: List<CalibrationEquation>): Long = calculateTotalCalibrationResult(
        calibrationEquations,
        operators = listOf(
            Long::plus,
            Long::times,
        ),
    )

    suspend fun part2(calibrationEquations: List<CalibrationEquation>): Long = calculateTotalCalibrationResult(
        calibrationEquations,
        operators = listOf(
            Long::plus,
            Long::times,
            { a, b -> "$a$b".toLong() },
        ),
    )


    val calibrationEquations = readInputLines("day-07-input").map {
        val (testValueString, operandsString) = it.split(": ")
        CalibrationEquation(testValueString.toLong(), operandsString.toLongs())
    }

    part1(calibrationEquations).println()
    part2(calibrationEquations).println()
}


private fun CalibrationEquation.canBeProducedWith(operators: List<(Long, Long) -> Long>): Boolean {
    val canBeProduced = DeepRecursiveFunction<Pair<Long, Int>, Boolean> { (value, operandsIndex) ->
        when {
            value > testValue -> false
            operandsIndex == operands.size -> value == testValue
            else -> {
                val nextOperand = operands[operandsIndex]
                operators.fold(false) { isCorrect, operator ->
                    isCorrect || callRecursive(operator(value, nextOperand) to operandsIndex + 1)
                }
            }
        }
    }
    return canBeProduced(operands.first() to 1)
}
