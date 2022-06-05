package de.maaxgr.config.backend.usermanagement.routes.repository

import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBRepositoryDao
import de.maaxgr.config.backend.usermanagement.helpers.authenticatedUserContext
import de.maaxgr.config.backend.usermanagement.routes.repository.RepositoryInitPostRouteCall.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun Route.routePostInit() {
    post("/repository/init") {
        call.authenticatedUserContext()?.let { user ->
            val data = call.receive<RequestData>()

            val result = RepositoryInitPostRouteCall(
                requestData = data
            ).run()

            when (result) {
                is CallResult.OK -> call.respond("OK")
                is CallResult.ErrorAlreadyExist -> {
                    call.respond(HttpStatusCode.BadRequest, "Repository already exists with that name!")
                }
            }
        }
    }
}

class RepositoryInitPostRouteCall(
    private val requestData: RequestData
) : KoinComponent {

    private val repositoryDao: MasterDBRepositoryDao by inject()

    suspend fun run(): CallResult {
        if (repositoryDao.selectAll().any { it.name == requestData.name }) {
            return CallResult.ErrorAlreadyExist
        }

        repositoryDao.init(
            name = requestData.name
        )

        return CallResult.OK
    }

    @kotlinx.serialization.Serializable
    class RequestData(
        val name: String
    )

    sealed class CallResult {
        object ErrorAlreadyExist: CallResult()
        object OK : CallResult()
    }

}

