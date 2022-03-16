package com.oguzhanaslann

import com.oguzhanaslann.dataSource.db.Movies
import com.oguzhanaslann.plugins.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 *  auto-reload : ./gradlew -t build (exclude tests "-x test -i")
 * */

@Serializable
data class Movie(
    val id :String,
    val name : String
)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureDatabaseConnection()
    configureDependencyInjection()
    configureSecurity()
    configureRouting()
    configureSerialization()
    configureTemplating()
    configureMonitoring()
    configureHTTP()
}



