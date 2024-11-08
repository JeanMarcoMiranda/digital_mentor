package com.example.digital_mentor.domain.usecase

import com.example.digital_mentor.data.model.UserProfileEntityCreate
import com.example.digital_mentor.domain.repository.AuthRepository
import com.example.digital_mentor.domain.repository.UserProfileRepository

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(
        name: String,
        card: String,
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            val userInfoResult = authRepository.signUpWithEmail(email, password)

            if (userInfoResult.isFailure) throw Exception("Cannot access user auth data")

            val userInfo = userInfoResult.getOrNull()

            if (userInfo != null) {
                val userId = userInfo.id
                val userCreateData = UserProfileEntityCreate(id = userId, name = name, card = card)

                userRepository.saveUserProfile(userCreateData)
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}