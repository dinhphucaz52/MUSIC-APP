package com.example.mymusicapp.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authentication by lazy {
        FirebaseAuth.getInstance()
    }

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val response = IdpResponse.fromResultIntent(data)
            val user = authentication.currentUser
            println("LoginActivity.signInLauncher: data: $data")
            println("LoginActivity.signInLauncher: response: $response")
            println("LoginActivity.signInLauncher: user: $user")
        } else {
            val response = IdpResponse.fromResultIntent(result.data)
            if (response != null) {
                println("LoginActivity.signInLauncher: Error: ${response.error}")
            }
        }
    }

    private val signInIntent by lazy {
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
            .build()
    }

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setEvents()
    }

    @UnstableApi
    private fun setEvents() {
        binding.apply {
            buttonGoogle.setOnClickListener {
//                signInLauncher.launch(signInIntent)
                authentication.signInWithEmailAndPassword("test", "test").addOnSuccessListener {
                    println("LoginActivity.setEvents: Success: $it")
                }
            }
            buttonLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun init() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
    }
}