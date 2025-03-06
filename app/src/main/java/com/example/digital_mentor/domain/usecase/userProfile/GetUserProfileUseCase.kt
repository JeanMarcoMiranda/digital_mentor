package com.jacket.digital_mentor.domain.usecase.userProfile

import com.jacket.digital_mentor.data.model.UserProfileEntity
import com.jacket.digital_mentor.domain.repository.UserProfileRepository

class GetUserProfileUseCase(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(userId: String): Result<UserProfileEntity> {
        return try {
            val userProfileResult = userRepository.getUserProfile(userId)

            if (userProfileResult.isFailure) throw Exception(userProfileResult.exceptionOrNull()?.message)

            val userProfile = userProfileResult.getOrNull() ?: throw Exception("User profile not found")

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}