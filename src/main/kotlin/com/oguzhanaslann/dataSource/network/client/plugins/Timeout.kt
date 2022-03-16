package com.oguzhanaslann.dataSource.network.client.plugins

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.features.*

fun HttpClientConfig<CIOEngineConfig>.configureTimeouts() {
    val thirtySecondsInMillis: Long = 30000
    install(HttpTimeout) {
        requestTimeoutMillis = thirtySecondsInMillis
    }
}
