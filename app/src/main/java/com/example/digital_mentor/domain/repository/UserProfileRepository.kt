package com.jacket.digital_mentor.domain.repository

import com.jacket.digital_mentor.data.model.UserProfileEntity
import com.jacket.digital_mentor.data.model.UserProfileEntityCreate
import com.jacket.digital_mentor.data.model.UserProfileEntityUpdate

interface UserProfileRepository {
    suspend fun getUserProfile(userId: String): Result<UserProfileEntity>

    suspend fun saveUserProfile(userData: UserProfileEntityCreate): Result<UserProfileEntity>

    suspend fun ensureUserProfileExists(userData: UserProfileEntityCreate): Result<UserProfileEntity>

    suspend fun updateUserProfile(
        userId: String,
        userData: UserProfileEntityUpdate
    ): Result<UserProfileEntity>
}