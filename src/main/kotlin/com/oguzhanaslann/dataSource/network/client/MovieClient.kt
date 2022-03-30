package com.oguzhanaslann.dataSource.network.client

import com.oguzhanaslann.dataSource.network.client.plugins.configureJsonFeature
import com.oguzhanaslann.dataSource.network.client.plugins.configureLogging
import com.oguzhanaslann.dataSource.network.client.plugins.configureTimeouts
import com.oguzhanaslann.dataSource.network.dto.MovieDTO
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

class MovieClient {

    private val ktorClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        HttpClient(CIO) {
            configureLogging()
            configureJsonFeature()
            configureTimeouts()
        }
    }

    suspend fun getUsers(): MovieDTO {

        return ktorClient.get(
            urlString = "$BASE_URL/?apikey=d110b23a&t=e"
        )
    }


    companion object {
        const val BASE_URL = "https://www.omdbapi.com/"

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