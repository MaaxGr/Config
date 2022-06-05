package de.maaxgr.config.backend.dataaccess.database.master.dao

import de.maaxgr.config.backend.dataaccess.database.master.MasterDatabaseSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface MasterDBVariantsDao {

    fun selectAll(): List<MasterDBVariantsDaoImpl.DBVariantsEntity>

    fun updateConfig(variantId: Int, configDataEncrypted: String)
}

class MasterDBVariantsDaoImpl: MasterDBVariantsDao, KoinComponent {

    private val database: MasterDatabaseSource by inject()

    override fun selectAll(): List<DBVariantsEntity> {
        return database.ktormDatabase().sequenceOf(DBVariantsTable).toList()
    }

    override fun updateConfig(variantId: Int, configDataEncrypted: String) {
        database.ktormDatabase().update(DBVariantsTable) {
            set(it.configEncrypted, configDataEncrypted)
            where { it.id eq variantId }
        }
    }

    object DBVariantsTable: Table<DBVariantsEntity>("variants") {
        val id = int("id").bindTo { it.id }
        val repositoryId = int("repository_id").bindTo { it.repositoryId }
        val name = varchar("name").bindTo { it.name }
        val createdBy = int("created_by").bindTo { it.createdBy }
        val configEncrypted = varchar("config_encrypted").bindTo { it.configEncrypted }
        val configId = int("config_id").bindTo { it.configId }
    }

    interface DBVariantsEntity: Entity<DBVariantsEntity> {
        companion object : Entity.Factory<DBVariantsEntity>()

        val id: Int
        val repositoryId: Int
        val name: String
        val createdBy: Int
        val configEncrypted: String
        val configId: Int
    }

}
