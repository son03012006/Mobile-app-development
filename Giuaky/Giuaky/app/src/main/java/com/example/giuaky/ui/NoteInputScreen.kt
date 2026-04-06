package com.example.giuaky.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.giuaky.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputScreen(
    existingNote: Note? = null,
    onSave: (String, String, Uri?) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(existingNote?.title ?: "") }
    var desc by remember { mutableStateOf(existingNote?.description ?: "") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    val existingImageUrl = existingNote?.fileUrl?.takeIf { it.isNotEmpty() }

    // Khởi tạo bộ chọn file từ thư viện máy
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> selectedUri = uri }

    val primaryOrange = Color(0xFFFF9800)
    val darkButton = Color(0xFF2D2D2D)
    val lightCream = Color(0xFFFFF9E1)

    Scaffold(
        containerColor = lightCream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (existingNote == null) "CREATE NOTE" else "EDIT NOTE",
                        fontWeight = FontWeight.Black,
                        color = darkButton
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = primaryOrange)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(15.dp, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    // Trường nhập liệu Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Note Title") },
                        leadingIcon = { Icon(Icons.Default.Create, null, tint = primaryOrange) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryOrange,
                            focusedLabelColor = primaryOrange
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trường nhập liệu description
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description") },
                        leadingIcon = { Icon(Icons.Default.Info, null, tint = primaryOrange) },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryOrange,
                            focusedLabelColor = primaryOrange
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Chức năng bổ sung, thay đổi tệp ảnh
                    Text(text = "Attachment", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = darkButton)
                    ) {
                        Icon(Icons.Default.Add, null, tint = primaryOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (selectedUri != null || existingImageUrl != null) "Change Image" else "Select Image")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Khu vực hiển thị file
                    val imageSource = selectedUri ?: existingImageUrl
                    if (imageSource != null) {
                        Column {
                            AsyncImage(
                                model = imageSource,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF5F5F5)),
                                contentScale = ContentScale.Crop
                            )

                            // Minh chứng tệp đã được lưu trữ trên Cloud Firestore/Cloudinary
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Icon(Icons.Default.Link, null, tint = primaryOrange, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = imageSource.toString(),
                                    fontSize = 10.sp,
                                    color = Color.DarkGray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Lưu (Save) hoặc Cập nhật (Update)
            // Nếu Admin đang sử dụng, logic này sẽ thực hiện "Sửa sản phẩm ở phân hệ admin"
            Button(
                onClick = { if (title.isNotEmpty()) onSave(title, desc, selectedUri) },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkButton),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(
                    text = if (existingNote == null) "SAVE NOTE" else "UPDATE CHANGES",
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}