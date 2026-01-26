package Lab1

fun main() {

    // In văn bản
    println("Hello, world!")
    println("This is the text to print!")

    // Comment
    // This is a comment line.
    // This is another comment.

    // Biến
    val age = 5              // không thay đổi
    val name = "Rover"

    var roll = 6             // có thể thay đổi
    var rolledValue: Int = 4

    // In biến với chuỗi
    println("You are already $age!")
    println("You are already $age days old, $name!")

    // Toán tử cơ bản
    val a = 8
    val b = 4
    println("a + b = ${a + b}")
    println("a - b = ${a - b}")
    println("a * b = ${a * b}")
    println("a / b = ${a / b}")

    // Toán tử logic
    println("a > b = ${a > b}")
    println("a < b = ${a < b}")
    println("a == b = ${a == b}")
    println("a != b = ${a != b}")


    // Hàm không đối số
    printHello()

    // Hàm có đối số
    printBorder("=", 10)

    // Hàm trả về giá trị
    val diceResult = rollDice()
    println("Lab1.Dice rolled: $diceResult")

    // if / else
    val num = 4
    if (num > 4) {
        println("The variable is greater than 4")
    } else if (num == 4) {
        println("The variable is equal to 4")
    } else {
        println("The variable is less than 4")
    }

    // when
    when (diceResult) {
        1 -> println("So sorry! You rolled a 1")
        2 -> println("Sadly, you rolled a 2")
        3 -> println("Unfortunately, you rolled a 3")
        4 -> println("No luck! You rolled a 4")
        5 -> println("Don't cry! You rolled a 5")
        else -> println("Apologies! You rolled a 6")
    }

    // repeat
    printSimpleBorder()

    // repeat lồng nhau
    printCakeBottom(age, 3)

    // Class
    val myFirstDice = Dice(6)
    println("Lab1.Dice from class: ${myFirstDice.roll()}")
}

// Các hàm

fun printHello() {
    println("Hello Kotlin")
}

fun printBorder(border: String, timesToRepeat: Int) {
    repeat(timesToRepeat) {
        print(border)
    }
    println()
}

fun rollDice(): Int {
    return (1..6).random()
}

fun printSimpleBorder() {
    repeat(23) {
        print("=")
    }
    println()
}

fun printCakeBottom(age: Int, layers: Int) {
    repeat(layers) {
        repeat(age + 2) {
            print("@")
        }
        println()
    }
}

// Class

class Dice(val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}
