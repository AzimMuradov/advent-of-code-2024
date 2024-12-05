import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText


/**
 * Reads text from the given input txt file.
 */
fun readInputText(name: String) = Path("src/$name.txt").readText().trim()

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(name: String) = readInputText(name).lines()

/**
 * Split string to ints using provided [separator].
 */
fun String.toInts(separator: String = " ") = this
    .split(separator)
    .map(String::toInt)

/**
 * Split string to longs using provided [separator].
 */
fun String.toLongs(separator: String = " ") = this
    .split(separator)
    .map(String::toLong)

/**
 * Converts list to pair.
 */
fun <T> List<T>.toPair() = Pair(this[0], this[1])

/**
 * Converts list to triple.
 */
fun <T> List<T>.toTriple() = Triple(this[0], this[1], this[2])

/**
 * Converts list to triple.
 */
fun <T> List<T>.workAsMut(block: MutableList<T>.() -> Unit): List<T> =
    toMutableList().apply(block)

/**
 * Returns the product of all elements in the collection.
 */
fun Iterable<Int>.product(): Int = fold(1, Int::times)

/**
 * Returns the product of all elements in the collection.
 */
fun Iterable<Long>.product(): Long = fold(1, Long::times)

/**
 * Returns the product of all elements in the sequence.
 *
 * The operation is _terminal_.
 */
fun Sequence<Int>.product(): Int = fold(1, Int::times)

/**
 * Returns the product of all elements in the sequence.
 *
 * The operation is _terminal_.
 */
fun Sequence<Long>.product(): Long = fold(1, Long::times)

/**
 * Returns a sequence of all occurrences of a [regular expression][regex] within the string.
 */
fun String.findAll(regex: Regex): Sequence<MatchResult> = regex.findAll(this)

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
