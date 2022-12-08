fun main() {
    val lsCommandRgx = """\$ ls""".toRegex()
    val cdCommandRgx = """\$ cd (.*)""".toRegex()
    val fileRgx = """(\d+) (.*)""".toRegex()
    val dirRgx = """dir (.*)""".toRegex()

    fun prepareInput(input: List<String>): List<Directory> {
        val root = Directory(cdCommandRgx.matchEntire(input.first())!!.groupValues[1], null)
        var currentDir = root
        val directories = mutableListOf(root)
        input.drop(1).forEach { line ->
            cdCommandRgx.matchEntire(line)?.groupValues?.last()?.let { dirName ->
                currentDir = when (dirName) {
                    ".." -> currentDir.parent!!
                    "/" -> root
                    else -> currentDir.children.filterIsInstance<Directory>().find { it.name == dirName }!!
                }
            } ?: fileRgx.matchEntire(line)?.groupValues?.drop(1)?.let { (size, fileName) ->
                if (!currentDir.containsFile(fileName)) {
                    currentDir + File(fileName, size.toInt())
                }
            } ?: dirRgx.matchEntire(line)?.groupValues?.drop(1)?.let { (dirName) ->
                if (!currentDir.containsDirectory(dirName))
                    directories += Directory(dirName, currentDir).also { currentDir.plus(it) }
            }
        }

        return directories.toList()
    }

    fun part1(directories: List<Directory>): Int = directories.mapNotNull { directory ->
        directory.totalSize.takeUnless { it > 100000 }
    }.sum()

    val testInput = prepareInput(readInput("Day07_test"))
    check(part1(testInput) == 95437)

    val input = prepareInput(readInput("Day07"))
    measureAndPrintTimeMillis {
        print("part1: ")
        checkAndPrint(part1(input), 1350966)
    }
}

private open class File(val name: String, val size: Int) {
    open val totalSize: Int get() = size

    override fun equals(other: Any?): Boolean = name == (other as? Directory)?.name && size == other.size

    override fun toString(): String = "File($name, $size)"
}

private class Directory(name: String, val parent: Directory?, val children: MutableList<File> = mutableListOf()): File(name, -1) {
    override val totalSize: Int
        get() = children.sumOf(File::totalSize)

    val directories: List<Directory> = children.filterIsInstance<Directory>()
    val files: List<File> = children.filterNot { it is Directory }

    fun containsDirectory(directoryName: String) = directories.find { it.name == directoryName } != null
    fun containsFile(fileName: String) = files.find { it.name == fileName } != null

    operator fun plus(file: File) = children.add(file)

    operator fun get(name: String) = children.find { it.name == name }

    override fun equals(other: Any?): Boolean = name == (other as? Directory)?.name
    override fun toString(): String = "Dir($name, $totalSize, $children)"
}