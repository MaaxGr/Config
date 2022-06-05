package de.maaxgr.config.backend.dataaccess.database.master.dao

import de.maaxgr.config.backend.dataaccess.database.master.MasterDatabaseSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.dsl.insert
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface MasterDBRepositoryDao {

    fun selectAll(): List<MasterDBRepositoryDaoImpl.DBRepositoriesEntity>
    fun init(name: String)
}

class MasterDBRepositoryDaoImpl: MasterDBRepositoryDao, KoinComponent {

    private val database: MasterDatabaseSource by inject()

    override fun init(name: String) {
        database.ktormDatabase().insert(DBRepositoriesTable) {
            set(it.name, name)
        }
    }

    override fun selectAll(): List<DBRepositoriesEntity> {
        return database.ktormDatabase().sequenceOf(DBRepositoriesTable).toList()
    }

    object DBRepositoriesTable: Table<DBRepositoriesEntity>("repositories") {
        val id = int("id").bindTo { it.id }
        val name = varchar("name").bindTo { it.name }
    }

    interface DBRepositoriesEntity: Entity<DBRepositoriesEntity> {
        companion object : Entity.Factory<DBRepositoriesEntity>()

        val id: Int
        val name: String
    }

}
