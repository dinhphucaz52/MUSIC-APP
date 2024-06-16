package com.example.mymusicapp.data.repository

object UserRepository {
    private var email: String? = null
    var name: String? = null
    var photoURL: String? = null
    var userUID: String? = null
    fun setUser(email: String?, displayName: String?, photoURL: String, uid: String) {
        this.email = email
        this.name = displayName
        this.photoURL = photoURL
        this.userUID = uid
    }
}