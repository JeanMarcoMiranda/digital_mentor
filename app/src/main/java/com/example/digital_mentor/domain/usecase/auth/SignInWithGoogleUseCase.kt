package com.example.digital_mentor.domain.usecase.auth

import android.content.Context
import android.util.Log
import com.example.digital_mentor.domain.repository.AuthRepository
import com.example.digital_mentor.domain.repository.UserProfileRepository

class SignInWithGoogleUseCase(
    private val repository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke(context: Context): Result<Boolean> {
        return runCatching {
            // Intentar autenticar al usuario con Google
            val userToCreate = repository.signInWithGoogle(context).getOrThrow()

            // Asegurar que el perfil del usuario exista
            userProfileRepository.ensureUserProfileExists(userToCreate).getOrThrow()
        }.map {
            // Si todo fue exitoso, devolver éxito
            true
        }.onFailure { error ->
            // Manejar fallos y cerrar sesión si es necesario
            Log.e("SignInWithGoogle", "Error: ${error.message}", error)
            repository.signOut()
        }
    }
}