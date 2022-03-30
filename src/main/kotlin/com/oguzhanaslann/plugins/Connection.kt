package com.oguzhanaslann.plugins

import com.oguzhanaslann.dataSource.db.Users
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.configureDatabase() {
    configureDatabaseConnection()
    createDatabaseTables()
}

fun Application.configureDatabaseConnection() {
    Database.connect(
        url = "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver",
        databaseConfig = DatabaseConfig {
            useNestedTransactions = true
        }
    )
}

fun Application.createDatabaseTables() {
    transaction {
        SchemaUtils.create(Users)
    }
}
