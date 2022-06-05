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
import org.ktorm.schema.jdbcTimestamp
import org.ktorm.schema.varchar
import java.sql.Timestamp

interface MasterDBTokenDao {

    fun selectAll(): List<MasterDBTokenDaoImpl.DBTokenEntity>

    fun create(token: String, createdAt: Timestamp, userId: Int)
}

class MasterDBTokenDaoImpl: MasterDBTokenDao, KoinComponent {

    private val database: MasterDatabaseSource by inject()

    override fun selectAll(): List<DBTokenEntity> {
        return database.ktormDatabase().sequenceOf(DBTokenTable).toList()
    }

    override fun create(token: String, createdAt: Timestamp, userId: Int) {
        database.ktormDatabase().insert(DBTokenTable) {
            set(it.token, token)
            set(it.createdAt, createdAt)
            set(it.userId, userId)
        }
    }


    object DBTokenTable: Table<DBTokenEntity>("token") {
        val id = int("id").bindTo { it.id }
        val token = varchar("token").bindTo { it.token }
        val createdAt = jdbcTimestamp("created_at").bindTo { it.createdAt }
        val userId = int("user_id").bindTo { it.userId }
    }

    interface DBTokenEntity: Entity<DBTokenEntity> {
        companion object : Entity.Factory<DBTokenEntity>()

        val id: Int
        val token: String
        val createdAt: Timestamp
        val userId: Int
    }

}
