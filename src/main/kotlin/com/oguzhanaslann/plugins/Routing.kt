package com.oguzhanaslann.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.oguzhanaslann.base.ONE_HOUR_MINUTES
import com.oguzhanaslann.base.ONE_MINUTE_MILLIS
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
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
    routing {
        post("/login") {
            val user = call.receive<User>()

            val secret = environment.config.property("jwt.secret").getString()
            val issuer = environment.config.property("jwt.issuer").getString()
            val audience = environment.config.property("jwt.audience").getString()

            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("email", user.email)
                .withClaim("password",user.password)
                .withExpiresAt(Date(System.currentTimeMillis() + ONE_MINUTE_MILLIS * ONE_HOUR_MINUTES))
                .sign(Algorithm.HMAC256(secret))

            call.respond(HttpStatusCode.OK, token)
        }


        post("/register") {
            val formParameters = call.receiveParameters()
            val email = formParameters["email"].toString()
            val password = formParameters["password"].toString()

            val secret = environment.config.property("jwt.secret").getString()
            val issuer = environment.config.property("jwt.issuer").getString()
            val audience = environment.config.property("jwt.audience").getString()

            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("email", email)
                .withClaim("password",password)
                .withExpiresAt(Date(System.currentTimeMillis() + ONE_MINUTE_MILLIS * ONE_HOUR_MINUTES))
                .sign(Algorithm.HMAC256(secret))

            call.respond(HttpStatusCode.OK, token)
        }

        authenticate {
            get("/requireLogin") {
                val principal = call.principal<JWTPrincipal>()
                call.respond(HttpStatusCode.OK, "${principal?.get("email")}")
            }
        }

    }
}

@Serializable
data class User(val email: String, val password: String)

