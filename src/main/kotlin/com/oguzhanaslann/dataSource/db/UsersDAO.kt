package com.oguzhanaslann.dataSource.db

import com.oguzhanaslann.util.SecurityManager
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.component.getScopeName
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


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

    suspend fun logTable() {
        newSuspendedTransaction {
            val whole = Users.selectAll()

            whole.forEach {
                println("pass : ${it[Users.password]}")

            }

        }
    }

    protected abstract suspend fun getUsersWithEmail(email: String): Long
    protected abstract suspend fun getUsersWithPassword(email: String, password: String): Long
    protected abstract suspend fun insertNewUserWith(email: String, password: String): Int
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
}