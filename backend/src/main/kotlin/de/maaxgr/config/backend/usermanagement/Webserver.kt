package de.maaxgr.config.backend.usermanagement

import de.maaxgr.config.backend.usermanagement.routes.repository.routePostInit
import de.maaxgr.config.backend.usermanagement.routes.repository.routeRepositoryDetailGet
import de.maaxgr.config.backend.usermanagement.routes.repository.routeRepositorySearchGet
import de.maaxgr.config.backend.usermanagement.routes.user.routeUserLoginPost
import de.maaxgr.config.backend.usermanagement.routes.variant.routeVariantConfigGet
import de.maaxgr.config.backend.usermanagement.routes.variant.routeVariantConfigPost
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

class Webserver {

    init {
        embeddedServer(Netty, port = 8077) {
            install(CallLogging) {
                level = Level.INFO
            }
            install(ContentNegotiation) {
                json()
            }


            routing {
                routePostInit()
                routeUserLoginPost()
                routeRepositorySearchGet()
                routeRepositoryDetailGet()
                routeVariantConfigGet()
                routeVariantConfigPost()
            }
        }.start(true)
    }

}
