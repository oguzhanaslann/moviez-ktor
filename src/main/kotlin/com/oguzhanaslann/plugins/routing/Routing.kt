package com.oguzhanaslann.plugins.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.oguzhanaslann.base.ONE_HOUR_MINUTES
import com.oguzhanaslann.base.ONE_MINUTE_MILLIS
import com.oguzhanaslann.dataSource.db.UsersDAO
import com.oguzhanaslann.dataSource.db.mapping.mapToEntity
import com.oguzhanaslann.domainModel.User
import com.oguzhanaslann.util.chainBySelfPredicate
import com.oguzhanaslann.util.isNotNull
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get
import java.util.*

fun Application.configureRouting() {
    loginRouting()
    userRouting()
}

fun Application.userRouting() {
    val usersDAO = get<UsersDAO>()
    routing {
        authenticate {
            get(USER_WITH_ID) {
                val id = call.parameters["id"]?.toLongOrNull()

                if (id.isNotNull()) {
                    call.respond(User(1, "pass", "fname", "lname"))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Please provide a proper id value.")
                }
            }

            post(USER) {
                val user = call.receive<User>()
                println(user)
                val isExistsInDBResult = usersDAO.getIsUserWithIdExists(user.id)

                isExistsInDBResult.chainBySelfPredicate(
                    onPredicateFalse = {
                        call.respond(HttpStatusCode.BadRequest,"No such a user with id: ${user.id}")
                    },
                    onMainResultFail = {
                        call.respond(HttpStatusCode.InternalServerError,"Something went wrong")
                    },
                    resultBuilderBlock = {
                        usersDAO.updateUser(user.mapToEntity())
                    },
                    onSuccess = { _ ->
                        call.respond(HttpStatusCode.OK)
                    },
                    onOtherResultFail = {
                        call.respond(HttpStatusCode.BadRequest,"User could not be updated")
                    }
                )

            }
        }
    }
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
