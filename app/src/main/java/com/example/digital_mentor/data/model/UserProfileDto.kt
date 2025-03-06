package com.jacket.digital_mentor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileEntity(
    val id: String,
    val name: String,
    @SerialName("phone_number") val phoneNumber: String?,
    val card: String?,
    val cvv: String?,
    val expirationDate: String?,
    @SerialName("cumulative_score") val cumulativeScore: Int?,
    @SerialName("illiteracy_level") val illiteracyLevel: String?,
    @SerialName("last_test_date") val lastTestDate: String?,
    @SerialName("created_at") val createdAt: String?
)

@Serializable
data class UserProfileEntityCreate(
    val id: String,
    val name: String,
    @SerialName("phone_number") val phoneNumber: String? = null,
)

@Serializable
data class UserProfileEntityUpdate(
    val name: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    val card: String? = null,
    val cvv: String? = null,
    val expirationDate: String? = null,
    @SerialName("cumulative_score") val cumulativeScore: Int? = null,
    @SerialName("illiteracy_level") val illiteracyLevel: String? = null,
    @SerialName("last_test_date") val lastTestDate: String? = null
)