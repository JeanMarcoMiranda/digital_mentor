package com.example.digital_mentor.data.repository

import android.content.Context
import io.github.jan.supabase.auth.user.UserInfo

interface AuthRepository {
    suspend fun signUpWithEmail(
        name: String,
        email: String,
        password: String
    ): Result<Boolean>

    suspend fun signInWithEmail(email: String, password: String): Result<Boolean>

    suspend fun signInWithGoogle(context: Context): Result<Boolean>
}