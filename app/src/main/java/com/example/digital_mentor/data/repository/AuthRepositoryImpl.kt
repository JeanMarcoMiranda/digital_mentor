package com.example.digital_mentor.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepositoryImpl(private val supabaseClient: SupabaseClient) : AuthRepository {

    override suspend fun signUpWithEmail(
        userName: String,
        userEmail: String,
        userPassword: String
    ): UserInfo? =
        withContext(Dispatchers.IO) {
            val user = supabaseClient.auth.signUpWith(Email) {
                email = userEmail
                password = userPassword
                data = buildJsonObject {
                    put("userName", userName)
                    put("userCardNumber", "")
                }
            }

            return@withContext user
        }

    override suspend fun signInWithEmail(userEmail: String, userPassword: String): Unit =
        withContext(Dispatchers.IO) {
            val user = supabaseClient.auth.signInWith(Email) {
                email = userEmail
                password = userPassword
            }

            return@withContext user
        }

}