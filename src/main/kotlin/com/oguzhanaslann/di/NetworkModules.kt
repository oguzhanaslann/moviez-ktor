package com.oguzhanaslann.di

import com.oguzhanaslann.dataSource.network.client.MovieClient
import org.koin.dsl.module

val networkModule = module {

    single {
        MovieClient.getInstance()
    }

}