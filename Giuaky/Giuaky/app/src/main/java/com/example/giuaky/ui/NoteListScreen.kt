package com.example.giuaky.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.giuaky.R
import com.example.giuaky.model.Note

@Composable
fun NoteListScreen(
    notes: List<Note>,
    isAdmin: Boolean,
    onDelete: (String) -> Unit,
    onEdit: (Note) -> Unit
) {
    val primaryOrange = Color(0xFFFF9800)
    val darkText = Color(0xFF2D2D2D)
    val lightBlueBg = Color(0xFFE3F2FD)
    val lightRedBg = Color(0xFFFFEBEE)

    if (notes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No notes available yet.", color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    } else {
        // Hiển thị danh sách
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
        ) {
            items(notes, key = { it.id }) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Hiển thị hình ảnh đi kèm mỗi tiêu đề
                        if (note.fileUrl.isNotEmpty()) {
                            AsyncImage(
                                model = note.fileUrl,
                                contentDescription = "Note Image",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF5F5F5)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_note1),
                                contentDescription = "Note Icon",
                                tint = primaryOrange,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            // Phân hệ admin: Hiển thị Email chủ sở hữu
                            // Giúp Admin định danh được ghi chú của từng người dùng
                            if (isAdmin && note.userEmail.isNotEmpty()) {
                                Surface(
                                    color = primaryOrange.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(bottom = 6.dp) // Cách Title một chút
                                ) {
                                    Text(
                                        text = "Owner: ${note.userEmail}",
                                        color = primaryOrange,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = note.title,
                                fontWeight = FontWeight.ExtraBold,
                                color = darkText,
                                fontSize = 17.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = note.description,
                                color = Color.Gray,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            // Thông báo đính kèm
                            if (note.fileUrl.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(primaryOrange.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = null,
                                        tint = primaryOrange,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Attachment included",
                                        color = primaryOrange,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Các chức năng sửa và xoá
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = { onEdit(note) },
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(color = lightBlueBg, shape = RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF2196F3).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    null,
                                    tint = Color(0xFF2196F3),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            IconButton(
                                onClick = { onDelete(note.id) },
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(color = lightRedBg, shape = RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFFF44336).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    null,
                                    tint = Color(0xFFF44336),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}