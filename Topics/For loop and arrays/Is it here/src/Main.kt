fun main() {
    val size = readln().toInt()
    val array = IntArray(size)
    for (i in 0..array.lastIndex) {
        array[i] = readln().toInt()
    }
    val number = readln().toInt()

    print(
        if (number in array) {
            "YES"
        } else {
            "NO"
        }
    )
}