package com.example.firebase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.firebase.ui.theme.FirebaseTheme
import com.example.firebase.ui.theme.greenColor
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
class CourseDetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseTheme {
                val context = LocalContext.current
                val courseList = remember { mutableStateListOf<Course>() }

                fun loadData() {
                    FirebaseFirestore.getInstance().collection("Courses").get()
                        .addOnSuccessListener { query ->
                            courseList.clear()
                            for (doc in query.documents) {
                                doc.toObject(Course::class.java)?.let { courseList.add(it) }
                            }
                        }
                }
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            loadData()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                LaunchedEffect(Unit) { loadData() }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("MY COURSES") },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = greenColor, titleContentColor = Color.White
                            )
                        )
                    }
                ) { padding ->
                    LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(12.dp)) {
                        items(courseList) { course ->
                            CourseItem(course, context) { loadData() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseItem(course: Course, context: Context, onRefresh: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(course.courseName ?: "", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = greenColor)
                Spacer(Modifier.height(4.dp))
                Text("⏱ Duration: ${course.courseDuration}", fontSize = 14.sp, color = Color.DarkGray)
                Text("📝 ${course.courseDescription}", fontSize = 14.sp, color = Color.Gray)
            }

            // Edit Button
            IconButton(onClick = {
                val intent = Intent(context, UpdateCourse::class.java).apply {
                    putExtra("courseID", course.courseID)
                    putExtra("courseName", course.courseName)
                    putExtra("courseDuration", course.courseDuration)
                    putExtra("courseDescription", course.courseDescription)
                }
                context.startActivity(intent)
            }) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Blue)
            }

        }
    }
}