package com.example.affirmations.model

import kotlin.math.PI

fun main() {

    // Tạo đối tượng từ lớp con
    val squareCabin = SquareCabin(residents = 4, length = 10.0)
    val roundHut = RoundHut(residents = 2, radius = 5.0)

    // Sử dụng with để truy cập nhanh các thuộc tính và hàm của đối tượng
    with(squareCabin) {
        println("\nSquare Cabin")
        println("Sức chứa: $capacity")
        println("Vật liệu: $buildingMaterial")
        println("Còn chỗ không? ${hasRoom()}")
        println("Diện tích sàn: ${floorArea()}")
    }

    with(roundHut) {
        println("\nRound Hut")
        println("Sức chứa: $capacity")
        println("Vật liệu: $buildingMaterial")
        println("Còn chỗ không? ${hasRoom()}")
        println("Diện tích sàn: ${floorArea()}")
    }

    // Danh sách
    println("\n===== LIST =====")

    // Danh sách chỉ đọc
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    println("Kích thước danh sách: ${numbers.size}")
    println("Phần tử đầu tiên: ${numbers[0]}")
    println("Danh sách đảo ngược: ${listOf("red", "blue", "green").reversed()}")

    // Danh sách có thể thay đổi
    val entrees = mutableListOf<String>()
    entrees.add("spaghetti")
    entrees.add("pizza")
    println("Danh sách món ăn: $entrees")

    // Sửa phần tử
    entrees[0] = "lasagna"
    println("Sau khi sửa: $entrees")

    // Xóa phần tử
    entrees.remove("pizza")
    println("Sau khi xóa: $entrees")

    // Vòng lặp
    println("\n===== VÒNG LẶP FOR =====")
    for (element in numbers) {
        println(element)
    }

    println("\n===== VÒNG LẶP WHILE =====")
    var index = 0
    while (index < numbers.size) {
        println(numbers[index])
        index++
    }

    // Chuỗi
    println("\n===== CHUỖI =====")

    val name = "Android"
    println("Số ký tự: ${name.length}")

    val number = 10
    val groups = 5
    println("$number người")
    println("${number * groups} người")

    // Toán tử gán mở rộng
    println("\n===== TOÁN TỬ =====")

    var a = 20
    var b = 5
    a += b
    println("a += b = $a")

    // Hàm vararg
    println("\n===== VARARG =====")
    addToppings("Phô mai", "Ô liu", "Nấm")
}

// Lớp trừu tượng

abstract class Dwelling(private var residents: Int) {

    // Thuộc tính trừu tượng
    abstract val buildingMaterial: String
    abstract val capacity: Int

    // Hàm kiểm tra còn chỗ hay không
    fun hasRoom(): Boolean {
        return residents < capacity
    }

    // Phương thức trừu tượng
    abstract fun floorArea(): Double
}

// Lớp open (có thể kế thừa)
open class RoundHut(
    residents: Int,
    protected val radius: Double
) : Dwelling(residents) {

    override val buildingMaterial: String = "Rơm"
    override val capacity: Int = 4

    // Ghi đè hàm tính diện tích
    override fun floorArea(): Double {
        return PI * radius * radius
    }
}

// Lớp con kế thừa
class SquareCabin(
    residents: Int,
    private val length: Double
) : Dwelling(residents) {

    override val buildingMaterial: String = "Gỗ"
    override val capacity: Int = 6

    // Ghi đè hàm tính diện tích
    override fun floorArea(): Double {
        return length * length
    }
}

// Hàm có số lượng đối số thay đổi
fun addToppings(vararg toppings: String) {
    println("Các loại topping:")
    for (topping in toppings) {
        println("- $topping")
    }
}
