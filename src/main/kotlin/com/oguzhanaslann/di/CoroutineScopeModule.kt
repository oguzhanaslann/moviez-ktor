package com.oguzhanaslann.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named

val coroutineScope = org.koin.dsl.module {

    factory(named(Scopes.Default)) {
        CoroutineScope(Dispatchers.Default)
    }

    factory(
        named(Scopes.IO)
    ) {
        CoroutineScope(Dispatchers.IO)
    }

    factory(
        named(Scopes.Unconfined)
    ) {
        CoroutineScope(Dispatchers.Unconfined)
    }

}

enum class Scopes {
    Default, IO, Unconfined
}

