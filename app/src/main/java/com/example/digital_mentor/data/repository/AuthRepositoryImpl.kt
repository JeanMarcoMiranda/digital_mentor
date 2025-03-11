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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import java.util.UUID
import kotlin.math.log

@Serializable
data class UserMetadata(
    @SerialName("name") val name: String? = null
)

//class AuthRepositoryImpl(
//    private val auth: Auth,
//    private val googleIdOption: GetGoogleIdOption,
//    private val credentialManager: CredentialManager
//) : AuthRepository {
//
//    override suspend fun signUpWithEmail(
//        email: String,
//        password: String
//    ): Result<UserInfo> {
//        return try {
//            auth.signUpWith(Email) {
//                this.email = email
//                this.password = password
//            }
//
//            val userInfo = auth.currentUserOrNull() ?: throw Exception("User ID not found")
//
//            Result.success(userInfo)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun signInWithEmail(email: String, password: String): Result<Boolean> {
//        return try {
//            auth.signInWith(Email) {
//                this.email = email
//                this.password = password
//            }
//            Result.success(true)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun signOut(): Result<Boolean> {
//        return try {
//            auth.signOut()
//            Result.success(true)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun isSessionActive(): Boolean {
//        val currentSession = auth.currentSessionOrNull()
//        return currentSession != null
//    }
//
//    override suspend fun sendPasswordRecoveryEmail(email: String): Result<Unit> {
//        Log.d("SendPasswordRecoveryEmail: ", "email to recover: " + email)
//        return try {
//            auth.resetPasswordForEmail(email)
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Log.d("SendPasswordRecoveryEmail: ", "this is the error: " + e)
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun updatePassword(newPassword: String): Result<Unit> {
//        return try {
//            val response = auth.updateUser { password = newPassword }
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun getCurrentUserInfo(): Result<UserInfo> {
//        return try {
//            val user = auth.currentUserOrNull() ?: throw Exception("Cannot find user Session Info")
//            Result.success(user)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun signInWithGoogle(context: Context): Result<UserProfileEntityCreate> {
//        return try {
//            val rawNonce = UUID.randomUUID()
//                .toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
//            val bytes = rawNonce.toString().toByteArray()
//            val md = MessageDigest.getInstance("SHA-256")
//            val digest = md.digest(bytes)
//            val hashedNonce =
//                digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce to be passed to Google sign-in
//
//            val googleOptions = GetGoogleIdOption.Builder()
//                .setFilterByAuthorizedAccounts(true)
//                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
//                .setNonce(hashedNonce)
//                .build()
//
//            val requestId: GetCredentialRequest = GetCredentialRequest.Builder()
//                .addCredentialOption(googleOptions)
//                .build()
//
//            try {
//                val result = credentialManager.getCredential(
//                    request = requestId,
//                    context = context
//                )
//
//                val googleIdTokenCredential =
//                    GoogleIdTokenCredential.createFrom(result.credential.data)
//                val googleIdToken = googleIdTokenCredential.idToken
//
//                auth.signInWith(IDToken) {
//                    idToken = googleIdToken
//                    provider = Google
//                    nonce = rawNonce
//                }
//            } catch (e: Exception) {
//                // Here we check if the exception is "No credentials available", if so we show signInGoogleModal
//                if (e is NoSuchElementException || e.message?.contains("No credentials available") == true) {
//                    val signInWithGoogleOption =
//                        GetSignInWithGoogleOption.Builder(BuildConfig.WEB_CLIENT_ID)
//                            .setNonce(hashedNonce)
//                            .build()
//
//                    val requestSignIn = GetCredentialRequest.Builder()
//                        .addCredentialOption(signInWithGoogleOption)
//                        .build()
//
//                    val resultSignIn =
//                        credentialManager.getCredential(request = requestSignIn, context = context)
//                    val googleIdTokenCredential =
//                        GoogleIdTokenCredential.createFrom(resultSignIn.credential.data)
//                    val googleIdToken = googleIdTokenCredential.idToken
//
//                    auth.signInWith(IDToken) {
//                        idToken = googleIdToken
//                        provider = Google
//                        nonce = rawNonce
//                    }
//                } else {
//                    throw e
//                }
//            }
//
//            // Obtener los datos del usuario autenticado
//            val session = auth.currentSessionOrNull()
//            val user = session?.user
//
//            if (user != null) {
//                val userId = user.id
//                val rawUserMetadata = user.userMetadata
//
//                val json = Json {
//                    ignoreUnknownKeys = true
//                }
//
//                val userMetadata = rawUserMetadata?.let {
//                    json.decodeFromString<UserMetadata>(Json.encodeToString(it))
//                }
//                val userName = userMetadata?.name ?: "Usuario"
//
//                val userCreateData = UserProfileEntityCreate(
//                    id = userId,
//                    name = userName,
//                )
//
//                Result.success(userCreateData)
//            } else {
//                Result.failure(Exception("Usuario no encontrado después de iniciar sesión."))
//            }
//        } catch (e: GoogleIdTokenParsingException) {
//            Result.failure(e)
//        } catch (e: RestException) {
//            Result.failure(e)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}

private const val TAG = "AuthRepository"

class AuthRepositoryImpl(
    private val auth: Auth,
    private val credentialManager: CredentialManager
) : AuthRepository {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<UserInfo> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando registro con email: $email")
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val userInfo = auth.currentUserOrNull()
                ?: return@withContext Result.failure(Exception("No se pudo obtener la información del usuario tras el registro"))

            Log.d(TAG, "Registro exitoso para: $email")
            Result.success(userInfo)
        } catch (e: Exception) {
            Log.e(TAG, "Error en signUpWithEmail: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando sesión con email: $email")
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d(TAG, "Inicio de sesión exitoso para: $email")
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error en signInWithEmail: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Cerrando sesión de usuario")
            auth.signOut()
            Log.d(TAG, "Sesión cerrada exitosamente")
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error al cerrar sesión: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun isSessionActive(): Boolean = withContext(Dispatchers.IO) {
        val currentSession = auth.currentSessionOrNull()
        val isActive = currentSession != null
        Log.d(TAG, "Verificación de sesión activa: $isActive")
        isActive
    }

    override suspend fun sendPasswordRecoveryEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Enviando email de recuperación a: $email")
            auth.resetPasswordForEmail(email)
            Log.d(TAG, "Email de recuperación enviado exitosamente a: $email")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error al enviar email de recuperación: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Actualizando contraseña de usuario")
            auth.updateUser { password = newPassword }
            Log.d(TAG, "Contraseña actualizada exitosamente")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar contraseña: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUserInfo(): Result<UserInfo> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Obteniendo información del usuario actual")
            val user = auth.currentUserOrNull()
                ?: return@withContext Result.failure(Exception("No se pudo encontrar la sesión del usuario"))

            Log.d(TAG, "Información del usuario obtenida correctamente, ID: ${user.id}")
            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener información del usuario: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(context: Context): Result<UserProfileEntityCreate> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando proceso de login con Google")

            // Generar nonce para seguridad
            val rawNonce = UUID.randomUUID().toString()
            val hashedNonce = generateHashedNonce(rawNonce)
            Log.d(TAG, "Nonce generado correctamente")

            // Intentar con cuentas autorizadas primero
            return@withContext try {
                val result = signInWithAuthorizedAccounts(context, hashedNonce, rawNonce)
                Log.d(TAG, "Login exitoso usando cuenta autorizada de Google")
                extractUserProfile()
            } catch (e: Exception) {
                Log.d(TAG, "No hay cuentas autorizadas disponibles, mostrando selector: ${e.message}")

                if (isNoCredentialsError(e)) {
                    val result = signInWithGooglePrompt(context, hashedNonce, rawNonce)
                    Log.d(TAG, "Login exitoso usando selector de Google")
                    extractUserProfile()
                } else {
                    Log.e(TAG, "Error en flujo de autenticación con Google: ${e.message}", e)
                    throw e
                }
            }
        } catch (e: GoogleIdTokenParsingException) {
            Log.e(TAG, "Error al analizar token de Google: ${e.message}", e)
            Result.failure(e)
        } catch (e: RestException) {
            Log.e(TAG, "Error de Supabase en autenticación con Google: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error general en autenticación con Google: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun generateHashedNonce(rawNonce: String): String {
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private suspend fun signInWithAuthorizedAccounts(
        context: Context,
        hashedNonce: String,
        rawNonce: String
    ) {
        val googleOptions = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

        Log.d(TAG, "Usando WebClientID: ${BuildConfig.WEB_CLIENT_ID}")

        val requestId = GetCredentialRequest.Builder()
            .addCredentialOption(googleOptions)
            .build()

        val result = credentialManager.getCredential(
            request = requestId,
            context = context
        )

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
        val googleIdToken = googleIdTokenCredential.idToken

        Log.d(TAG, "Token de Google obtenido correctamente (versión autorizada)")

        auth.signInWith(IDToken) {
            idToken = googleIdToken
            provider = Google
            nonce = rawNonce
        }
    }

    private suspend fun signInWithGooglePrompt(
        context: Context,
        hashedNonce: String,
        rawNonce: String
    ) {
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(BuildConfig.WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

        val requestSignIn = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val resultSignIn = credentialManager.getCredential(
            request = requestSignIn,
            context = context
        )

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(resultSignIn.credential.data)
        val googleIdToken = googleIdTokenCredential.idToken

        Log.d(TAG, "Token de Google obtenido correctamente (versión prompt)")

        auth.signInWith(IDToken) {
            idToken = googleIdToken
            provider = Google
            nonce = rawNonce
        }
    }

    private fun isNoCredentialsError(e: Exception): Boolean {
        // Ampliar para capturar más tipos de errores relacionados
        return e is NoSuchElementException ||
                e is androidx.credentials.exceptions.GetCredentialCancellationException ||
                e.message?.contains("No credentials available") == true ||
                e.message?.contains("Account reauth failed") == true
    }

    private fun extractUserProfile(): Result<UserProfileEntityCreate> {
        val session = auth.currentSessionOrNull()
        val user = session?.user
            ?: return Result.failure(Exception("Usuario no encontrado después de iniciar sesión"))

        Log.d(TAG, "Sesión obtenida correctamente, extrayendo metadatos del usuario")

        val userId = user.id
        val rawUserMetadata = user.userMetadata

        val userMetadata = rawUserMetadata?.let {
            try {
                json.decodeFromString<UserMetadata>(Json.encodeToString(it))
            } catch (e: Exception) {
                Log.e(TAG, "Error al decodificar metadatos: ${e.message}", e)
                null
            }
        }

        val userName = userMetadata?.name ?: "Usuario"
        Log.d(TAG, "Perfil de usuario creado con ID: $userId, Nombre: $userName")

        val userCreateData = UserProfileEntityCreate(
            id = userId,
            name = userName,
        )

        return Result.success(userCreateData)
    }
}