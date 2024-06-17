package com.example.mymusicapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.enum.LoginEnum
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AuthenticationRepository(private val context: Context) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun handleSignInResultOk() {
        val user = FirebaseAuth.getInstance().currentUser
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
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveUserToSharedPreferences(
        email: String?,
        displayName: String?,
        photoUrl: String,
        uid: String
    ) {
        val sharedPreferences =
            context.getSharedPreferences(AppCommon.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(AppCommon.SHARED_PREF_EMAIL, email)
        editor.putString(AppCommon.SHARED_PREF_DISPLAY_NAME, displayName)
        editor.putString(AppCommon.SHARED_PREF_PHOTO_URL, photoUrl)
        editor.putString(AppCommon.SHARED_PREF_UID, uid)
        editor.apply()
    }

    fun checkUser(): Int {
        val sharedPreferences =
            context.getSharedPreferences(AppCommon.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        val email = sharedPreferences.getString(AppCommon.SHARED_PREF_EMAIL, null)
        val displayName = sharedPreferences.getString(AppCommon.SHARED_PREF_DISPLAY_NAME, null)
        val photoUrl = sharedPreferences.getString(AppCommon.SHARED_PREF_PHOTO_URL, null)
        val uid = sharedPreferences.getString(AppCommon.SHARED_PREF_UID, null)

        if (email != null && displayName != null && photoUrl != null && uid != null) {
            UserRepository.setUser(email, displayName, photoUrl, uid)
            return LoginEnum.SUCCESS
        }
        return LoginEnum.NULL
    }

}