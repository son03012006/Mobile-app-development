package com.example.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.firebase.ui.theme.FirebaseTheme
import com.example.firebase.ui.theme.greenColor
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourse : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra("courseID") ?: ""
        val name = intent.getStringExtra("courseName") ?: ""
        val dur = intent.getStringExtra("courseDuration") ?: ""
        val des = intent.getStringExtra("courseDescription") ?: ""

        setContent {
            FirebaseTheme {
                val context = LocalContext.current
                var uName by remember { mutableStateOf(name) }
                var uDur by remember { mutableStateOf(dur) }
                var uDes by remember { mutableStateOf(des) }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("UPDATE DETAILS") },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = greenColor, titleContentColor = Color.White
                            )
                        )
                    }
                ) { padding ->
                    Column(modifier = Modifier.padding(padding).padding(24.dp)) {
                        OutlinedTextField(value = uName, onValueChange = { uName = it }, label = { Text("Course Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(value = uDur, onValueChange = { uDur = it }, label = { Text("Duration") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(value = uDes, onValueChange = { uDes = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3, shape = RoundedCornerShape(12.dp))

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = {
                                if (uName.isBlank()) {
                                    Toast.makeText(context, "Please enter course name", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val updatedData = Course(id, uName, uDur, uDes)
                                FirebaseFirestore.getInstance().collection("Courses").document(id).set(updatedData)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Update successful!", Toast.LENGTH_SHORT).show()
                                        (context as ComponentActivity).finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = greenColor)
                        ) {
                            Text("SAVE CHANGES", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}