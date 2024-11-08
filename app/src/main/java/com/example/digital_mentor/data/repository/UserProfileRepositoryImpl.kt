package com.example.digital_mentor.data.repository

import android.util.Log
import com.example.digital_mentor.data.model.UserProfileEntity
import com.example.digital_mentor.data.model.UserProfileEntityCreate
import com.example.digital_mentor.data.model.UserProfileEntityUpdate
import com.example.digital_mentor.domain.repository.UserProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UserProfileRepositoryImpl(
    private val supabase: SupabaseClient
) : UserProfileRepository {
    override suspend fun getUserProfile(): Result<UserProfileEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserProfile(userData: UserProfileEntityCreate): Result<UserProfileEntity> {
        return try {
            val user: UserProfileEntity = supabase.from("user_profiles")
                .insert(userData).decodeSingle()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(
        userId: String,
        userData: UserProfileEntityUpdate
    ): Result<UserProfileEntity> {
        return try {
            Log.d("UpdateUser", "Here")
            val updatedUserProfile: UserProfileEntity =
                supabase.from("user_profiles").update(userData) {
                    select()
                    filter {
                        UserProfileEntity::id eq userId
                    }
                }.decodeSingle()

            Log.d("UpdateUser", "Here2")
            Result.success(updatedUserProfile)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

//    override suspend fun updateUserProfile(userId: String, userData: UserProfileEntityUpdate): Result<UserProfileEntity> {
//        return try {
//            Log.d("UpdateUser", "Here")
//            val updatedUserProfile: UserProfileEntity = supabase.from("user_profiles").update(
//                {
//                    UserProfileEntity::name setTo userData.name
//                    UserProfileEntity::card setTo userData.card
//                    UserProfileEntity::cumulativeScore setTo userData.cumulativeScore
//                    UserProfileEntity::illiteracyLevel setTo userData.illiteracyLevel
//                    UserProfileEntity::lastTestDate setTo userData.lastTestDate
//                }
//            ) {
//                filter {
//                    UserProfileEntity::id eq userId
//                }
//            }.decodeSingle()
//
//            Log.d("UpdateUser", "Here2")
//            Result.success(updatedUserProfile)
//        } catch (e: Exception) {
//            return Result.failure(e)
//        }
//
//    }

//    override suspend fun updateUserProfile(userId: String, userData: UserProfileEntityUpdate): Result<UserProfileEntity> {
//        return try {
//            Log.d("UpdateUser", "Attempting to update user profile for userId: $userId")
//
//            // Crear un mapa con solo los campos no nulos
//            val updateMap = mutableMapOf<String, Any?>()
//
//            userData.name?.let { updateMap["name"] = it }
//            userData.card?.let { updateMap["card"] = it }
//            userData.cumulativeScore?.let { updateMap["cumulative_score"] = it }
//            userData.illiteracyLevel?.let { updateMap["illiteracy_level"] = it }
//            userData.lastTestDate?.let { updateMap["last_test_date"] = it }
//
//            // Verificar que haya campos para actualizar
//            if (updateMap.isEmpty()) {
//                Log.w("UpdateUser", "No fields to update for userId: $userId")
//                return Result.failure(IllegalArgumentException("No fields to update"))
//            }
//
//            // Ejecutar la actualización con los campos no nulos
//            val updatedUserProfile: UserProfileEntity = supabase.from("user_profiles")
//                .update(updateMap) {
//                    filter { UserProfileEntity::id eq userId }
//                }
//                .decodeSingle()
//
//            Log.d("UpdateUser", "Update successful for userId: $userId")
//            Result.success(updatedUserProfile)
//        } catch (e: Exception) {
//            Log.e("UpdateUser", "Update failed for userId: $userId", e)
//            Result.failure(e)
//        }
//    }
}