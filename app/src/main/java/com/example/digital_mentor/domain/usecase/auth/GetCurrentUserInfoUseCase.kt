package com.jacket.digital_mentor.domain.usecase.auth

import com.jacket.digital_mentor.domain.repository.AuthRepository
import io.github.jan.supabase.auth.user.UserInfo

class GetCurrentUserInfoUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserInfo> = authRepository.getCurrentUserInfo()
}