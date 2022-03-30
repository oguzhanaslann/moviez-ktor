package com.oguzhanaslann.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.oguzhanaslann.base.ONE_HOUR_MINUTES
import com.oguzhanaslann.base.ONE_MINUTE_MILLIS
import com.oguzhanaslann.dataSource.db.UsersDAO
import com.oguzhanaslann.domainModel.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.launch
import org.koin.ktor.ext.get
import java.util.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }

    loginRouting()
}

private fun Application.loginRouting() {
    val usersDAO = get<UsersDAO>()
    routing {
        post("/login") {
            val formParameters = call.receiveParameters()
            val email = formParameters["email"].toString()
            val isUserExists = usersDAO.getIsUserWithEmailExists(email)
            if (isUserExists) {
                val password = formParameters["password"].toString()

                val isPasswordCorrect = usersDAO.isPasswordCorrect(email,password)

                if (isPasswordCorrect) {
                    val token = getUserToken(email, password)
                    call.respond(HttpStatusCode.OK, token)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Email or password is wrong")
                }

            } else {
                call.respond(HttpStatusCode.BadRequest, "Email or password is wrong")
            }
        }


        post("/register") {
            val formParameters = call.receiveParameters()
            val email = formParameters["email"].toString()
            val password = formParameters["password"].toString()
            val isUserExists = usersDAO.getIsUserWithEmailExists(email)
            if (isUserExists) {
                call.respond(HttpStatusCode.BadRequest, "There is already a user with this email.")
            } else {
                usersDAO.registerUser(email,password)
                val token = getUserToken(email, password)
                call.respond(HttpStatusCode.OK, token)
            }
        }

    }
}

private fun Application.getUserToken(
    email: String,
    password: String
): String {
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()

    val token = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("email", email)
        .withClaim("password", password)
        .withExpiresAt(Date(System.currentTimeMillis() + ONE_MINUTE_MILLIS * ONE_HOUR_MINUTES))
        .sign(Algorithm.HMAC256(secret))
    return token
}
