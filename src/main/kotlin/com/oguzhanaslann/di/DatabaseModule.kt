package com.oguzhanaslann.di

import com.oguzhanaslann.dataSource.db.UserDatabaseOperationsHandler
import com.oguzhanaslann.dataSource.db.UsersDAO
import com.oguzhanaslann.util.SecurityManager
import org.koin.dsl.module

val databaseModule = module {
    factory<SecurityManager> {
       com.oguzhanaslann.util.SecurityManager()
    }
    single<UsersDAO> {
        UserDatabaseOperationsHandler(
            get()
        )
    }


}
