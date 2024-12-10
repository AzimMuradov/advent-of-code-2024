private typealias DiskMap = String

private sealed interface DiskEntity {

    val size: Int


    data class File(override val size: Int, val id: Int) : DiskEntity

    data class FreeSpace(override val size: Int) : DiskEntity
}

private sealed interface DiskBlock {

    data class File(val id: Int) : DiskBlock

    data object FreeSpace : DiskBlock
}


fun main() {
    fun DiskMap.entities(): MutableList<DiskEntity> = this
        .map(Char::digitToInt)
        .mapIndexedTo(mutableListOf()) { i, size ->
            if (i % 2 == 0) DiskEntity.File(size, id = i / 2)
            else DiskEntity.FreeSpace(size)
        }

    fun List<DiskEntity>.blocks(): MutableList<DiskBlock> = this
        .flatMapTo(mutableListOf()) { entity ->
            when (entity) {
                is DiskEntity.File -> List(entity.size) { DiskBlock.File(entity.id) }
                is DiskEntity.FreeSpace -> List(entity.size) { DiskBlock.FreeSpace }
            }
        }

    fun List<DiskBlock>.filesystemChecksum(): Long = this
        .mapIndexed { i, block ->
            when (block) {
                is DiskBlock.File -> (i * block.id).toLong()
                DiskBlock.FreeSpace -> 0L
            }
        }.sum()


    fun part1(diskMap: DiskMap): Long {
        val blocks = diskMap
            .entities()
            .blocks()

        var fsI = 0
        var fI = blocks.lastIndex
        while (fsI < fI) {
            fI = blocks.indexOfLastOrNull(fsI, fI + 1) { block ->
                block is DiskBlock.File
            } ?: break

            fsI = blocks.indexOfFirstOrNull(fsI, fI + 1) { block ->
                block is DiskBlock.FreeSpace
            } ?: break

            blocks[fsI] = blocks[fI]
            blocks[fI] = DiskBlock.FreeSpace

            fI--
            fsI++
        }

        return blocks.filesystemChecksum()
    }

    fun part2(diskMap: DiskMap): Long {
        val entities = diskMap.entities()

        var fId = entities.lastIndex / 2
        var fI = entities.lastIndex
        while (fId >= 0 && fI >= 0) {
            var fsI = 0
            fId--

            fI = entities.indexOfLastOrNull(fsI, fI + 1) { entity ->
                entity is DiskEntity.File && entity.id <= fId + 1
            }!!
            val f = entities[fI]

            fsI = entities.indexOfFirstOrNull(fsI, fI + 1) { entity ->
                entity is DiskEntity.FreeSpace && entity.size >= f.size
            } ?: continue
            val fs = entities[fsI]

            if (fs.size == f.size) {
                entities[fsI] = f
                entities[fI] = fs
                fI--
            } else {
                entities[fsI] = f
                entities.add(fsI + 1, DiskEntity.FreeSpace(fs.size - f.size))
                fI++
                entities[fI] = DiskEntity.FreeSpace(f.size)
                fI--
            }
        }

        return entities
            .blocks()
            .filesystemChecksum()
    }


    val diskMap = readInputText("day-09-input")

    part1(diskMap).println()
    part2(diskMap).println()
}
