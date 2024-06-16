package com.example.mymusicapp.data.repository

import com.google.firebase.auth.FirebaseAuth


class AuthenticationRepository {
    private val authentication by lazy {
        FirebaseAuth.getInstance()
    }

}