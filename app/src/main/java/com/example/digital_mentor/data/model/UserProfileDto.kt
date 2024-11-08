package com.example.digital_mentor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileEntity(
    val id: String,
    val name: String,
    val card: String,
    val cumulativeScore: Int,
    val illiteracyLevel: String,
    val lastTestDate: String,
    val createdAt: String
)

@Serializable
data class UserProfileEntityCreate(
    val id: String,
    val name: String,
    val card: String,
    val cumulativeScore: Int = 0,
    val illiteracyLevel: String? = null,
    val lastTestDate: String? = null
)

@Serializable
data class UserProfileEntityUpdate(
    val name: String? = null,
    val card: String? = null,
    val cumulativeScore: Int? = null,
    val illiteracyLevel: String? = null,
    val lastTestDate: String? = null
)