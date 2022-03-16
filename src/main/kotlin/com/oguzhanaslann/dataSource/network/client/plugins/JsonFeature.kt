package com.oguzhanaslann.dataSource.network.client.plugins

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

fun HttpClientConfig<CIOEngineConfig>.configureJsonFeature() {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}
