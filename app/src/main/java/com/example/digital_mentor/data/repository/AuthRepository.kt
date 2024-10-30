package com.example.digital_mentor.data.repository

import io.github.jan.supabase.auth.user.UserInfo

interface AuthRepository {
    suspend fun signUpWithEmail(
        userName: String,
        userEmail: String,
        userPassword: String
    ): UserInfo?

    suspend fun signInWithEmail(userEmail: String, userPassword: String): Unit
}