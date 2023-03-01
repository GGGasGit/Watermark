fun main() {
    val size = readln().toInt()
    val array = IntArray(size)
    for (i in 0..array.lastIndex) {
        array[i] = readln().toInt()
    }
    val (p, m) = readln().split(" ").map { it.toInt() }

    println(
        if (p in array && m in array) {
            "YES"
        } else {
            "NO"
        }
    )
}