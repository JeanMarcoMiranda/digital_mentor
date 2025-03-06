package com.jacket.digital_mentor.presentation.intent

import com.jacket.digital_mentor.data.model.UserProfileEntity

sealed class UserProfileState {
    object Loading : UserProfileState()

    data class UserData(
        val userProfile: UserProfileEntity?,
        val email: String?,
        val cardNumber: String? = null,
        val cardError: String? = null,
        val expirationDate: String? = null,
        val expirationDateError: String? = null,
        val cvv: String? = null,
        val cvvError: String? = null,
        val isEditSheetOpen: Boolean = false,
        val successMessage: String? = null,
        val errorMessage: String? = null
    ) : UserProfileState()
}

sealed class UserProfileIntent {
    object OpenEditCard : UserProfileIntent()
    object CloseEditCard : UserProfileIntent()
    data class UpdateCardDetails(
        val cardNumber: String?,
        val expirationDate: String?,
        val cvv: String?
    ) : UserProfileIntent()

    object SaveCardDetails : UserProfileIntent()
}