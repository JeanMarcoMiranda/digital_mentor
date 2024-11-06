package com.example.digital_mentor.domain.repository

import android.content.Context

interface AuthRepository {
    suspend fun signUpWithEmail(
        name: String,
        card: String,
        email: String,
        password: String
    ): Result<Boolean>

    suspend fun signInWithEmail(email: String, password: String): Result<Boolean>

    suspend fun signOut(): Result<Boolean>

    suspend fun signInWithGoogle(context: Context): Result<Boolean>

    suspend fun isSessionActive(): Boolean
}