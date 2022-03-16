package com.oguzhanaslann.dataSource.network.client.plugins

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.features.logging.*

fun HttpClientConfig<CIOEngineConfig>.configureLogging() {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
}
