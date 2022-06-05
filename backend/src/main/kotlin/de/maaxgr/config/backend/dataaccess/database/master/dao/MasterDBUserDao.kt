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

interface MasterDBUserDao {

    fun selectAll(): List<MasterDBUserDaoImpl.DBUserEntity>

}

class MasterDBUserDaoImpl: MasterDBUserDao, KoinComponent {

    private val database: MasterDatabaseSource by inject()

    override fun selectAll(): List<DBUserEntity> {
        return database.ktormDatabase().sequenceOf(DBUserTable).toList()
    }

    object DBUserTable: Table<DBUserEntity>("user") {
        val id = int("id").bindTo { it.id }
        val username = varchar("username").bindTo { it.username }
        val hashedPassword = varchar("hashed_password").bindTo { it.hashedPassword }
    }

    interface DBUserEntity: Entity<DBUserEntity> {
        companion object : Entity.Factory<DBUserEntity>()

        val hashedPassword: String
        val id: Int
        val username: String
    }

}
