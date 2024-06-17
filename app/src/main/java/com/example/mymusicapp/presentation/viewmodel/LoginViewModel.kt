package com.example.mymusicapp.presentation.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.data.repository.AuthenticationRepository
import com.example.mymusicapp.data.repository.UserRepository
import com.example.mymusicapp.enum.LoginEnum
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.Auth

class LoginViewModel : ViewModel() {

    private lateinit var repository: AuthenticationRepository
    fun setRepository(repository: AuthenticationRepository) {
        this.repository = repository
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            repository.handleSignInResultOk()
            loginStatus.postValue(LoginEnum.SUCCESS)
        } else {
            loginStatus.postValue(LoginEnum.FAIL)
        }
    }

    companion object {
        @Volatile
        private var instance: LoginViewModel? = null

        @MainThread
        fun getInstance(): LoginViewModel {
            return instance ?: synchronized(this) {
                instance ?: LoginViewModel().also { instance = it }
            }
        }
        @MainThread
        fun clearInstance() {
            synchronized(this) {
                instance = null
            }
        }
    }

    private var loginStatus = MutableLiveData(LoginEnum.NULL)
    fun observeLoginStatus() = loginStatus
    fun getUserFromSharedPreferences() {
        val result = repository.checkUser()
        if (result == LoginEnum.SUCCESS) {
            loginStatus.postValue(LoginEnum.SUCCESS)
        } else {
            loginStatus.postValue(LoginEnum.NULL)
        }
    }
}