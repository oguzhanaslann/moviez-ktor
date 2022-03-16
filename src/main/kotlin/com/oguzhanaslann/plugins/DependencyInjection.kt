package com.oguzhanaslann.plugins

import com.oguzhanaslann.di.coroutineScope
import com.oguzhanaslann.di.networkModule
import io.ktor.application.*
import kotlinx.coroutines.CoroutineScope
import org.koin.ktor.ext.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        coroutineScope.single {
            applicationScope
        }
        modules(networkModule, coroutineScope)
    }
}

val Application.applicationScope
    get() = CoroutineScope(coroutineContext)
