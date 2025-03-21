package com.jacket.digital_mentor.domain.repository

import android.content.Context
import com.jacket.digital_mentor.data.model.UserProfileEntityCreate
import io.github.jan.supabase.auth.user.UserInfo

interface AuthRepository {
    suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<UserInfo>

    suspend fun signInWithEmail(email: String, password: String): Result<Boolean>

    suspend fun signOut(): Result<Boolean>

    suspend fun signInWithGoogle(context: Context): Result<UserProfileEntityCreate>

    suspend fun getCurrentUserInfo(): Result<UserInfo>

    suspend fun isSessionActive(): Boolean

    suspend fun sendPasswordRecoveryEmail(email: String): Result<Unit>

    suspend fun updatePassword(newPassword: String): Result<Unit>
}