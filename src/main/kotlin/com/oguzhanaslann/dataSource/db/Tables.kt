package com.oguzhanaslann.dataSource.db

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object Users : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val email: Column<String> = varchar("email", 150)
    val password: Column<String> = varchar("password", 150)
    val firstName : Column<String?> = varcharNullable("firstName",50)
    val lastName : Column<String?> = varcharNullable("lastName",50)

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}

fun Table.varcharNullable(name :String, length: Int)  = varchar(name,length).nullable()

object Movies : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 150)

    override val primaryKey = PrimaryKey(id)
}

/*
* title // str
* year // int
* releaseDate dd MM YYYY
* duration // min int
* genre str
* director  string
* actors : listof str
* imdb score: double
* language: // enum
* poster : str url
* */
