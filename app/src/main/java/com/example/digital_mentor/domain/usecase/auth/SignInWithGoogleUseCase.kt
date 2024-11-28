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
        return try {
            val userSignedInResult = repository.signInWithGoogle(context)

            Log.d("CheckUserExists", "Here")
            userSignedInResult.fold(
                onSuccess = { userToCreate ->
                    // 2. Asegurar que el perfil exista
                    val userProfileResult = userProfileRepository.ensureUserProfileExists(
                        userToCreate
                    )

                    Log.d("CreatedUser", "After create user: $userProfileResult")
                    userProfileResult.fold(
                        onSuccess = { userProfile ->
                            Result.success(userProfile)
                        },
                        onFailure = { error ->
                            Result.failure(error)
                        }
                    )
                },
                onFailure = { error ->
                    Log.d("CreatedUser", "Here Error")
                    Result.failure(Exception("Failed to authenticate user with Google: ${error.message}"))
                }
            )

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}