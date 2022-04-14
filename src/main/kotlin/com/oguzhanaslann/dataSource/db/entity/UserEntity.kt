package com.oguzhanaslann.dataSource.db.entity

abstract class Entity {
    companion object
}


data class UserEntity(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String
):  Entity() {
    companion object
}