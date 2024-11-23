package com.example.digital_mentor.di

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import com.example.digital_mentor.BuildConfig
import com.example.digital_mentor.data.datasource.remote.MySupabaseClient
import com.example.digital_mentor.domain.repository.AuthRepository
import com.example.digital_mentor.data.repository.AuthRepositoryImpl
import com.example.digital_mentor.data.repository.CategoryRepositoryImpl
import com.example.digital_mentor.data.repository.CourseRepositoryImpl
import com.example.digital_mentor.data.repository.TopicRepositoryImpl
import com.example.digital_mentor.data.repository.UserProfileRepositoryImpl
import com.example.digital_mentor.domain.repository.CategoryRepository
import com.example.digital_mentor.domain.repository.CourseRepository
import com.example.digital_mentor.domain.repository.TopicRepository
import com.example.digital_mentor.domain.repository.UserProfileRepository
import com.example.digital_mentor.domain.usecase.auth.CheckSessionUseCase
import com.example.digital_mentor.domain.usecase.category.GetCategoriesWithQuestionsUseCase
import com.example.digital_mentor.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.example.digital_mentor.domain.usecase.auth.SendPasswordRecoveryEmailUseCase
import com.example.digital_mentor.domain.usecase.userProfile.GetUserProfileUseCase
import com.example.digital_mentor.domain.usecase.auth.SignInUseCase
import com.example.digital_mentor.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.digital_mentor.domain.usecase.auth.SignOutUseCase
import com.example.digital_mentor.domain.usecase.auth.SignUpUseCase
import com.example.digital_mentor.domain.usecase.auth.UpdatePasswordUseCase
import com.example.digital_mentor.domain.usecase.course.GetCoursesUseCase
import com.example.digital_mentor.domain.usecase.topic.GetTopicWithQuestionsUseCase
import com.example.digital_mentor.domain.usecase.userProfile.UpdateUserProfileUseCase
import com.example.digital_mentor.presentation.viewmodel.AppViewModel
import com.example.digital_mentor.presentation.viewmodel.IlliteracyTestViewModel
import com.example.digital_mentor.presentation.viewmodel.LearningGuidesViewModel
import com.example.digital_mentor.presentation.viewmodel.LiveSupportViewModel
import com.example.digital_mentor.presentation.viewmodel.LoginViewModel
import com.example.digital_mentor.presentation.viewmodel.MainLayoutViewModel
import com.example.digital_mentor.presentation.viewmodel.PasswordRecoveryViewModel
import com.example.digital_mentor.presentation.viewmodel.RegisterViewModel
import com.example.digital_mentor.presentation.viewmodel.TestResultViewModel
import com.example.digital_mentor.presentation.viewmodel.UserProfileViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
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
    single<TopicRepository> { TopicRepositoryImpl(get()) }
    single<CourseRepository> { CourseRepositoryImpl(get()) }

    // Auth
    single { SignUpUseCase(get(), get()) }
    single { SignInUseCase(get()) }
    single { SignOutUseCase(get()) }
    single { SignInWithGoogleUseCase(get()) }
    single { CheckSessionUseCase(get()) }
    single { GetCurrentUserInfoUseCase(get()) }
    single { SendPasswordRecoveryEmailUseCase(get()) }
    single { UpdatePasswordUseCase(get()) }
    // Category
    single { GetCategoriesWithQuestionsUseCase(get()) }
    // UserProfile
    single { UpdateUserProfileUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    // Topic
    single { GetTopicWithQuestionsUseCase(get()) }
    // Courses
    single { GetCoursesUseCase(get()) }

    viewModel { AppViewModel(get()) }
    viewModel { LiveSupportViewModel(get()) }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { IlliteracyTestViewModel(get(), get(), get()) }
    viewModel { MainLayoutViewModel(get()) }
    viewModel { LearningGuidesViewModel(get()) }
    viewModel { TestResultViewModel(get(), get()) }
    viewModel { UserProfileViewModel(get(), get(), get()) }
    viewModel { PasswordRecoveryViewModel(get(), get()) }
}