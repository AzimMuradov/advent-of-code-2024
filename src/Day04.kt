private typealias WordSearch = List<String>


fun main() {
    fun WordSearch.dim() = size to first().length

    fun WordSearch.mirrorHor() = map(String::reversed)
    fun WordSearch.mirrorVer() = reversed()

    fun WordSearch.transpose(): WordSearch {
        val (n, m) = dim()
        return List(m) { j ->
            List(n) { i ->
                this[i][j]
            }.joinToString(separator = "")
        }
    }


    fun part1(input: List<String>): Int {
        /*
         * XMAS
         */
        fun countHor(ws: WordSearch): Int = ws.sumOf { line ->
            line.findAll("XMAS".toRegex()).count()
        }

        /*
         * X...
         * .M..
         * ..A.
         * ...S
         */
        fun countDiag(ws: WordSearch): Int {
            val (n, m) = ws.dim()

            var cnt = 0
            for (i in 0..<n - 3) {
                for (j in 0..<m - 3) {
                    if (
                        ws[i + 0][j + 0] == 'X' &&
                        ws[i + 1][j + 1] == 'M' &&
                        ws[i + 2][j + 2] == 'A' &&
                        ws[i + 3][j + 3] == 'S'
                    ) {
                        cnt += 1
                    }
                }
            }
            return cnt
        }

        return listOf(
            countHor(input),
            countHor(input.mirrorHor()),
            countHor(input.transpose()),
            countHor(input.transpose().mirrorHor()),
            countDiag(input),
            countDiag(input.mirrorHor()),
            countDiag(input.mirrorVer()),
            countDiag(input.mirrorHor().mirrorVer()),
        ).sum()
    }


    fun part2(input: List<String>): Int {
        /*
         * M.S
         * .A.
         * M.S
         */
        fun countHor(ws: WordSearch): Int {
            val (n, m) = ws.dim()

            var cnt = 0
            for (i in 0..<n - 2) {
                for (j in 0..<m - 2) {
                    if (
                        ws[i + 0][j + 0] == 'M' &&
                        ws[i + 2][j + 0] == 'M' &&
                        ws[i + 1][j + 1] == 'A' &&
                        ws[i + 0][j + 2] == 'S' &&
                        ws[i + 2][j + 2] == 'S'
                    ) {
                        cnt += 1
                    }
                }
            }
            return cnt
        }

        return listOf(
            countHor(input),
            countHor(input.mirrorHor()),
            countHor(input.transpose()),
            countHor(input.transpose().mirrorHor()),
        ).sum()
    }


    val input = readInputLines("day-04-input")

    part1(input).println()
    part2(input).println()
}
