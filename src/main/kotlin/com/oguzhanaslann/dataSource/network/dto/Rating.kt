package com.oguzhanaslann.dataSource.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    @SerialName("Source")
    val source: String? = null,
    @SerialName("Value")
    val value: String? = null
)