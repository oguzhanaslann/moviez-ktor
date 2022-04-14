package com.oguzhanaslann.domainModel

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String
)