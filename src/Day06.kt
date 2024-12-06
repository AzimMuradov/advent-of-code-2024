import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


private data class State(
    val x: Int,
    val y: Int,
    val dir: Dir,
)

private enum class Dir { T, R, D, L }


suspend fun main() = coroutineScope {
    fun getInitPos(mapYX: List<String>): Pair<Int, Int> {
        val indices = mapYX.indices

        for (x in indices) {
            for (y in indices) {
                if (mapYX[y][x] == '^') {
                    return Pair(x, y)
                }
            }
        }

        error("init pos missing")
    }

    fun getObstacles(mapYX: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
        val indices = mapYX.indices
        val size = mapYX.size

        val obYX: List<MutableList<Int>> = List(size) { mutableListOf() }
        val obXY: List<MutableList<Int>> = List(size) { mutableListOf() }

        for (x in indices) {
            for (y in indices) {
                if (mapYX[y][x] == '#') {
                    obYX[y] += x
                    obXY[x] += y
                }
            }
        }

        return obYX to obXY
    }

    fun getNextState(
        state: State,
        obYX: List<List<Int>>,
        obXY: List<List<Int>>,
    ): State? {
        val (x, y, dir) = state

        val res = when (dir) {
            Dir.T -> obXY[x].lastOrNull { obY -> obY < y }
            Dir.R -> obYX[y].firstOrNull { obX -> obX > x }
            Dir.D -> obXY[x].firstOrNull { obY -> obY > y }
            Dir.L -> obYX[y].lastOrNull { obX -> obX < x }
        }

        return if (res != null) {
            when (dir) {
                Dir.T -> State(x, res + 1, Dir.R)
                Dir.R -> State(res - 1, y, Dir.D)
                Dir.D -> State(x, res - 1, Dir.L)
                Dir.L -> State(res + 1, y, Dir.T)
            }
        } else {
            null
        }
    }

    fun getPathPositions(mapYX: List<String>): MutableSet<Pair<Int, Int>> {
        val lastIndex = mapYX.lastIndex

        val initPos = getInitPos(mapYX)
        val (obYX, obXY) = getObstacles(mapYX)

        var state = State(initPos.first, initPos.second, Dir.T)

        val pathPositions = mutableSetOf<Pair<Int, Int>>()

        while (true) {
            val (x, y, dir) = state

            val nextState = getNextState(state, obYX, obXY)

            val (from, to) = if (nextState != null) {
                val (nextX, nextY) = nextState
                when (dir) {
                    Dir.T -> (x to nextY) to (x to y)
                    Dir.R -> (x to y) to (nextX to y)
                    Dir.D -> (x to y) to (x to nextY)
                    Dir.L -> (nextX to y) to (x to y)
                }
            } else {
                when (dir) {
                    Dir.T -> (x to 0) to (x to y)
                    Dir.R -> (x to y) to (lastIndex to y)
                    Dir.D -> (x to y) to (x to lastIndex)
                    Dir.L -> (0 to y) to (x to y)
                }
            }

            for (x in from.first..to.first) {
                for (y in from.second..to.second) {
                    pathPositions += x to y
                }
            }

            state = nextState ?: break
        }

        return pathPositions
    }


    fun part1(mapYX: List<String>): Int = getPathPositions(mapYX).count()

    suspend fun part2(mapYX: List<String>): Int {
        val size = mapYX.size

        val initPos = getInitPos(mapYX)
        val placesToPlaceObs = getPathPositions(mapYX) - initPos

        val tasks = placesToPlaceObs.map { (x, y) ->
            async {
                val mapYX = run {
                    val map = List(size) { x ->
                        MutableList(size) { y ->
                            mapYX[x][y]
                        }
                    }
                    map[y][x] = '#'
                    map.map { it.joinToString(separator = "") }
                }
                val (obYX, obXY) = getObstacles(mapYX)

                val log = mutableListOf(
                    State(initPos.first, initPos.second, Dir.T),
                )
                while (true) {
                    val s = getNextState(log.last(), obYX, obXY) ?: break

                    if (s in log) {
                        return@async true
                    } else {
                        log += s
                    }
                }
                return@async false
            }
        }

        return tasks.awaitAll().count { it }
    }


    val input = readInputLines("day-06-input")

    part1(input).println()
    part2(input).println()
}
