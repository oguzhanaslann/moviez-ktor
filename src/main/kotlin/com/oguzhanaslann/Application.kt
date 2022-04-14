package com.oguzhanaslann

import com.oguzhanaslann.plugins.*
import com.oguzhanaslann.plugins.routing.configureRouting
import io.ktor.application.*
import kotlinx.serialization.Serializable

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
    configureDatabase()
    configureDependencyInjection()
    configureSecurity()
    configureRouting()
    configureSerialization()
    configureTemplating()
    configureMonitoring()
    configureHTTP()
}
