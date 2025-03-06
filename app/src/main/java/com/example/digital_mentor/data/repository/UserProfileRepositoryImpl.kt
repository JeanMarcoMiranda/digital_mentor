package com.jacket.digital_mentor.data.repository

import android.util.Log
import com.jacket.digital_mentor.data.model.UserProfileEntity
import com.jacket.digital_mentor.data.model.UserProfileEntityCreate
import com.jacket.digital_mentor.data.model.UserProfileEntityUpdate
import com.jacket.digital_mentor.domain.repository.UserProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UserProfileRepositoryImpl(
    private val supabase: SupabaseClient
) : UserProfileRepository {

    override suspend fun saveUserProfile(userData: UserProfileEntityCreate): Result<UserProfileEntity> {
        return try {
            val userResponse = supabase.from("user_profiles")
                .insert(userData) {
                    select()
                }

            val users = userResponse.decodeSingle<UserProfileEntity>()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(userId: String): Result<UserProfileEntity> {
        return try {
            val users: List<UserProfileEntity> = supabase.from("user_profiles").select {
                filter { UserProfileEntity::id eq userId }
            }.decodeList()

            if (users.isEmpty()) {
                Result.failure(Exception("No such record"))
            } else {
                Result.success(users.first())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun ensureUserProfileExists(userData: UserProfileEntityCreate): Result<UserProfileEntity> {
        return getUserProfile(userData.id).recoverCatching { _ ->
            saveUserProfile(userData).getOrThrow()
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