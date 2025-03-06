package com.jacket.digital_mentor.core.utils

import com.jacket.digital_mentor.R

data class AppInfo(val name: String, val packageName: String, val iconRes: Int)

val appList = listOf(
    AppInfo("Google", "com.google.android.googlequicksearchbox", R.drawable.ic_google),
    AppInfo("WhatsApp", "com.whatsapp", R.drawable.ic_whatsapp),
    AppInfo("Facebook", "com.facebook.katana", R.drawable.ic_facebook),
    AppInfo("TikTok", "com.zhiliaoapp.musically", R.drawable.ic_tiktok),
    AppInfo("Instagram", "com.instagram.android", R.drawable.ic_instagram),
    AppInfo("LinkedIn", "com.linkedin.android", R.drawable.ic_linkedin),
    AppInfo("Twitter", "com.twitter.android", R.drawable.ic_x),
    AppInfo("YouTube", "com.google.android.youtube", R.drawable.ic_youtube),
    AppInfo("Gmail", "com.google.android.gm", R.drawable.ic_gmail),
    AppInfo("Outlook", "com.microsoft.office.outlook", R.drawable.ic_outlook)
)