package de.maaxgr.config.backend.usermanagement.routes.variant

import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBConfigDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBRepositoryDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBVariantsDao
import de.maaxgr.config.backend.usermanagement.helpers.authenticatedUserContext
import de.maaxgr.config.backend.usermanagement.routes.variant.VariantConfigGetCall.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun Route.routeVariantConfigGet() {
    get("/variant/{variantId}/config") {
        call.authenticatedUserContext()?.let { user ->
            val variantId = call.parameters["variantId"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Numeric 'variantId'-Parameter missing!")

            val result = VariantConfigGetCall(
                variantId = variantId
            ).run()

            when (result) {
                is CallResult.OK -> call.respond(result.configData)
                is CallResult.NotFound -> call.respond(HttpStatusCode.NotFound, "Variant not found!")
            }
        }
    }
}

class VariantConfigGetCall(
    private val variantId: Int
) : KoinComponent {

    private val repositoryDao: MasterDBRepositoryDao by inject()
    private val configsDao: MasterDBConfigDao by inject()
    private val variantsDao: MasterDBVariantsDao by inject()

    suspend fun run(): CallResult {
        val variant = variantsDao.selectAll().firstOrNull { it.id == variantId }
            ?: return CallResult.NotFound

        return CallResult.OK(variant.configEncrypted)
    }

    sealed class CallResult {
        data class OK(val configData: String) : CallResult()
        object NotFound: CallResult()
    }

}

