package com.example.digital_mentor.domain.usecase.auth

import com.example.digital_mentor.data.model.UserProfileEntityCreate
import com.example.digital_mentor.domain.repository.AuthRepository
import com.example.digital_mentor.domain.repository.UserProfileRepository

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserProfileRepository
) {
    suspend operator fun invoke(
        name: String,
        phoneNumber: String?,
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            val userInfoResult = authRepository.signUpWithEmail(email, password)

            if (userInfoResult.isFailure) throw Exception("Cannot access user auth data")

            val userInfo = userInfoResult.getOrNull()

            if (userInfo != null) {
                val userId = userInfo.id
                val userCreateData = UserProfileEntityCreate(
                    id = userId,
                    name = name,
                    phoneNumber = phoneNumber ?: null
                )

                userRepository.saveUserProfile(userCreateData)
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}