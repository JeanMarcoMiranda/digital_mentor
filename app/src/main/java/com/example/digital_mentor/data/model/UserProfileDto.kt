package com.example.digital_mentor.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileEntity(
    val id: String,
    val name: String,
    val card: String,
    @SerialName("cumulative_score") val cumulativeScore: Int?,
    @SerialName("illiteracy_level") val illiteracyLevel: String?,
    @SerialName("last_test_date") val lastTestDate: String?,
    @SerialName("created_at") val createdAt: String?
)

@Serializable
data class UserProfileEntityCreate(
    val id: String,
    val name: String,
    val card: String,
    @SerialName("cumulative_score") val cumulativeScore: Int = 0,
    @SerialName("illiteracy_level") val illiteracyLevel: String? = null,
    @SerialName("last_test_date") val lastTestDate: String? = null
)

@Serializable
data class UserProfileEntityUpdate(
    val name: String? = null,
    val card: String? = null,
    @SerialName("cumulative_score") val cumulativeScore: Int? = null,
    @SerialName("illiteracy_level") val illiteracyLevel: String? = null,
    @SerialName("last_test_date") val lastTestDate: String? = null
)