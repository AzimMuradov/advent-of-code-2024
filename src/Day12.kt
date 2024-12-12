fun main() {
    fun part1(input: List<String>): Int {
        val visited = mutableSetOf<Pos>()

        val rect = Rect(input[0].length, input.size)
        val moves = listOf(Vec(0, -1), Vec(0, 1), Vec(-1, 0), Vec(1, 0))

        fun calculateFencePrice(start: Pos, c: Char): Pair<Int, Int> {
            var totalArea = 0
            var totalPerimeter = 0

            var q = listOf(start)
            while (q.isNotEmpty()) {
                visited += q

                val neighbours = q
                    .flatMap { pos -> moves.map { move -> pos + move } }
                    .filter { it in rect && input[it.y][it.x] == c }

                val area = q.size
                val perimeter = 4 * q.size - neighbours.count()

                totalArea += area
                totalPerimeter += perimeter

                q = neighbours.toSet().filter { it !in visited }
            }

            return totalArea to totalPerimeter
        }

        var sum = 0

        for (x in input.indices) {
            for (y in input.indices) {
                if (Pos(x, y) in visited) continue

                val (area, perimeter) = calculateFencePrice(Pos(x, y), input[y][x])
                sum += area * perimeter
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val visited = mutableSetOf<Pos>()

        val rect = Rect(input[0].length, input.size)
        val moves = listOf(Vec(0, -1), Vec(0, 1), Vec(-1, 0), Vec(1, 0))

        fun calculateFencePrice(start: Pos, c: Char): Pair<Int, Int> {
            var totalArea = 0
            var totalPerimeter = 0

            var q = listOf(start)
            val plants = mutableSetOf<Pos>()
            while (q.isNotEmpty()) {
                plants += q
                totalArea += q.size

                q = q
                    .flatMap { pos -> moves.map { move -> pos + move } }
                    .filter { it in rect && input[it.y][it.x] == c }
                    .filter { it !in plants }
                    .distinct()
            }
            visited += plants

            for (y in input.indices) {
                var x = 0
                while (x < input.size && (Pos(x, y) !in plants || Pos(x, y - 1) in plants)) x++
                while (x < input.size) {
                    while (Pos(x, y) in plants && Pos(x, y - 1) !in plants) x++
                    while (x < input.size && (Pos(x, y) !in plants || Pos(x, y - 1) in plants)) x++
                    totalPerimeter++
                }
            }
            for (y in input.indices) {
                var x = 0
                while (x < input.size && (Pos(x, y) !in plants || Pos(x, y + 1) in plants)) x++
                while (x < input.size) {
                    while (Pos(x, y) in plants && Pos(x, y + 1) !in plants) x++
                    while (x < input.size && (Pos(x, y) !in plants || Pos(x, y + 1) in plants)) x++
                    totalPerimeter++
                }
            }
            for (x in input.indices) {
                var y = 0
                while (y < input.size && (Pos(x, y) !in plants || Pos(x - 1, y) in plants)) y++
                while (y < input.size) {
                    while (Pos(x, y) in plants && Pos(x - 1, y) !in plants) y++
                    while (y < input.size && (Pos(x, y) !in plants || Pos(x - 1, y) in plants)) y++
                    totalPerimeter++
                }
            }
            for (x in input.indices) {
                var y = 0
                while (y < input.size && (Pos(x, y) !in plants || Pos(x + 1, y) in plants)) y++
                while (y < input.size) {
                    while (Pos(x, y) in plants && Pos(x + 1, y) !in plants) y++
                    while (y < input.size && (Pos(x, y) !in plants || Pos(x + 1, y) in plants)) y++
                    totalPerimeter++
                }
            }

            return totalArea to totalPerimeter
        }

        var sum = 0

        for (x in input.indices) {
            for (y in input.indices) {
                if (Pos(x, y) in visited) continue

                val (area, perimeter) = calculateFencePrice(Pos(x, y), input[y][x])
                sum += area * perimeter
            }
        }

        return sum
    }


    val input = readInputLines("day-12-input")

    part1(input).println()
    part2(input).println()
}
