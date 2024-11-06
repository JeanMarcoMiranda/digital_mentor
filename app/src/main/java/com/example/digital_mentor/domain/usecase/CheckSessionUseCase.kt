package com.example.digital_mentor.domain.usecase

import com.example.digital_mentor.domain.repository.AuthRepository

class CheckSessionUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isSessionActive()
    }
}