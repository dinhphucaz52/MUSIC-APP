package com.example.mymusicapp.presentation.activity

import android.content.Intent
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.OpenGL.MyRenderer
import com.example.mymusicapp.data.repository.AuthenticationRepository
import com.example.mymusicapp.databinding.ActivityLoginBinding
import com.example.mymusicapp.enum.LoginEnum
import com.example.mymusicapp.presentation.viewmodel.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract


@UnstableApi
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginMVVM: LoginViewModel

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        loginMVVM.onSignInResult(res)
//        this.onSignInResult(res)
    }


    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
//        initNo2()
        preprocessing()
        observeData()
        setEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        LoginViewModel.clearInstance()
    }

    private fun preprocessing() {
        loginMVVM.getUserFromSharedPreferences()
    }

    private fun observeData() {
        loginMVVM.observeLoginStatus().observe(this@LoginActivity) {
            when(it) {
                LoginEnum.NULL -> {
                    binding.splashScreen.visibility = View.GONE
                }
                LoginEnum.LOADING -> {
                    binding.splashScreen.visibility = View.VISIBLE
                }
                LoginEnum.SUCCESS -> {
                    binding.splashScreen.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                    loadMainActivity()
                }
                LoginEnum.FAIL -> {
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    binding.splashScreen.visibility = View.GONE
                }
            }
        }
    }

    @UnstableApi
    private fun setEvents() {
        binding.apply {
            buttonGoogle.setOnClickListener {
                requestSignInWithGoogle()
            }
            buttonLogin.setOnClickListener {
//                loadMainActivity()
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

    private fun initNo2() {
        val glSurfaceView = GLSurfaceView(this)
        setContentView(glSurfaceView)
        enableEdgeToEdge()
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setRenderer(MyRenderer())
    }

    private fun init() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        loginMVVM = LoginViewModel.getInstance()
        loginMVVM.setRepository(AuthenticationRepository(this))
    }


}