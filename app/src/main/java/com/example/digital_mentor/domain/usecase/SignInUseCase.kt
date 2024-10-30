package com.example.digital_mentor.domain.usecase

import android.util.Log
import com.example.digital_mentor.data.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signInWithEmail(email, password)
}