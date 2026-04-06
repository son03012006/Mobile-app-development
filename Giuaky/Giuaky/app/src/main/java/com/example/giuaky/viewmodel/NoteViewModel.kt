package com.example.giuaky.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.giuaky.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val appContext = application.applicationContext

    private val CLOUD_NAME = "dxlis0abwn"
    private val UPLOAD_PRESET = "note_app_preset"
    private val API_KEY = "686158312619219"
    private val client = OkHttpClient()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var listenerRegistration: ListenerRegistration? = null

    init {
        fetchNotes()
    }

    // Phân quyền: Admin thấy hết, user thấy của bản thân
    fun fetchNotes() {
        listenerRegistration?.remove()
        val user = auth.currentUser ?: return

        // Kiểm tra quyền Admin dựa trên email
        val isAdmin = user.email == "admin@gmail.com"

        val query = if (isAdmin) {
            // Lấy toàn bộ Note trong Database
            db.collection("notes")
        } else {
            // Chỉ lấy những Note có userId trùng với mình
            db.collection("notes").whereEqualTo("userId", user.uid)
        }

        listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("FIRESTORE", "Error: ${error.message}")
                _errorMessage.value = "Failed to load notes"
                return@addSnapshotListener
            }
            val list = snapshot?.toObjects(Note::class.java) ?: emptyList()
            _notes.value = list
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

    // Xử lý updoad tệp hình ảnh
    private suspend fun uploadToCloudinary(uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = appContext.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@withContext null

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "image.jpg", bytes.toRequestBody("image/*".toMediaTypeOrNull()))
                    .addFormDataPart("upload_preset", UPLOAD_PRESET)
                    .addFormDataPart("api_key", API_KEY)
                    .build()

                val request = Request.Builder().url("https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload").post(requestBody).build()

                client.newCall(request).execute().use { response ->
                    val res = response.body?.string()
                    if (response.isSuccessful && res != null) JSONObject(res).getString("secure_url") else null
                }
            } catch (e: Exception) { null }
        }
    }

    // Lưu ghi chú mới kèm định danh người dùng
    fun saveNote(title: String, desc: String, uri: Uri?) {
        val user = auth.currentUser ?: return
        val id = db.collection("notes").document().id

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val imageUrl = if (uri != null) uploadToCloudinary(uri) ?: "" else ""

                // Ghi kèm cả userId và userEmail
                val note = Note(
                    id = id,
                    title = title,
                    description = desc,
                    fileUrl = imageUrl,
                    userId = user.uid,
                    userEmail = user.email ?: ""
                )

                db.collection("notes").document(id).set(note)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cập nhật thông tin ghi chú
    fun updateNote(id: String, title: String, desc: String, uri: Uri?, oldUrl: String, originalUserId: String, originalUserEmail: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val imageUrl = if (uri != null) uploadToCloudinary(uri) ?: oldUrl else oldUrl

                // Giữ nguyên chủ sở hữu cũ, dù Admin là người sửa
                val note = Note(
                    id = id,
                    title = title,
                    description = desc,
                    fileUrl = imageUrl,
                    userId = originalUserId,
                    userEmail = originalUserEmail
                )

                db.collection("notes").document(id).set(note)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Xóa sản phẩm khỏi Firebase
    fun deleteNote(id: String) {
        db.collection("notes").document(id).delete()
    }

    fun logout() {
        listenerRegistration?.remove()
        listenerRegistration = null
        _notes.value = emptyList()
        auth.signOut()
    }

    fun clearError() { _errorMessage.value = null }
}