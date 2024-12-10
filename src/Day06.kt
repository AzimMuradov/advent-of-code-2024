import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


private data class State(
    val x: Int,
    val y: Int,
    val dir: Dir,
) {

    val pos = Pos(x, y)

    operator fun component4() = pos
}

private enum class Dir { U, R, D, L }


suspend fun main() = coroutineScope {
    fun getInitPos(mapYX: List<String>): Pos {
        val indices = mapYX.indices

        for (x in indices) {
            for (y in indices) {
                if (mapYX[y][x] == '^') {
                    return Pos(x, y)
                }
            }
        }

        error("init pos missing")
    }

    fun getObstaclePositions(mapYX: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
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
            Dir.U -> obXY[x].lastOrNull { obY -> obY < y }
            Dir.R -> obYX[y].firstOrNull { obX -> obX > x }
            Dir.D -> obXY[x].firstOrNull { obY -> obY > y }
            Dir.L -> obYX[y].lastOrNull { obX -> obX < x }
        }

        return if (res != null) {
            when (dir) {
                Dir.U -> State(x, res + 1, Dir.R)
                Dir.R -> State(res - 1, y, Dir.D)
                Dir.D -> State(x, res - 1, Dir.L)
                Dir.L -> State(res + 1, y, Dir.U)
            }
        } else {
            null
        }
    }

    fun getPathPositions(mapYX: List<String>): Set<Pos> {
        val lastIndex = mapYX.lastIndex

        val (initX, initY) = getInitPos(mapYX)
        val (obYX, obXY) = getObstaclePositions(mapYX)

        var state = State(initX, initY, Dir.U)

        val pathPositions = mutableSetOf<Pos>()

        while (true) {
            val (x, y, dir, pos) = state

            val nextState = getNextState(state, obYX, obXY)

            val nextPos = nextState?.pos ?: when (dir) {
                Dir.U -> Pos(x, 0)
                Dir.R -> Pos(lastIndex, y)
                Dir.D -> Pos(x, lastIndex)
                Dir.L -> Pos(0, y)
            }
            pathPositions += positionsList(pos, nextPos)

            state = nextState ?: break
        }

        return pathPositions
    }


    fun part1(mapYX: List<String>): Int = getPathPositions(mapYX).size

    suspend fun part2(mapYX: List<String>): Int {
        val initPos = getInitPos(mapYX)
        val positionsToPlaceObs = getPathPositions(mapYX) - initPos

        val tasks = positionsToPlaceObs.map { (x, y) ->
            async {
                val mapYX = mapYX.mapIndexed { i, line ->
                    if (i == y) {
                        buildString {
                            append(line)
                            set(x, '#')
                        }
                    } else {
                        line
                    }
                }
                val (obYX, obXY) = getObstaclePositions(mapYX)

                val log = mutableListOf(
                    State(initPos.x, initPos.y, Dir.U),
                )
                while (true) {
                    val nextState = getNextState(log.last(), obYX, obXY) ?: break

                    if (nextState in log) {
                        return@async true
                    } else {
                        log += nextState
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
