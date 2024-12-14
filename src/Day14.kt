private data class Robot(val pos: Pos, val vel: Vec)


fun main() {
    fun Sequence<Robot>.skip(sec: Int, map: Rect): Sequence<Pos> = this
        .map { (initPos, vel) -> initPos + sec * vel }
        .map { (x, y) -> Pos(x % map.w, y % map.h) }


    fun part1(robots: List<Robot>, map: Rect): Long = robots
        .asSequence()
        .skip(sec = 100, map)
        .filterNot { (x, y) -> x == map.w / 2 || y == map.h / 2 }
        .groupingBy { (x, y) -> x / (map.w / 2 + 1) to y / (map.h / 2 + 1) }
        .eachCount()
        .values
        .map(Int::toLong)
        .product()

    fun part2(robots: List<Robot>, map: Rect): Int {
        /*
         * We are looking for this (at least for me):
         *
         * *******************************
         * *.............................*
         * *.............................*
         * *.............................*
         * *.............................*
         * *..............*..............*
         * *.............***.............*
         * *............*****............*
         * *...........*******...........*
         * *..........*********..........*
         * *............*****............*
         * *...........*******...........*
         * *..........*********..........*
         * *.........***********.........*
         * *........*************........*
         * *..........*********..........*
         * *.........***********.........*
         * *........*************........*
         * *.......***************.......*
         * *......*****************......*
         * *........*************........*
         * *.......***************.......*
         * *......*****************......*
         * *.....*******************.....*
         * *....*********************....*
         * *.............***.............*
         * *.............***.............*
         * *.............***.............*
         * *.............................*
         * *.............................*
         * *.............................*
         * *.............................*
         * *******************************
         */

        // It's a hack to find the Easter Egg border
        val pattern = Regex("""\*{31}""")

        val sec = (0..<map.w * map.h)
            .asSequence()
            .map { sec -> robots.asSequence().skip(sec, map).toSet() }
            .indexOfFirst { robots ->
                val image = buildString {
                    for (y in 0..<map.h) {
                        for (x in 0..<map.w) {
                            append(if (Pos(x, y) in robots) '*' else '.')
                        }
                        appendLine()
                    }
                }
                val hasEasterEgg = image.findAll(pattern).count() >= 2

                if (hasEasterEgg) println(image)

                return@indexOfFirst hasEasterEgg
            }

        return sec
    }


    val map = Rect(w = 101, h = 103)

    val robots = readInputLines("day-14-input")
        .map { line ->
            val (p, v) = line.split(" ").map { it.drop(2) }.map { it.toInts(",").toPair() }
            Robot(pos = p.toPos(), vel = v.toVec())
        }
        .map { robot ->
            robot.copy(
                vel = Vec(
                    x = (robot.vel.x + map.w) % map.w,
                    y = (robot.vel.y + map.h) % map.h,
                ),
            )
        }

    part1(robots, map).println()
    part2(robots, map).println()
}
