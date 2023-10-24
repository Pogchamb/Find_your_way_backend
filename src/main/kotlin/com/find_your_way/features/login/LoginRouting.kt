package com.find_your_way.features.login

import com.find_your_way.features.cache.InMemoryCache
import com.find_your_way.features.cache.TokenCache
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureLoginRouting() {
    routing {
        get("/login") {
            val receive = call.receive(LoginReceiveRemote::class)
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