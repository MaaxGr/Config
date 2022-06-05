package de.maaxgr.config.backend

import de.maaxgr.config.backend.businesslogic.helper.Hasher
import de.maaxgr.config.backend.businesslogic.helper.HasherImpl
import de.maaxgr.config.backend.dataaccess.config.ConfigConstants
import de.maaxgr.config.backend.dataaccess.config.ConfigLoader
import de.maaxgr.config.backend.dataaccess.config.ConfigYaml
import de.maaxgr.config.backend.dataaccess.database.master.MasterDatabaseSource
import de.maaxgr.config.backend.dataaccess.database.master.MasterDatabaseSourceImpl
import de.maaxgr.config.backend.usermanagement.helpers.AuthenticationGuard
import de.maaxgr.config.backend.usermanagement.helpers.AuthenticationGuardImpl
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun main() {

    startKoin {
        val mainModule = module {
            single { ConfigLoader().loadConfig() }
            single(named(ConfigConstants.MASTER_DATABASE)) { get<ConfigYaml>().databases.master }
            single(named(ConfigConstants.ENCRYPTION_KEY)) { get<ConfigYaml>().encryptionKey }
            single<MasterDatabaseSource> { MasterDatabaseSourceImpl() }
            single<Hasher> { HasherImpl() }

            /*
             * User-Interface
             */
            // Helpers
            single<AuthenticationGuard> { AuthenticationGuardImpl() }
        }
        modules(mainModule)
    }

    ConfigBackend()

}
