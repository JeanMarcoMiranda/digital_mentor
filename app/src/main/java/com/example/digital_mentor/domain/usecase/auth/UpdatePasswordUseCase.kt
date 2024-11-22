package com.example.digital_mentor.domain.usecase.auth

import com.example.digital_mentor.domain.repository.AuthRepository

class UpdatePasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(newPassword: String): Result<Unit> {
        return repository.updatePassword(newPassword)
    }
}