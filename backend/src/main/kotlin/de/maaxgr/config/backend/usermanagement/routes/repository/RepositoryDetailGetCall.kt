package de.maaxgr.config.backend.usermanagement.routes.repository

import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBConfigDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBRepositoryDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBVariantsDao
import de.maaxgr.config.backend.usermanagement.entities.UIRepositoryDetail
import de.maaxgr.config.backend.usermanagement.entities.UIRepositoryDetailConfig
import de.maaxgr.config.backend.usermanagement.entities.UIRepositoryDetailConfigVariant
import de.maaxgr.config.backend.usermanagement.helpers.authenticatedUserContext
import de.maaxgr.config.backend.usermanagement.routes.repository.RepositoryDetailGetCall.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun Route.routeRepositoryDetailGet() {
    get("/repository/{id}") {
        call.authenticatedUserContext()?.let { user ->
            val repositoryId = call.parameters["id"]?.toInt()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Numeric 'id'-Parameter missing!")

            val result = RepositoryDetailGetCall(
                repositoryId = repositoryId
            ).run()

            when (result) {
                is CallResult.OK -> call.respond(result.details)
                is CallResult.NotFound -> call.respond(HttpStatusCode.NotFound, "Repo not found!")
            }
        }
    }
}

class RepositoryDetailGetCall(
    private val repositoryId: Int
) : KoinComponent {

    private val repositoryDao: MasterDBRepositoryDao by inject()
    private val configsDao: MasterDBConfigDao by inject()
    private val variantsDao: MasterDBVariantsDao by inject()

    suspend fun run(): CallResult {
        val repositoryResult = repositoryDao.selectAll().firstOrNull { it.id == repositoryId }
            ?: return CallResult.NotFound

        val configs = configsDao.selectAll().filter { it.repositoryId == repositoryResult.id }

        val variants = variantsDao.selectAll()

        return CallResult.OK(
            details = UIRepositoryDetail(
                id = repositoryResult.id,
                name = repositoryResult.name,
                configs = configs.map { config ->
                    UIRepositoryDetailConfig(
                        path = config.path,
                        accessibleVariants = variants.filter { it.configId == config.id }.map {
                            UIRepositoryDetailConfigVariant(
                                id = it.id,
                                name = it.name
                            )
                        }
                    )
                }
            )
        )
    }

    sealed class CallResult {
        data class OK(val details: UIRepositoryDetail) : CallResult()
        object NotFound: CallResult()
    }

}

