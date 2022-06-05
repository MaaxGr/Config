package de.maaxgr.config.backend.usermanagement.routes.variant

import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBConfigDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBRepositoryDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBVariantsDao
import de.maaxgr.config.backend.usermanagement.helpers.authenticatedUserContext
import de.maaxgr.config.backend.usermanagement.routes.variant.VariantConfigPostCall.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun Route.routeVariantConfigPost() {
    post("/variant/{variantId}/config") {
        call.authenticatedUserContext()?.let { user ->
            val variantId = call.parameters["variantId"]?.toIntOrNull()
                ?: return@post call.respond(HttpStatusCode.BadRequest, "Numeric 'variantId'-Parameter missing!")
            val configContent = call.receiveOrNull<String>()
                ?: return@post call.respond(HttpStatusCode.BadRequest, "Content missing!")

            val result = VariantConfigPostCall(
                variantId = variantId,
                configData = configContent
            ).run()

            when (result) {
                is CallResult.OK -> call.respond("OK")
                is CallResult.NotFound -> call.respond(HttpStatusCode.NotFound, "Variant not found!")
            }
        }
    }
}

class VariantConfigPostCall(
    private val variantId: Int,
    private val configData: String
) : KoinComponent {

    private val repositoryDao: MasterDBRepositoryDao by inject()
    private val configsDao: MasterDBConfigDao by inject()
    private val variantsDao: MasterDBVariantsDao by inject()

    suspend fun run(): CallResult {
        val variant = variantsDao.selectAll().firstOrNull { it.id == variantId }
            ?: return CallResult.NotFound

        variantsDao.updateConfig(variantId = variant.id, configDataEncrypted = configData)

        return CallResult.OK
    }

    sealed class CallResult {
        object OK : CallResult()
        object NotFound: CallResult()
    }

}

