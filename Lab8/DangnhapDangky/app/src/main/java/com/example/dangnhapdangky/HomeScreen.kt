package com.example.dangnhapdangky

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    val firebaseAuth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SUCCESS", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Chào mừng bạn đã quay lại!", color = Color.Gray)

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)), // Màu đỏ cho Sign Out
                    onClick = {
                        firebaseAuth.signOut()
                        navController.navigate(Screen.Signin.rout) {
                            popUpTo(Screen.Home.rout) { inclusive = true }
                        }
                    }
                ) {
                    Text("SIGN OUT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}