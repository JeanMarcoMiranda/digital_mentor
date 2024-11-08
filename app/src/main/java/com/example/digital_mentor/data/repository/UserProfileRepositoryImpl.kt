package com.example.digital_mentor.data.repository

import com.example.digital_mentor.data.model.UserProfileEntity
import com.example.digital_mentor.data.model.UserProfileEntityCreate
import com.example.digital_mentor.data.model.UserProfileEntityUpdate
import com.example.digital_mentor.domain.repository.UserProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UserProfileRepositoryImpl(
    private val supabase: SupabaseClient
) : UserProfileRepository {
    override suspend fun getUserProfile(): Result<UserProfileEntity> {
        TODO("Not yet implemented")
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

    override suspend fun updateUserProfile(userData: UserProfileEntityUpdate): Result<UserProfileEntity> {
        return try {
            val updatedUserProfile: UserProfileEntity = supabase.from("user_profile").update(
                {
                    UserProfileEntity::name setTo userData.name
                    UserProfileEntity::card setTo userData.card
                    UserProfileEntity::cumulativeScore setTo userData.cumulativeScore
                    UserProfileEntity::illiteracyLevel setTo userData.illiteracyLevel
                    UserProfileEntity::lastTestDate setTo userData.lastTestDate
                }
            ).decodeSingle()

            Result.success(updatedUserProfile)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}