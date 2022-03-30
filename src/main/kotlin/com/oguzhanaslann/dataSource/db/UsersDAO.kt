package com.oguzhanaslann.dataSource.db

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction

interface UsersDAO {
    suspend fun getIsUserWithEmailExists(email: String): Boolean
    suspend fun isPasswordCorrect(email: String, password: String): Boolean
    suspend fun registerUser(email: String, password: String)
}

class UserDatabaseOperationsHandler(private val coroutineScope: CoroutineScope) : UsersDAO {
    override suspend fun getIsUserWithEmailExists(email: String): Boolean {
        val users = newSuspendedTransaction(Dispatchers.IO) {
            val users = Users.select {
                Users.email eq email
            }

            users.count()
        }

        return users > 1
    }

    override suspend fun isPasswordCorrect(email: String, password: String): Boolean {
        val isUserExists  = newSuspendedTransaction {
            val user = Users.select {
                (Users.email eq email) and (Users.password eq password)
            }

            user.count() == 1L
        }
        return isUserExists
    }

    override suspend fun registerUser(email: String, password: String) {
        newSuspendedTransaction {
           val x  = Users.insert {
                it[Users.email] = email
                it[Users.password] = password
            }

            print("x : :  : ${x.insertedCount} ")
        }
    }
}
