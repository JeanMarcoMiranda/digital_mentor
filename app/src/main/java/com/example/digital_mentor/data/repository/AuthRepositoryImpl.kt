package com.jacket.digital_mentor.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.jacket.digital_mentor.BuildConfig
import com.jacket.digital_mentor.data.model.UserProfileEntityCreate
import com.jacket.digital_mentor.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.exceptions.RestException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import java.util.UUID

@Serializable
data class UserMetadata(
    @SerialName("name") val name: String? = null
)

class AuthRepositoryImpl(
    private val auth: Auth,
    private val googleIdOption: GetGoogleIdOption,
    private val credentialManager: CredentialManager
) : AuthRepository {

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<UserInfo> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val userInfo = auth.currentUserOrNull() ?: throw Exception("User ID not found")

            Result.success(userInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<Boolean> {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return try {
            auth.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isSessionActive(): Boolean {
        val currentSession = auth.currentSessionOrNull()
        return currentSession != null
    }

    override suspend fun sendPasswordRecoveryEmail(email: String): Result<Unit> {
        return try {
            auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            val response = auth.updateUser { password = newPassword }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUserInfo(): Result<UserInfo> {
        return try {
            val user = auth.currentUserOrNull() ?: throw Exception("Cannot find user Session Info")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(context: Context): Result<UserProfileEntityCreate> {
        return try {
            val rawNonce = UUID.randomUUID()
                .toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
            val bytes = rawNonce.toString().toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce =
                digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce to be passed to Google sign-in

            val googleOptions = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                .setNonce(hashedNonce)
                .build()

            val requestId: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleOptions)
                .build()

            try {
                val result = credentialManager.getCredential(
                    request = requestId,
                    context = context
                )

                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }
            } catch (e: Exception) {
                // Here we check if the exception is "No credentials available", if so we show signInGoogleModal
                if (e is NoSuchElementException || e.message?.contains("No credentials available") == true) {
                    val signInWithGoogleOption =
                        GetSignInWithGoogleOption.Builder(BuildConfig.WEB_CLIENT_ID)
                            .setNonce(hashedNonce)
                            .build()

                    val requestSignIn = GetCredentialRequest.Builder()
                        .addCredentialOption(signInWithGoogleOption)
                        .build()

                    val resultSignIn =
                        credentialManager.getCredential(request = requestSignIn, context = context)
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(resultSignIn.credential.data)
                    val googleIdToken = googleIdTokenCredential.idToken

                    auth.signInWith(IDToken) {
                        idToken = googleIdToken
                        provider = Google
                        nonce = rawNonce
                    }
                } else {
                    throw e
                }
            }

            // Obtener los datos del usuario autenticado
            val session = auth.currentSessionOrNull()
            val user = session?.user

            if (user != null) {
                val userId = user.id
                val rawUserMetadata = user.userMetadata

                val json = Json {
                    ignoreUnknownKeys = true
                }

                val userMetadata = rawUserMetadata?.let {
                    json.decodeFromString<UserMetadata>(Json.encodeToString(it))
                }
                val userName = userMetadata?.name ?: "Usuario"

                val userCreateData = UserProfileEntityCreate(
                    id = userId,
                    name = userName,
                )

                Result.success(userCreateData)
            } else {
                Result.failure(Exception("Usuario no encontrado después de iniciar sesión."))
            }
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(e)
        } catch (e: RestException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}