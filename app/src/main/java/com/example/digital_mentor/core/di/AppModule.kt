package com.example.digital_mentor.core.di

import androidx.credentials.CredentialManager
import com.example.digital_mentor.BuildConfig
import com.example.digital_mentor.data.repository.AuthRepository
import com.example.digital_mentor.data.repository.AuthRepositoryImpl
import com.example.digital_mentor.domain.usecase.SignInUseCase
import com.example.digital_mentor.domain.usecase.SignInWithGoogleUseCase
import com.example.digital_mentor.domain.usecase.SignUpUseCase
import com.example.digital_mentor.presentation.login.LoginViewModel
import com.example.digital_mentor.presentation.register.RegisterViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.security.MessageDigest
import java.util.UUID


val appModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
            install(Auth)
        }
    }

    single<GetGoogleIdOption> {
        val rawNonce = UUID.randomUUID()
            .toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
        val bytes = rawNonce.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce =
            digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce to be passed to Google sign-in

        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

    }

    single<Auth> {
        get<SupabaseClient>().auth
    }
    single { CredentialManager.create(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single { SignUpUseCase(get()) }
    single { SignInUseCase(get()) }
    single { SignInWithGoogleUseCase(get()) }

    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
}