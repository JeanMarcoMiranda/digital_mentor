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