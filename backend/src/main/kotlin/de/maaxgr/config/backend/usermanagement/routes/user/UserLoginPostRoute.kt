package de.maaxgr.config.backend.usermanagement.routes.user

import de.maaxgr.config.backend.businesslogic.helper.Hasher
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBTokenDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBUserDao
import de.maaxgr.config.backend.usermanagement.routes.user.UserLoginPostRouteCall.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Timestamp
import java.util.UUID

fun Route.routeUserLoginPost() {
    post("/user/login") {
        val data = call.receive<RequestData>()

        val result = UserLoginPostRouteCall(
            requestData = data
        ).run()

        when (result) {
            is CallResult.OK -> call.respond(result.data)
            is CallResult.InvalidCredentials -> {
                call.respond(HttpStatusCode.Unauthorized, "Bad credentials!")
            }
        }
    }

}

class UserLoginPostRouteCall(
    private val requestData: RequestData
) : KoinComponent {

    private val usersDao: MasterDBUserDao by inject()
    private val tokenDao: MasterDBTokenDao by inject()
    private val hasher: Hasher by inject()

    suspend fun run(): CallResult {

        val user = usersDao.selectAll().firstOrNull { it.username == requestData.username }
            ?: return CallResult.InvalidCredentials

        val result = hasher.checkAgainstHash(requestData.password, user.hashedPassword)

        if (!result) {
            return CallResult.InvalidCredentials
        }

        val token = UUID.randomUUID().toString().replace("-", "")

        tokenDao.create(
            token = token,
            createdAt = Timestamp(System.currentTimeMillis()),
            userId = user.id
        )

        return CallResult.OK(
            data = ResponseData(
                token = token
            )
        )
    }

    @kotlinx.serialization.Serializable
    class RequestData(
        val username: String,
        val password: String
    )

    @kotlinx.serialization.Serializable
    class ResponseData(
        val token: String
    )

    sealed class CallResult {
        object InvalidCredentials : CallResult()
        data class OK(val data: ResponseData) : CallResult()
    }

}

