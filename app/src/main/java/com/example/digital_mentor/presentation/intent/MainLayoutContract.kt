package com.jacket.digital_mentor.presentation.intent

import androidx.compose.material3.DrawerValue

sealed class MainLayoutState {
    data class Success(val message: String): MainLayoutState()
    data class Error(val error: String): MainLayoutState()
    data class Drawer(val isOpen: DrawerValue = DrawerValue.Closed): MainLayoutState()
}

sealed class MainLayoutIntent {
    object ToggleDrawer : MainLayoutIntent()
    object LogoutClicked: MainLayoutIntent()
}

