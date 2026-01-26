package com.example.affirmations.model

fun main() {

    // Set (nhóm)
    println("\n===== SET =====")

    // Tạo set từ danh sách
    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    val setOfNumbers = numbers.toSet()
    println("Set từ list: $setOfNumbers")

    // Khai báo set
    val set1 = setOf(1, 2, 3)
    val set2 = mutableSetOf(3, 4, 5)

    // Phép toán trên set
    println("Giao của set: ${set1.intersect(set2)}")
    println("Hợp của set: ${set1.union(set2)}")

    // Map (sơ đồ)
    println("\n===== MAP =====")

    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23
    )

    // Thêm / cập nhật giá trị
    peopleAges["Fred"] = 31
    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51

    // Lặp map
    peopleAges.forEach {
        print("${it.key} is ${it.value}, ")
    }
    println()

    // Chuyển đổi từng phần tử
    println(
        peopleAges
            .map { "${it.key} is ${it.value}" }
            .joinToString(", ")
    )

    // Lọc map
    val filteredNames = peopleAges.filter { it.key.length < 4 }
    println("Tên ngắn hơn 4 ký tự: $filteredNames")

    // Các phép toán khác
    println("\n===== FILTER - SHUFFLE - TAKE - SORT =====")

    val words = listOf("about", "acute", "balloon", "best", "brief", "class")
    val filteredWords = words
        .filter { it.startsWith("b", ignoreCase = true) }
        .shuffled()
        .take(2)
        .sorted()

    println("Kết quả: $filteredWords")

    // Hàm phạm vi (Scope function)
    println("\n===== SCOPE FUNCTION =====")

    val argument: String? = "ABC123"
    argument?.let {
        println("Giá trị trong let: $it")
    }

    // apply
    val person = Person().apply {
        name = "Sơn"
        age = 19
    }
    println("Person: ${person.name}, ${person.age}")

    // Thuộc tính dự phòng
    println("\n===== THUỘC TÍNH DỰ PHÒNG =====")

    val example = BackupPropertyExample()
    println("Giá trị hiện tại: ${example.currentScrambledWord}")

    // Lệnh gọi an toàn
    println("\n===== SAFE CALL =====")

    val letterId: String? = null
    println("Kết quả safe call: ${letterId?.length}")

    // Lambda
    println("\n===== LAMBDA =====")

    val triple: (Int) -> Int = { a: Int -> a * 3 }
    println("Triple của 5: ${triple(5)}")

    // Companion Object
    println("\n===== COMPANION OBJECT =====")

    println("Hằng số LETTER: ${DetailActivity.LETTER}")


    // Property Delegation
    println("\n===== PROPERTY DELEGATION =====")

    val viewModel: GameViewModel by lazy {
        GameViewModel()
    }
    println("ViewModel đã khởi tạo: $viewModel")

    // Toán tử Elvis
    println("\n===== ELVIS OPERATOR =====")

    var quantity: Int? = null
    println(quantity ?: 0)

    quantity = 4
    println(quantity ?: 0)

    // Lateinit
    println("\n===== LATEINIT =====")

    val lateInitExample = LateInitExample()
    lateInitExample.initWord("Kotlin")
    println("Giá trị lateinit: ${lateInitExample.currentWord}")
}

// Thuộc tính dự phòng
class BackupPropertyExample {
    private var _currentScrambledWord = "test"

    val currentScrambledWord: String
        get() = _currentScrambledWord
}

// Lateinit
class LateInitExample {
    lateinit var currentWord: String

    fun initWord(word: String) {
        currentWord = word
    }
}

// apply
class Person {
    var name: String = ""
    var age: Int = 0
}

class DetailActivity {
    companion object {
        const val LETTER = "letter"
    }
}

// delegation
class GameViewModel


