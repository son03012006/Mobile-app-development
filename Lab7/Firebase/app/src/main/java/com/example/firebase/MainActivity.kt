package com.example.firebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.firebase.ui.theme.FirebaseTheme
import com.example.firebase.ui.theme.greenColor
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseTheme {
                val context = LocalContext.current
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("CREATE COURSE", fontWeight = FontWeight.Bold) },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = greenColor,
                                titleContentColor = Color.White
                            )
                        )
                    }
                ) { innerPadding ->
                    AddCourseScreen(context, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AddCourseScreen(context: Context, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Course Name") },
            leadingIcon = { Icon(Icons.Default.Book, contentDescription = null, tint = greenColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = duration, onValueChange = { duration = it },
            label = { Text("Duration (e.g. 1 Year)") },
            leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null, tint = greenColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = description, onValueChange = { description = it },
            label = { Text("Description") },
            leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = greenColor) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                if (name.isBlank() || duration.isBlank() || description.isBlank()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val db = FirebaseFirestore.getInstance()
                    val ref = db.collection("Courses").document()
                    val course = Course(ref.id, name, duration, description)
                    ref.set(course).addOnSuccessListener {
                        Toast.makeText(context, "Course saved!", Toast.LENGTH_SHORT).show()
                        name = ""; duration = ""; description = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = greenColor)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("SAVE COURSE", fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = { context.startActivity(Intent(context, CourseDetailsActivity::class.java)) }) {
            Text("View all saved courses →", color = Color.Gray)
        }
    }
}