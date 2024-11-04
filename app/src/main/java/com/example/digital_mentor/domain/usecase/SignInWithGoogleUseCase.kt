package com.example.digital_mentor.domain.usecase

import android.content.Context
import com.example.digital_mentor.domain.repository.AuthRepository

class SignInWithGoogleUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(context: Context): Result<Boolean> {
        return repository.signInWithGoogle(context)
    }
}