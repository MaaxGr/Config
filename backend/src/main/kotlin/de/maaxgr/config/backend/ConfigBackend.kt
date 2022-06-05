package de.maaxgr.config.backend

import de.maaxgr.config.backend.dataaccess.config.ConfigDatabaseMySQL
import de.maaxgr.config.backend.dataaccess.database.master.MasterDatabaseSource
import de.maaxgr.config.backend.usermanagement.Webserver
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class ConfigBackend: KoinComponent {

    private val masterDatabaseSource: MasterDatabaseSource by inject()

    init {
        masterDatabaseSource.connect()
        masterDatabaseSource.loadDaos()

        Webserver()
    }

}
