package com.example.digital_mentor.core.di

import com.example.digital_mentor.data.repository.AuthRepository
import com.example.digital_mentor.data.repository.AuthRepositoryImpl
import com.example.digital_mentor.domain.usecase.SignInUseCase
import com.example.digital_mentor.domain.usecase.SignUpUseCase
import com.example.digital_mentor.presentation.login.LoginViewModel
import com.example.digital_mentor.presentation.register.RegisterViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val SUPABASE_URL = "https://cutgkatnwrnpbgljpect.supabase.co"
private const val SUPABASE_KEY =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN1dGdrYXRud3JucGJnbGpwZWN0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjk2NTE1MjQsImV4cCI6MjA0NTIyNzUyNH0.PjfjiYEpQDUC1Q8KXXyc0Ho_kvg5tqgaB7STmp6mQbY"

val appModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth)
        }
    }

    single<Auth> {
        get<SupabaseClient>().auth
    }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { SignUpUseCase(get()) }
    single { SignInUseCase(get()) }

    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }

}