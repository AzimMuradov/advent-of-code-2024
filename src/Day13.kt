data class ClawMachine(
    val aBtnMove: VecLong,
    val bBtnMove: VecLong,
    val prizePos: PosLong,
)


fun main() {
    fun calcMinNeededTokens(clawMachine: ClawMachine): Long {
        val (aBtnMove, bBtnMove, prizePos) = clawMachine
        val (aX, aY) = aBtnMove
        val (bX, bY) = bBtnMove
        val (pX, pY) = prizePos

        val aNum = pY * bX - pX * bY
        val bNum = pY * aX - pX * aY
        val den = aY * bX - aX * bY

        return if (den != 0L) {
            if (aNum % den == 0L && bNum % den == 0L) {
                3 * (aNum / den) + (bNum / -den)
            } else {
                0
            }
        } else {
            prizePos.x / aBtnMove.x
        }
    }


    fun part1(clawMachines: List<ClawMachine>): Long =
        clawMachines.sumOf(::calcMinNeededTokens)

    fun part2(clawMachines: List<ClawMachine>): Long {
        val prizeMove = run {
            val prizeMove1D = 10_000_000_000_000L
            VecLong(prizeMove1D, prizeMove1D)
        }
        return clawMachines
            .map { it.copy(prizePos = it.prizePos + prizeMove) }
            .sumOf(::calcMinNeededTokens)
    }


    val input = readInputText("day-13-input")

    val clawMachines = input.split("\n\n").map { clawMachineString ->
        val (aBtnMoveString, bBtnMoveString, prizePosString) = clawMachineString.split("\n")

        val aBtnMove = aBtnMoveString.removePrefix("Button A: ").split(", ").map {
            it.drop(2).toLong()
        }.toPair().toVecLong()
        val bBtnMove = bBtnMoveString.removePrefix("Button B: ").split(", ").map {
            it.drop(2).toLong()
        }.toPair().toVecLong()
        val prizePos = prizePosString.removePrefix("Prize: ").split(", ").map {
            it.drop(2).toLong()
        }.toPair().toPosLong()

        ClawMachine(aBtnMove, bBtnMove, prizePos)
    }

    part1(clawMachines).println()
    part2(clawMachines).println()
}
