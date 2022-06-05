package de.maaxgr.config.backend.usermanagement.helpers

import de.maaxgr.config.backend.businesslogic.utils.get
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBUserDaoImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

data class UserContext(
    val user: MasterDBUserDaoImpl.DBUserEntity
)

suspend fun ApplicationCall.authenticatedUserContext(): UserContext? {
    val user = get<AuthenticationGuard>().test(this)

    if (user == null) {
        respond(HttpStatusCode.Unauthorized, "Invalid credentials!")
        return null
    }

    return UserContext(
        user = user
    )
}
