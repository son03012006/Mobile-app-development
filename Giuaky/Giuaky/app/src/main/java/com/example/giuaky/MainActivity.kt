package com.example.giuaky

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.giuaky.model.Note
import com.example.giuaky.ui.*
import com.example.giuaky.viewmodel.NoteViewModel
import com.google.firebase.auth.FirebaseAuth

// Màu nền kem nhẹ nhàng cho ứng dụng
val LightCream = Color(0xFFFFF9E1)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = LightCream) {
                    val auth = FirebaseAuth.getInstance()
                    val vm: NoteViewModel = viewModel()

                    // Quản lý trạng thái đăng nhập và điều hướng
                    var user by remember { mutableStateOf(auth.currentUser) }
                    var isRegistering by remember { mutableStateOf(false) }
                    var currentHomeTab by remember { mutableStateOf("home_list") }
                    var editingNote by remember { mutableStateOf<Note?>(null) }

                    // Luồng chưa đăng nhập
                    if (user == null) {
                        if (isRegistering) {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    isRegistering = false
                                    Toast.makeText(this@MainActivity, "Account created! Please login.", Toast.LENGTH_SHORT).show()
                                },
                                onNavigateToLogin = { isRegistering = false }
                            )
                        } else {
                            LoginScreen(
                                // Đăng nhập thành công
                                onLoginSuccess = {
                                    user = auth.currentUser
                                    vm.fetchNotes() // Tải lại ghi chú sau khi login
                                    Toast.makeText(this@MainActivity, "Welcome back! 👋", Toast.LENGTH_SHORT).show()
                                },
                                onNavigateToRegister = { isRegistering = true }
                            )
                        }
                    }
                    // Luồng đã đăng nhập
                    else {
                        when (currentHomeTab) {
                            // 1. Màn hình danh sách ghi chú
                            "home_list" -> HomeScreen(
                                vm = vm,
                                onNavigateToAdd = {
                                    editingNote = null // Reset để hiểu là thêm mới
                                    currentHomeTab = "home_add"
                                },
                                onLogout = {
                                    vm.logout()
                                    user = null
                                    currentHomeTab = "home_list"
                                    Toast.makeText(this@MainActivity, "Logged out successfully", Toast.LENGTH_SHORT).show()
                                },
                                onEdit = { note ->
                                    // Chuyển dữ liệu Note vào trạng thái Editing để thực hiện sửa
                                    editingNote = note
                                    currentHomeTab = "home_add"
                                }
                            )

                            // Màn hình thêm/sửa ghi chú
                            "home_add" -> NoteInputScreen(
                                existingNote = editingNote,
                                onSave = { title, desc, uri ->
                                    if (editingNote == null) {
                                        // Chức năng thêm mới
                                        vm.saveNote(title, desc, uri)
                                        Toast.makeText(this@MainActivity, "Note saved! 📝", Toast.LENGTH_SHORT).show()
                                    } else {
                                        // Cập nhật
                                        vm.updateNote(
                                            id = editingNote!!.id,
                                            title = title,
                                            desc = desc,
                                            uri = uri,
                                            oldUrl = editingNote!!.fileUrl,
                                            originalUserId = editingNote!!.userId,
                                            originalUserEmail = editingNote!!.userEmail
                                        )
                                        Toast.makeText(this@MainActivity, "Note updated! ✅", Toast.LENGTH_SHORT).show()
                                    }
                                    currentHomeTab = "home_list"
                                },
                                onBack = { currentHomeTab = "home_list" }
                            )
                        }
                    }
                }
            }
        }
    }
}