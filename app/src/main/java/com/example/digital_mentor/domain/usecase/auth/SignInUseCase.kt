package com.example.digital_mentor.domain.usecase.auth

import com.example.digital_mentor.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> =
        authRepository.signInWithEmail(email, password)
}