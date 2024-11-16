package com.example.digital_mentor.domain.usecase.auth

import com.example.digital_mentor.domain.repository.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> = authRepository.signOut()
}