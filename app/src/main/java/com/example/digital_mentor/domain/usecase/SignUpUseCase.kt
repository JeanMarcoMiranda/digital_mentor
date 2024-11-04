package com.example.digital_mentor.domain.usecase

import com.example.digital_mentor.domain.repository.AuthRepository

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, card: String,email: String, password: String) =
        authRepository.signUpWithEmail(name, card, email, password)
}