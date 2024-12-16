// NOTE : This needs to be refactored, but I'm too tired...


fun main() {
    val robot = '@'
    val box = 'O'
    val wideBoxLeft = '['
    val wideBoxRight = ']'
    val wall = '#'
    val empty = '.'

    fun findRobot(map: List<List<Char>>): Pos {
        map.iterateMap { pos, c ->
            if (c == robot) return pos
        }
        error("not found")
    }

    fun sumBoxesGpsCoordinates(map: List<List<Char>>, box: Char): Long {
        var sum = 0L
        map.iterateMap { (x, y), c ->
            if (c == box) sum += 100 * y + x
        }
        return sum
    }


    fun part1(map: List<MutableList<Char>>, moves: String): Long {
        var pos = findRobot(map)

        for (move in moves) {
            when (move) {
                '^' -> {
                    val ys = (0..pos.y).toList().takeLastWhile { y -> map[y][pos.x] != wall }
                    if (ys.isNotEmpty()) {
                        val firstEmpty = ys.lastOrNull { y -> map[y][pos.x] == empty }
                        if (firstEmpty != null) {
                            for (y in firstEmpty + 1..pos.y) {
                                map[y - 1][pos.x] = map[y][pos.x]
                            }
                            map[pos] = empty
                            pos += Vec.MOVE_UP
                        }
                    }
                }

                '>' -> {
                    val xs = (pos.x..map.lastIndex).toList().takeWhile { x -> map[pos.y][x] != wall }
                    if (xs.isNotEmpty()) {
                        val firstEmpty = xs.firstOrNull { x -> map[pos.y][x] == empty }
                        if (firstEmpty != null) {
                            for (x in firstEmpty - 1 downTo pos.x) {
                                map[pos.y][x + 1] = map[pos.y][x]
                            }
                            map[pos] = empty
                            pos += Vec.MOVE_RIGHT
                        }
                    }
                }

                'v' -> {
                    val ys = (pos.y..map.lastIndex).toList().takeWhile { y -> map[y][pos.x] != wall }
                    if (ys.isNotEmpty()) {
                        val firstEmpty = ys.firstOrNull { y -> map[y][pos.x] == empty }
                        if (firstEmpty != null) {
                            for (y in firstEmpty - 1 downTo pos.y) {
                                map[y + 1][pos.x] = map[y][pos.x]
                            }
                            map[pos] = empty
                            pos += Vec.MOVE_DOWN
                        }
                    }
                }

                '<' -> {
                    val xs = (0..pos.x).toList().takeLastWhile { x -> map[pos.y][x] != wall }
                    if (xs.isNotEmpty()) {
                        val firstEmpty = xs.lastOrNull { x -> map[pos.y][x] == empty }
                        if (firstEmpty != null) {
                            for (x in firstEmpty + 1..pos.x) {
                                map[pos.y][x - 1] = map[pos.y][x]
                            }
                            map[pos] = empty
                            pos += Vec.MOVE_LEFT
                        }
                    }
                }
            }
        }

        return sumBoxesGpsCoordinates(map, box)
    }

    fun part2(map: List<MutableList<Char>>, moves: String): Long {
        val map = map.map {
            it.flatMapTo(mutableListOf()) {
                when (it) {
                    box -> listOf(wideBoxLeft, wideBoxRight)
                    robot -> listOf(robot, empty)
                    wall -> listOf(wall, wall)
                    empty -> listOf(empty, empty)
                    else -> error("impossible")
                }
            }
        }

        var pos = findRobot(map)

        for (move in moves) {
            when (move) {
                '^' -> run {
                    val coord = mutableMapOf<Int, MutableSet<Int>>().apply {
                        for (y in 0..pos.y) {
                            put(y, mutableSetOf())
                        }

                        this[pos.y]!! += pos.x

                        for (y in pos.y downTo 0) {
                            val xs = get(y)
                            if (xs.isNullOrEmpty()) return@apply
                            for (x in xs) {
                                when (map[y - 1][x]) {
                                    wideBoxLeft -> this[y - 1]!! += listOf(x, x + 1)
                                    wideBoxRight -> this[y - 1]!! += listOf(x - 1, x)
                                    wall -> return@run
                                    empty -> continue
                                }
                            }
                        }
                        for (y in 0..pos.y) {
                            if (this[y].isNullOrEmpty()) remove(y)
                        }
                    }

                    for (y in coord.keys.sorted()) {
                        for (x in coord[y]!!) {
                            map[y - 1][x] = map[y][x]
                            map[y][x] = empty
                        }
                    }
                    pos += Vec.MOVE_UP
                }

                '>' -> {
                    val xs = (pos.x..map[0].lastIndex).toList().takeWhile { x -> map[pos.y][x] != wall }
                    if (xs.isNotEmpty()) {
                        val firstEmpty = xs.firstOrNull { x -> map[pos.y][x] == empty }
                        if (firstEmpty != null) {
                            for (x in firstEmpty - 1 downTo pos.x) {
                                map[pos.y][x + 1] = map[pos.y][x]
                            }
                            map[pos] = empty
                            pos += Vec.MOVE_RIGHT
                        }
                    }
                }

                'v' -> run {
                    val coord = mutableMapOf<Int, MutableSet<Int>>().apply {
                        for (y in pos.y..map.lastIndex) {
                            put(y, mutableSetOf())
                        }

                        this[pos.y]!! += pos.x

                        for (y in pos.y..map.lastIndex) {
                            val xs = get(y)
                            if (xs.isNullOrEmpty()) return@apply
                            for (x in xs) {
                                when (map[y + 1][x]) {
                                    wideBoxLeft -> this[y + 1]!! += listOf(x, x + 1)
                                    wideBoxRight -> this[y + 1]!! += listOf(x - 1, x)
                                    wall -> return@run
                                    empty -> continue
                                }
                            }
                        }
                        for (y in pos.y..map.lastIndex) {
                            if (this[y].isNullOrEmpty()) remove(y)
                        }
                    }

                    for (y in coord.keys.sortedDescending()) {
                        for (x in coord[y]!!) {
                            map[y + 1][x] = map[y][x]
                            map[y][x] = empty
                        }
                    }
                    pos += Vec.MOVE_DOWN
                }

                '<' -> {
                    val xs = (0..pos.x).toList().takeLastWhile { x -> map[pos.y][x] != wall }
                    if (xs.isNotEmpty()) {
                        val firstEmpty = xs.lastOrNull { x -> map[pos.y][x] == empty }
                        if (firstEmpty != null) {
                            for (x in firstEmpty + 1..pos.x) {
                                map[pos.y][x - 1] = map[pos.y][x]
                            }
                            map[pos] = empty
                            pos += Vec.MOVE_LEFT
                        }
                    }
                }
            }
        }

        return sumBoxesGpsCoordinates(map, wideBoxLeft)
    }


    val (map, moves) = readInputText("day-15-input").let {
        val (mapString, movesString) = it.split("\n\n")
        val map = mapString.lines().map(String::toList)
        val moves = movesString.lines().joinToString(separator = "")
        map to moves
    }

    part1(map.map { it.toMutableList() }, moves).println()
    part2(map.map { it.toMutableList() }, moves).println()
}
