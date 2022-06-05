package de.maaxgr.config.backend.dataaccess.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigYaml(
    val encryptionKey: String,
    val databases: ConfigDatabases,
)

@Serializable
data class ConfigDatabases(
    val master: ConfigDatabaseMySQL
)

@Serializable
data class ConfigDatabaseMySQL(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val database: String
)
