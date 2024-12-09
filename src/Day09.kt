private typealias DiskMap = String

private sealed interface DiskElement {

    val size: Int


    data class File(override val size: Int, val id: Int) : DiskElement

    data class FreeSpace(override val size: Int) : DiskElement
}

private sealed interface DiskBlock {

    data class File(val id: Int) : DiskBlock

    data object FreeSpace : DiskBlock
}


fun main() {
    fun DiskMap.elements(): MutableList<DiskElement> = this
        .map(Char::digitToInt)
        .mapIndexedTo(mutableListOf()) { i, size ->
            if (i % 2 == 0) DiskElement.File(size, i / 2)
            else DiskElement.FreeSpace(size)
        }

    fun List<DiskElement>.blocks(): MutableList<DiskBlock> = this
        .flatMapTo(mutableListOf()) { element ->
            when (element) {
                is DiskElement.File -> List(element.size) { DiskBlock.File(element.id) }
                is DiskElement.FreeSpace -> List(element.size) { DiskBlock.FreeSpace }
            }
        }

    fun List<DiskBlock>.filesystemChecksum(): Long = this
        .mapIndexed { i, block ->
            when (block) {
                is DiskBlock.File -> (i * block.id).toLong()
                DiskBlock.FreeSpace -> 0
            }
        }.sum()


    fun part1(diskMap: DiskMap): Long {
        val diskBlocks = diskMap
            .elements()
            .blocks()

        var fI = diskBlocks.lastIndex
        var fsI = 0
        while (fsI < fI) {
            while (fsI < fI && diskBlocks[fI] !is DiskBlock.File) fI--
            if (fsI >= fI) break

            while (fsI < fI && diskBlocks[fsI] !is DiskBlock.FreeSpace) fsI++
            if (fsI >= fI) break

            diskBlocks[fsI] = diskBlocks[fI]
            diskBlocks[fI] = DiskBlock.FreeSpace

            fI--
            fsI++
        }

        return diskBlocks.filesystemChecksum()
    }

    fun part2(diskMap: DiskMap): Long {
        val diskElements = diskMap.elements()

        var fId = diskElements.lastIndex / 2
        var fI = diskElements.lastIndex
        while (fId >= 0 && fI >= 0) {
            fId--

            var fsI = 0

            while (fI >= 0 && diskElements[fI] !is DiskElement.File) fI--
            if (fI < 0) break
            val f = diskElements[fI]

            while (
                fsI < fI &&
                !(diskElements[fsI] is DiskElement.FreeSpace && diskElements[fsI].size >= f.size)
            ) fsI++
            if (fsI >= fI) {
                fI--
                continue
            }
            val fs = diskElements[fsI]

            if (fs.size == f.size) {
                diskElements[fsI] = f
                diskElements[fI] = fs
                fI--
            } else {
                diskElements[fsI] = f
                diskElements.add(fsI + 1, DiskElement.FreeSpace(fs.size - f.size))
                fI++
                diskElements[fI] = DiskElement.FreeSpace(f.size)
                fI--
            }
        }

        return diskElements
            .blocks()
            .filesystemChecksum()
    }


    val diskMap = readInputText("day-09-input")

    part1(diskMap).println()
    part2(diskMap).println()
}
