private typealias WordSearch = List<String>


fun main() {
    fun WordSearch.dim() = size to first().length

    fun WordSearch.mirrorHor() = map(String::reversed)
    fun WordSearch.mirrorVer() = reversed()


    fun part1(input: List<String>): Int {
        /*
         * XMAS
         */
        fun countHor(ws: WordSearch): Int {
            val (n, m) = ws.dim()

            var cnt = 0
            for (i in 0..<n) {
                for (j in 0..<m - 3) {
                    if (
                        ws[i][j + 0] == 'X' &&
                        ws[i][j + 1] == 'M' &&
                        ws[i][j + 2] == 'A' &&
                        ws[i][j + 3] == 'S'
                    ) {
                        cnt += 1
                    }
                }
            }
            return cnt
        }

        /*
         * X
         * M
         * A
         * S
         */
        fun countVer(ws: WordSearch): Int {
            val (n, m) = ws.dim()

            var cnt = 0
            for (i in 0..<n - 3) {
                for (j in 0..<m) {
                    if (
                        ws[i + 0][j] == 'X' &&
                        ws[i + 1][j] == 'M' &&
                        ws[i + 2][j] == 'A' &&
                        ws[i + 3][j] == 'S'
                    ) {
                        cnt += 1
                    }
                }
            }
            return cnt
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
            countVer(input),
            countVer(input.mirrorVer()),
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

        /*
         * M.M
         * .A.
         * S.S
         */
        fun countVer(ws: WordSearch): Int {
            val (n, m) = ws.dim()

            var cnt = 0
            for (i in 0..<n - 2) {
                for (j in 0..<m - 2) {
                    if (
                        ws[i + 0][j + 0] == 'M' &&
                        ws[i + 0][j + 2] == 'M' &&
                        ws[i + 1][j + 1] == 'A' &&
                        ws[i + 2][j + 0] == 'S' &&
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
            countVer(input),
            countVer(input.mirrorVer()),
        ).sum()
    }


    val input = readInputLines("day-04-input")

    part1(input).println()
    part2(input).println()
}
