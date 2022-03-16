package com.oguzhanaslann.dataSource.network.client

import com.oguzhanaslann.dataSource.network.client.plugins.configureJsonFeature
import com.oguzhanaslann.dataSource.network.client.plugins.configureLogging
import com.oguzhanaslann.dataSource.network.client.plugins.configureTimeouts
import io.ktor.client.*
import io.ktor.client.engine.cio.*

class MovieClient {

    private val ktorClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        HttpClient(CIO) {
            configureLogging()
            configureJsonFeature()
            configureTimeouts()
        }
    }

    companion object {

        @Volatile
        private var instance: MovieClient? = null

        @Synchronized
        fun getInstance(): MovieClient {
            return instance ?: synchronized(this) {
                instance ?: MovieClient()
                    .also {
                        instance = it
                    }
            }
        }
    }
}
