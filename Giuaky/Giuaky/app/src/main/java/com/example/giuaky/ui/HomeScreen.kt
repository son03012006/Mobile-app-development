package com.example.giuaky.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.giuaky.model.Note
import com.example.giuaky.viewmodel.NoteViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: NoteViewModel,
    onNavigateToAdd: () -> Unit,
    onLogout: () -> Unit,
    onEdit: (Note) -> Unit
) {
    // Theo dõi trạng thái từ ViewModel
    val notes by vm.notes.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val errorMessage by vm.errorMessage.collectAsState()
    val context = LocalContext.current

    // Logic phân quyền admin
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isAdmin = currentUser?.email == "admin@gmail.com"

    val primaryOrange = Color(0xFFFF9800)
    val darkText = Color(0xFF2D2D2D)

    // Hiển thị thông báo lỗi nếu có
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.clearError()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = primaryOrange,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "MY NOTES",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                fontSize = 22.sp,
                                color = darkText
                            )
                        )
                    }
                },
                actions = {
                    // Hiển thị tên người dùng trước dấu @
                    val displayName = currentUser?.email?.substringBefore("@") ?: "User"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Hi, $displayName",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = onLogout) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                null,
                                tint = primaryOrange,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            // Nút thêm ghi chú với hiệu ứng loading
            ExtendedFloatingActionButton(
                onClick = { if (!isLoading) onNavigateToAdd() },
                containerColor = if (isLoading) Color.Gray else darkText,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Syncing...")
                } else {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add New Note", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Hiển thị danh sách ghi chú
            NoteListScreen(
                notes = notes,
                isAdmin = isAdmin,
                onDelete = { noteId ->
                    vm.deleteNote(noteId)
                    Toast.makeText(context, "Note deleted! 🗑️", Toast.LENGTH_SHORT).show()
                },
                onEdit = { note -> onEdit(note) }
            )
        }
    }
}