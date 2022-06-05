package de.maaxgr.config.backend.dataaccess.database.master

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.maaxgr.config.backend.dataaccess.config.ConfigConstants
import de.maaxgr.config.backend.dataaccess.config.ConfigDatabaseMySQL
import de.maaxgr.config.backend.dataaccess.database.master.dao.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.ktorm.database.Database


internal interface MasterDatabaseSource {
    fun connect()
    fun loadDaos()
    fun ktormDatabase(): Database
}

internal class MasterDatabaseSourceImpl: MasterDatabaseSource, KoinComponent {

    // dependencies
    private val configDatabase: ConfigDatabaseMySQL by inject(named(ConfigConstants.MASTER_DATABASE))

    // variables
    private lateinit var ktormDatabase: Database
    private lateinit var dataSource: HikariDataSource

    override fun connect() {
        Class.forName("com.mysql.cj.jdbc.Driver")

        val dbUrl = "${configDatabase.host}:${configDatabase.port}"

        val jdbcUrl = "jdbc:mysql://$dbUrl/${configDatabase.database}"
        val jdbcParameters = mapOf(
            "user" to configDatabase.username,
            "password" to configDatabase.password,
            "useSSL" to false,
            "allowPublicKeyRetrieval" to true,
        )

        val dsn = "%s?%s".format(
            jdbcUrl,
            jdbcParameters.toList().joinToString("&") { (key, value) -> "$key=$value" }
        )

        val config = HikariConfig()
        config.jdbcUrl = dsn
        dataSource = HikariDataSource(config)
        ktormDatabase = Database.connect(dataSource)
    }

    override fun loadDaos() {
        val daoModule = module {
            single<MasterDBConfigDao> { MasterDBConfigDaoImpl() }
            single<MasterDBRepositoryDao> { MasterDBRepositoryDaoImpl() }
            single<MasterDBUserDao> { MasterDBUserDaoImpl() }
            single<MasterDBTokenDao> { MasterDBTokenDaoImpl() }
            single<MasterDBVariantsDao> { MasterDBVariantsDaoImpl() }
        }
        loadKoinModules(daoModule)
    }

    override fun ktormDatabase(): Database {
        return ktormDatabase
    }

}
