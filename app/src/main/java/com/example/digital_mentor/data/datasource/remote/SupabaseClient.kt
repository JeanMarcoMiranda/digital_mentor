package com.jacket.digital_mentor.data.datasource.remote

import com.jacket.digital_mentor.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object MySupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        install(Auth) {
            host = "resetpassword"
            scheme = "digitalmentor"

            defaultExternalAuthAction = ExternalAuthAction.CustomTabs()
        }
    }
}