package com.oguzhanaslann.plugins

import com.oguzhanaslann.dataSource.db.UsersDAO
import com.oguzhanaslann.di.coroutineScope
import com.oguzhanaslann.di.databaseModule
import com.oguzhanaslann.di.networkModule
import io.ktor.application.*
import kotlinx.coroutines.CoroutineScope
import org.koin.core.qualifier.named
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get

fun Application.configureDependencyInjection() {
    install(Koin) {
        coroutineScope.single(named("applicationScope")) {
            applicationScope
        }
        modules(networkModule, coroutineScope, databaseModule)
    }
}

val Application.applicationScope
    get() = CoroutineScope(coroutineContext)
