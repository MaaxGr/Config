package de.maaxgr.config.backend.dataaccess.database.master.dao

import de.maaxgr.config.backend.dataaccess.database.master.MasterDatabaseSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface MasterDBConfigDao {

    fun selectAll(): List<MasterDBConfigDaoImpl.DBConfigEntity>

}

class MasterDBConfigDaoImpl: MasterDBConfigDao, KoinComponent {

    private val database: MasterDatabaseSource by inject()

    override fun selectAll(): List<DBConfigEntity> {
        return database.ktormDatabase().sequenceOf(DBConfigTable).toList()
    }

    object DBConfigTable: Table<DBConfigEntity>("config") {
        val id = int("id").bindTo { it.id }
        val repositoryId = int("repository_id").bindTo { it.repositoryId }
        val path = varchar("path").bindTo { it.path }
    }

    interface DBConfigEntity: Entity<DBConfigEntity> {
        companion object : Entity.Factory<DBConfigEntity>()

        val id: Int
        val repositoryId: Int
        val path: String
    }

}
