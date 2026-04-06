package com.example.giuaky.model

data class Note(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val fileUrl: String = "",
    val userId: String = "",
    val userEmail: String = ""
) {
    constructor() : this("", "", "", "", "","")
}
