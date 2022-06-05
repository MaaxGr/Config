package de.maaxgr.config.backend.usermanagement.routes.repository

import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBRepositoryDao
import de.maaxgr.config.backend.usermanagement.entities.UIRepository
import de.maaxgr.config.backend.usermanagement.helpers.authenticatedUserContext
import de.maaxgr.config.backend.usermanagement.routes.repository.RepositorySearchGetCall.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun Route.routeRepositorySearchGet() {
    get("/repository/search") {
        call.authenticatedUserContext()?.let { user ->
            val needle = call.parameters["needle"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "'needle'-Parameter missing!")

            val result = RepositorySearchGetCall(
                needle = needle
            ).run()

            when (result) {
                is CallResult.OK -> call.respond(result.repositories)
            }
        }
    }
}

class RepositorySearchGetCall(
    private val needle: String
) : KoinComponent {

    private val repositoryDao: MasterDBRepositoryDao by inject()

    suspend fun run(): CallResult {

        val results = repositoryDao.selectAll().filter { it.name.contains(needle) }

        return CallResult.OK(
            repositories = results.map {
                UIRepository(
                    id = it.id,
                    name = it.name
                )
            }
        )
    }


    sealed class CallResult {
        data class OK(val repositories: List<UIRepository>) : CallResult()
    }

}

