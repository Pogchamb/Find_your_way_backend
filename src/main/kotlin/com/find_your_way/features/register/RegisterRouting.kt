package com.find_your_way.features.register

import com.find_your_way.features.cache.InMemoryCache
import com.find_your_way.features.cache.TokenCache
import com.find_your_way.features.login.LoginResponseRemote
import com.find_your_way.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRegisterRouting() {
    routing {
        get("/register") {
            val receive = call.receive(RegisterReceiveRemote::class)
            if (!receive.email.isValidEmail()) {
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            }

            if (InMemoryCache.userList.map { it.login }.contains(receive.login)) {
                call.respond(HttpStatusCode.Conflict, "This user already exist")
            }


            if (InMemoryCache.userList.map { it.login }.contains(receive.login)) {
                val token = UUID.randomUUID().toString()
                InMemoryCache.token.add(TokenCache(login = receive.login, token))
                call.respond(LoginResponseRemote(token = token))
                return@get
            }

            call.respond(HttpStatusCode.BadRequest)
        }
    }
}