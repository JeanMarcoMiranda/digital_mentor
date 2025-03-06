package com.jacket.digital_mentor.domain.usecase.auth

import com.jacket.digital_mentor.domain.repository.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> = authRepository.signOut()
}