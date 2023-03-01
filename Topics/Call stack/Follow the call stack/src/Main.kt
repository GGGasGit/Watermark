import kotlin.math.sqrt

fun printIfPrime(number: Int) {
    var flag = false
    for (i in 2..sqrt(number.toDouble()).toInt()) {
        if (number % i == 0) {
            flag = true
            break
        }
    }
    print(if (flag) "$number is not a prime number." else "$number is a prime number.")
}

fun main(args: Array<String>) {
    val number = readln().toInt()
    printIfPrime(number)
}