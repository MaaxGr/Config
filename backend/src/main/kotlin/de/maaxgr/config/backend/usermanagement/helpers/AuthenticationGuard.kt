package de.maaxgr.config.backend.usermanagement.helpers

import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBTokenDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBUserDao
import de.maaxgr.config.backend.dataaccess.database.master.dao.MasterDBUserDaoImpl
import io.ktor.server.application.*
import io.ktor.server.request.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface AuthenticationGuard {
    suspend fun test(call: ApplicationCall): MasterDBUserDaoImpl.DBUserEntity?
}

class AuthenticationGuardImpl : AuthenticationGuard, KoinComponent {

    private val tokenDao: MasterDBTokenDao by inject()
    private val userDao: MasterDBUserDao by inject()

    override suspend fun test(call: ApplicationCall): MasterDBUserDaoImpl.DBUserEntity? {
        return searchByToken(call)
    }

    private fun searchByToken(call: ApplicationCall): MasterDBUserDaoImpl.DBUserEntity? {
        val token = call.request.header("Token")
            ?: return null

        val entry = tokenDao.selectAll().firstOrNull { it.token == token }
            ?: return null

        return userDao.selectAll().firstOrNull { it.id == entry.userId }
    }

}
