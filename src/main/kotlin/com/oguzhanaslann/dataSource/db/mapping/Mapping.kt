package com.oguzhanaslann.dataSource.db.mapping

import com.oguzhanaslann.dataSource.db.entity.UserEntity
import com.oguzhanaslann.domainModel.User

fun UserEntity.mapToDomainModel(): User {
    return User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName
    )
}

fun User.mapToEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName
    )
}