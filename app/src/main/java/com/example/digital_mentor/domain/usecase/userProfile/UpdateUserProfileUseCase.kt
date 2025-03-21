package com.jacket.digital_mentor.domain.usecase.userProfile

import android.util.Log
import com.jacket.digital_mentor.data.model.UserProfileEntityUpdate
import com.jacket.digital_mentor.domain.repository.UserProfileRepository

class UpdateUserProfileUseCase(
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(
        userId: String,
        userData: UserProfileEntityUpdate
    ): Result<Boolean> {
        return try {
            val userUpdated = userRepository.updateUserProfile(userId, userData)

            Log.d("UpdateUser", "This is the user update error: ${userUpdated.exceptionOrNull()?.message}")
            if (userUpdated.isFailure) throw Exception(userUpdated.exceptionOrNull()?.message)

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}