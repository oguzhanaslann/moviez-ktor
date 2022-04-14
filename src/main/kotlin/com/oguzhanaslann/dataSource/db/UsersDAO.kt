package com.oguzhanaslann.dataSource.db

import com.oguzhanaslann.dataSource.db.entity.UserEntity
import com.oguzhanaslann.util.SecurityManager
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update


abstract class DAO {
    protected suspend fun <T> runSafeDBOperation(
        suspendBlock: suspend () -> T
    ): Result<T> {
        return runCatching {
            suspendBlock()
        }
    }
}

abstract class UsersDAO : DAO() {
    suspend fun getIsUserWithEmailExists(email: String): Result<Boolean> {
        return runSafeDBOperation {
            val userCount = getUsersWithEmail(email)
            userCount == 1L
        }
    }

    suspend fun isPasswordCorrect(email: String, password: String): Result<Boolean> {
        return runSafeDBOperation {
            val userCount = getUsersWithPassword(email, password)
            userCount == 1L
        }
    }

    suspend fun registerUser(email: String, password: String): Result<Boolean> {
        return runSafeDBOperation {
            val insertCount = insertNewUserWith(email, password)
            insertCount == 1
        }
    }

    suspend fun getIsUserWithIdExists(id: Int): Result<Boolean> {
        return runSafeDBOperation {
            val users = getUsersWithId(id)
            users == 1L
        }
    }

    suspend fun updateUser(userEntity: UserEntity): Result<Boolean> {
        return runSafeDBOperation {
            val updatedRow = updateUserWith(userEntity)
            updatedRow == 1L
        }
    }

    protected abstract suspend fun getUsersWithEmail(email: String): Long
    protected abstract suspend fun getUsersWithPassword(email: String, password: String): Long
    protected abstract suspend fun insertNewUserWith(email: String, password: String): Int
    protected abstract suspend fun getUsersWithId(id: Int): Long
    protected abstract suspend fun updateUserWith(userEntity: UserEntity): Long
}

class UserDatabaseOperationsHandler(
    private val securityManager: SecurityManager
) : UsersDAO() {
    override suspend fun getUsersWithEmail(email: String): Long {
        val users = newSuspendedTransaction(Dispatchers.IO) {
            val users = Users.select {
                Users.email eq email
            }

            users.count()
        }

        return users
    }

    override suspend fun getUsersWithPassword(email: String, password: String): Long {
        val users = newSuspendedTransaction {
            val user = Users.select {
                (Users.email eq email) and (Users.password eq securityManager.hash(password))
            }

            user.count()
        }
        return users
    }

    override suspend fun insertNewUserWith(email: String, password: String): Int {
        val insertStatement = newSuspendedTransaction {
            Users.insert {
                it[Users.email] = email
                it[Users.password] = securityManager.hash(password)
            }
        }

        return insertStatement.insertedCount
    }

    override suspend fun getUsersWithId(id: Int): Long {
        val userCount = newSuspendedTransaction {
            val users = Users.select {
                (Users.id eq id)
            }

            users.count()
        }

        return userCount
    }

    override suspend fun updateUserWith(userEntity: UserEntity): Long {
        val updateRows = newSuspendedTransaction {
            val transaction = this
            val isExistingUserResult = getIsUserWithEmailExists(userEntity.email)

            if (isExistingUserResult.isFailure) {
                transaction.close()
            }

            val isExistingUser = isExistingUserResult.getOrDefault(true)

            if (isExistingUser) {
                transaction.close()
            }

            val updateRows = Users.update(
                where = {
                    Users.id eq userEntity.id
                },
                body = {
                    it[email] = userEntity.email
                    it[firstName] = userEntity.firstName
                    it[lastName] = userEntity.lastName
                }
            )

            updateRows
        }

        return updateRows.toLong()
    }
}