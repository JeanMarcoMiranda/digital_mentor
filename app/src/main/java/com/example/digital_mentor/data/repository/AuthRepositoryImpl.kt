package com.example.digital_mentor.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.RestException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepositoryImpl(
    private val auth: Auth,
    private val googleIdOption: GetGoogleIdOption,
    private val credentialManager: CredentialManager
) : AuthRepository {

    override suspend fun signUpWithEmail(
        name: String,
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
//                data = buildJsonObject {
//                    put("name", name)
//                }
            }
            Result.success(true)
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


    override suspend fun signInWithGoogle(context: Context): Result<Boolean> {
        return try {
            val request:GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val googleIdToken = googleIdTokenCredential.idToken

//            auth.signInWith(IDToken) {
//                idToken = googleIdToken
//                provider = Google
//            }

            Result.success(true)
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(e)
        } catch (e: RestException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}