package com.example.digital_mentor.data.repository

import android.util.Log
import com.example.digital_mentor.data.model.UserProfileEntity
import com.example.digital_mentor.data.model.UserProfileEntityCreate
import com.example.digital_mentor.data.model.UserProfileEntityUpdate
import com.example.digital_mentor.domain.repository.UserProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UserProfileRepositoryImpl(
    private val supabase: SupabaseClient
) : UserProfileRepository {
    override suspend fun getUserProfile(userId: String): Result<UserProfileEntity> {
        return try {
            val user = supabase.from("user_profiles").select {
                filter {
                    UserProfileEntity::id eq userId
                }
            }.decodeSingle<UserProfileEntity>()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUserProfile(userData: UserProfileEntityCreate): Result<UserProfileEntity> {
        return try {
            val user: UserProfileEntity = supabase.from("user_profiles")
                .insert(userData).decodeSingle()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun ensureUserProfileExists(userData: UserProfileEntityCreate): Result<UserProfileEntity> {
        return try {
            // 1. Verificar si el perfil del usuario ya existe
            val existingProfileResult = getUserProfile(userData.id)

            existingProfileResult.fold(
                onSuccess = { existingProfile ->
                    Log.d("CheckUserExists", "Here exists")
                    // Si existe, devolverlo
                    Result.success(existingProfile)
                },
                onFailure = { error ->
                    Log.d("CheckUserExists", "Here doesnt exists")
                    if (error.message?.contains("No such record") == true) {
                        Log.d("CheckUserExists", "Here to create profile")
                        // 2. Si no existe, crear el perfil
                        saveUserProfile(userData)
                    } else {
                        // Si hay otro tipo de error, devolverlo
                        Result.failure(error)
                    }
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(
        userId: String,
        userData: UserProfileEntityUpdate
    ): Result<UserProfileEntity> {
        return try {
            Log.d("UpdateUser", "Here")
            val updatedUserProfile: UserProfileEntity =
                supabase.from("user_profiles").update(userData) {
                    select()
                    filter {
                        UserProfileEntity::id eq userId
                    }
                }.decodeSingle()

            Log.d("UpdateUser", "Here2")
            Result.success(updatedUserProfile)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}