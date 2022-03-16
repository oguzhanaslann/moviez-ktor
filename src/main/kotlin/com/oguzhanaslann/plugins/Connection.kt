package com.oguzhanaslann.plugins

import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig

fun Application.configureDatabaseConnection() {
    Database.connect(
        url = "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver",
        databaseConfig = DatabaseConfig {
            useNestedTransactions = true
        }
    )
}
