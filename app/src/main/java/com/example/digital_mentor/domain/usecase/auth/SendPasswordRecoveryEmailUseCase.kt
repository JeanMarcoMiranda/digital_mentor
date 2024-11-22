package com.example.digital_mentor.domain.usecase.auth

import com.example.digital_mentor.domain.repository.AuthRepository

class SendPasswordRecoveryEmailUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return repository.sendPasswordRecoveryEmail(email)
    }
}