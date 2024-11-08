package com.example.digital_mentor.domain.usecase

import com.example.digital_mentor.data.model.UserProfileEntityUpdate
import com.example.digital_mentor.domain.repository.UserProfileRepository

class UpdateUserProfileUseCase(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(
        userData: UserProfileEntityUpdate
    ): Result<Boolean> {
        return try {
            userRepository.updateUserProfile(userData)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}