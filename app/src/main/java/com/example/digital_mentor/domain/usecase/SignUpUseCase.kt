package com.example.digital_mentor.domain.usecase

import com.example.digital_mentor.data.repository.AuthRepository

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        authRepository.signUpWithEmail(name, email, password)
}