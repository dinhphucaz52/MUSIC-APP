package com.example.mymusicapp.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.repository.UserRepository
import com.example.mymusicapp.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@UnstableApi
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authentication: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    @UnstableApi
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = authentication.currentUser
            if (user != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    database.reference.apply {
                        child("user/${user.uid}/email").setValue(user.email)
                        child("user/${user.uid}/name").setValue(user.displayName)
                        child("user/${user.uid}/photoUrl").setValue(user.photoUrl.toString())
                        child("user/${user.uid}/uid").setValue(user.uid)
                    }
                    saveUserToSharedPreferences(
                        user.email,
                        user.displayName,
                        user.photoUrl.toString(),
                        user.uid
                    )
                }
                UserRepository.setUser(
                    user.email,
                    user.displayName,
                    user.photoUrl.toString(),
                    user.uid,
                )

            }
            Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
            loadMainActivity()
        } else {
            Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveUserToSharedPreferences(
        email: String?,
        displayName: String?,
        photoUrl: String,
        uid: String
    ) {
        val sharedPreferences =
            getSharedPreferences(AppCommon.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(AppCommon.SHARED_PREF_EMAIL, email)
        editor.putString(AppCommon.SHARED_PREF_DISPLAY_NAME, displayName)
        editor.putString(AppCommon.SHARED_PREF_PHOTO_URL, photoUrl)
        editor.putString(AppCommon.SHARED_PREF_UID, uid)

        editor.apply()
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
                requestSignInWithGoogle()
            }
            buttonLogin.setOnClickListener {
                loadMainActivity()
            }
        }
    }

    @UnstableApi
    private fun loadMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun requestSignInWithGoogle() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun init() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        authentication = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        getUserFromSharedPreferences()

    }

    private fun getUserFromSharedPreferences() {
        val sharedPreferences =
            getSharedPreferences(AppCommon.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        val email = sharedPreferences.getString(AppCommon.SHARED_PREF_EMAIL, null)
        val displayName = sharedPreferences.getString(AppCommon.SHARED_PREF_DISPLAY_NAME, null)
        val photoUrl = sharedPreferences.getString(AppCommon.SHARED_PREF_PHOTO_URL, null)
        val uid = sharedPreferences.getString(AppCommon.SHARED_PREF_UID, null)

        if (email != null && displayName != null && photoUrl != null && uid != null) {
            UserRepository.setUser(email, displayName, photoUrl, uid)
            loadMainActivity()
        } else {
            binding.splashScreen.visibility = View.GONE
        }

        editor.apply()
    }
}