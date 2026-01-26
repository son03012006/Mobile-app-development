package Lab1

import kotlinx.coroutines.*
import kotlin.random.Random

fun main() {

    // Run blocking
    // Chạy coroutine và chặn luồng hiện tại cho tới khi hoàn thành
    runBlocking {
        val output = getValue()
        println("Giá trị lấy được (runBlocking): $output")
    }

    // Global scope lauch
    // Chạy coroutine không đồng bộ
    val job: Job = GlobalScope.launch {
        val output = getValue()
        println("Giá trị lấy được (GlobalScope): $output")
    }

    // Hủy coroutine
    job.cancel()

    // ASYNC / AWAIT
    runBlocking {
        val deferred = async {
            getValue()
        }
        println("Giá trị lấy được (async/await): ${deferred.await()}")
    }

    // TRY - CATCH
    try {
        riskyFunction()
    } catch (e: Exception) {
        println("Bắt được ngoại lệ: ${e.message}")
    }

    // OBJECT
    DataProviderManager.printMessage()

    //  ENUM
    val direction = Direction.NORTH

    when (direction) {
        Direction.NORTH -> println("Hướng Bắc")
        Direction.SOUTH -> println("Hướng Nam")
        Direction.WEST -> println("Hướng Tây")
        Direction.EAST -> println("Hướng Đông")
    }
}

//  Hàm tạm ngưng
suspend fun getValue(): Double {
    // Giả lập công việc tốn thời gian
    delay(1000)
    return Random.nextDouble(1.0, 100.0)
}

// Gọi hàm tạm ngưng từ hàm tạm ngưng
suspend fun processValue(): Double {
    val value = getValue()
    return value * 2
}

// Object
object DataProviderManager {
    fun printMessage() {
        println("Lab1.DataProviderManager đang hoạt động")
    }
}

// Hàm phát sinh ngoại lệ
fun riskyFunction() {
    throw Exception("Đây là lỗi thử nghiệm")
}

// ===== ENUM =====
enum class Direction {
    NORTH, SOUTH, WEST, EAST
}
