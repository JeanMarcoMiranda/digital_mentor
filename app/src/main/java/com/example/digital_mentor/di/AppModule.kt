package com.example.digital_mentor.di

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import com.example.digital_mentor.BuildConfig
import com.example.digital_mentor.data.datasource.remote.MySupabaseClient
import com.example.digital_mentor.domain.repository.AuthRepository
import com.example.digital_mentor.data.repository.AuthRepositoryImpl
import com.example.digital_mentor.data.repository.CategoryRepositoryImpl
import com.example.digital_mentor.data.repository.UserProfileRepositoryImpl
import com.example.digital_mentor.domain.repository.CategoryRepository
import com.example.digital_mentor.domain.repository.UserProfileRepository
import com.example.digital_mentor.domain.usecase.CheckSessionUseCase
import com.example.digital_mentor.domain.usecase.GetCategoriesWithQuestionsUseCase
import com.example.digital_mentor.domain.usecase.GetCurrentUserInfoUseCase
import com.example.digital_mentor.domain.usecase.GetUserProfileUseCase
import com.example.digital_mentor.domain.usecase.SignInUseCase
import com.example.digital_mentor.domain.usecase.SignInWithGoogleUseCase
import com.example.digital_mentor.domain.usecase.SignOutUseCase
import com.example.digital_mentor.domain.usecase.SignUpUseCase
import com.example.digital_mentor.domain.usecase.UpdateUserProfileUseCase
import com.example.digital_mentor.presentation.viewmodel.AppViewModel
import com.example.digital_mentor.presentation.viewmodel.IlliteracyTestViewModel
import com.example.digital_mentor.presentation.viewmodel.LiveSupportViewModel
import com.example.digital_mentor.presentation.viewmodel.LoginViewModel
import com.example.digital_mentor.presentation.viewmodel.MainLayoutViewModel
import com.example.digital_mentor.presentation.viewmodel.RegisterViewModel
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


@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {
    single<SupabaseClient> {
        MySupabaseClient.client
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
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

    }
    single<Auth> {
        get<SupabaseClient>().auth
    }
    single { CredentialManager.create(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<UserProfileRepository> { UserProfileRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }

    single { SignUpUseCase(get(), get()) }
    single { SignInUseCase(get()) }
    single { SignOutUseCase(get()) }
    single { SignInWithGoogleUseCase(get()) }
    single { CheckSessionUseCase(get()) }
    single { GetCategoriesWithQuestionsUseCase(get()) }
    single { UpdateUserProfileUseCase(get()) }
    single { GetCurrentUserInfoUseCase(get()) }
    single { GetUserProfileUseCase(get()) }

    viewModel { AppViewModel(get()) }
    viewModel { LiveSupportViewModel() }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { IlliteracyTestViewModel(get(), get(), get()) }
    viewModel { MainLayoutViewModel(get()) }
}