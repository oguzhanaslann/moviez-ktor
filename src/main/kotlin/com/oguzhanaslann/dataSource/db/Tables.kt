package com.oguzhanaslann.dataSource.db

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object Movies: Table() {
    val id : Column<Int> = integer("id").autoIncrement()
    val name: Column<String>  = varchar("name",150)

    override val primaryKey = PrimaryKey(id)
}