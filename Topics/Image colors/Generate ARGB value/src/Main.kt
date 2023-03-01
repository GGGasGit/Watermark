import java.awt.Color

fun printARGB() {
    val channels = readln().split(" ").map { it.toInt() }
    var outOfRange = false
    for (channel in channels) {
        if (channel !in 0..255) {
            print("Invalid input")
            outOfRange = true
            break
        }
    }
    if (!outOfRange) {
        val color = channels[0] * 16777216 + channels[1] * 65536 + channels[2] * 256 + channels[3]
        print(color.toUInt())
    }
}