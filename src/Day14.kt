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

        // It's hack to find the Easter Egg border
        val pattern = """\*""".repeat(31).toRegex()

        val sec = (0..<map.w * map.h)
            .asSequence()
            .map { sec -> robots.asSequence().skip(sec, map).toList() }
            .indexOfFirst { rs ->
                val image = buildString {
                    for (y in 0..<map.h) {
                        for (x in 0..<map.w) {
                            append(if (Pos(x, y) in rs) '*' else '.')
                        }
                        appendLine()
                    }
                }
                val isEasterEgg = image.findAll(pattern).count() == 2

                if (isEasterEgg) println(image)

                return@indexOfFirst isEasterEgg
            }

        return sec
    }


    val map = Rect(101, 103)

    val robots = readInputLines("day-14-input")
        .map { line ->
            val (p, v) = line.split(" ").map { it.drop(2) }.map { it.toInts(",").toPair() }
            Robot(p.toPos(), v.toVec())
        }
        .map { robot ->
            robot.copy(vel = Vec((robot.vel.x + map.w) % map.w, (robot.vel.y + map.h) % map.h))
        }

    part1(robots, map).println()
    part2(robots, map).println()
}
