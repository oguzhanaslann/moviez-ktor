package com.oguzhanaslann.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.oguzhanaslann.base.ONE_HOUR_MINUTES
import com.oguzhanaslann.base.ONE_MINUTE_MILLIS
import com.oguzhanaslann.dataSource.db.UsersDAO
import com.oguzhanaslann.plugins.routing.LOGIN
import com.oguzhanaslann.plugins.routing.REGISTER
import com.oguzhanaslann.util.chain
import com.oguzhanaslann.util.chainBySelfPredicate
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
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
        post(LOGIN) {
            val formParameters = call.receiveParameters()
            val email = formParameters["email"].toString()
            val password = formParameters["password"].toString()

            val userExistsResult = usersDAO.getIsUserWithEmailExists(email)

            userExistsResult.chainBySelfPredicate(
                resultBuilderBlock = { usersDAO.isPasswordCorrect(email, password) },
                onSuccess = { isPasswordCorrect ->
                    if (isPasswordCorrect) {
                        val token = getUserToken(email, password)
                        call.respond(HttpStatusCode.OK, token)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Email or password is wrong")
                    }
                },
                onPredicateFalse = {
                    call.respond(HttpStatusCode.BadRequest, "Email or password is wrong")
                },
                onMainResultFail = {
                    call.respond(HttpStatusCode.InternalServerError)
                    System.err.println("Error while controlling email")
                },
                onOtherResultFail = {
                    call.respond(HttpStatusCode.InternalServerError)
                    System.err.println("Error while controlling password")
                }

            )

        }


        post(REGISTER) {
            val formParameters = call.receiveParameters()
            val email = formParameters["email"].toString()
            val password = formParameters["password"].toString()
            val userExistsResult = usersDAO.getIsUserWithEmailExists(email)

            userExistsResult.chainBySelfPredicate(
                reversePredicate = true,
                resultBuilderBlock = {
                    usersDAO.registerUser(email, password)
                },
                onOtherResultFail = {
                    call.respond(HttpStatusCode.InternalServerError)
                    System.err.println("Error while registering User")
                },
                onSuccess = { hasRegistered ->
                    if (hasRegistered) {
                        val token = getUserToken(email, password)
                        call.respond(HttpStatusCode.OK, token)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Registration failed")
                    }
                },
                onPredicateFalse = {
                    call.respond(HttpStatusCode.BadRequest, "There is already a user with this email.")
                },

                onMainResultFail = {
                    call.respond(HttpStatusCode.InternalServerError)
                    System.err.println("Error while controlling User")
                }

            )
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

    return with(JWT.create()) {
        withAudience(audience)
        withIssuer(issuer)
        withClaim("email", email)
        withClaim("password", password)
        withExpiresAt(Date(System.currentTimeMillis() + ONE_MINUTE_MILLIS * ONE_HOUR_MINUTES))
        sign(Algorithm.HMAC256(secret))
    }
}
