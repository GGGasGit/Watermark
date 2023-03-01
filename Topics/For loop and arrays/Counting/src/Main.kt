fun main() {
    val size = readln().toInt()
    val array = IntArray(size)
    for (i in 0..array.lastIndex) {
        array[i] = readln().toInt()
    }
    val number = readln().toInt()

    var count = 0
    for (elem in array) {
        if (elem == number) count++
    }
    print(count)
}