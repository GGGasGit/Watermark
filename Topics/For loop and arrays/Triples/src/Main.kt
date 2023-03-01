fun main() {
    val size = readln().toInt()
    val numberList = IntArray(size)
    for (i in 0 until size) {
        numberList[i] = readln().toInt()
    }
    var triplets = 0
    for (i in 0 until size - 2) {
        if (numberList[i] + 1 == numberList[i + 1] && numberList[i + 1] + 1 == numberList[i + 2]) triplets++
    }
    print(triplets)
}