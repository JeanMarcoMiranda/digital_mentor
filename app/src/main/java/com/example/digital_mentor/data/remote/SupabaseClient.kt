package com.example.digital_mentor.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://cutgkatnwrnpbgljpect.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN1dGdrYXRud3JucGJnbGpwZWN0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjk2NTE1MjQsImV4cCI6MjA0NTIyNzUyNH0.PjfjiYEpQDUC1Q8KXXyc0Ho_kvg5tqgaB7STmp6mQbY"
    ) {
        install(Auth)
    }
}