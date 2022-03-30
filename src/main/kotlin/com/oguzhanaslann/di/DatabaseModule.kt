package com.oguzhanaslann.di

import com.oguzhanaslann.dataSource.db.UserDatabaseOperationsHandler
import com.oguzhanaslann.dataSource.db.UsersDAO
import kotlinx.coroutines.CoroutineScope
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {
    single<UsersDAO> {
        UserDatabaseOperationsHandler(
            get<CoroutineScope>(named(Scopes.IO))
        )
    }
}
