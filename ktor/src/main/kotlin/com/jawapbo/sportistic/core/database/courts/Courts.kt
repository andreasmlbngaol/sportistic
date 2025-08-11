package com.jawapbo.sportistic.core.database.courts

import com.jawapbo.sportistic.core.database.arenas.Arenas
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Courts: LongIdTable("courts") {
    val arenaId = reference("arena_id", Arenas.id)
    val name = varchar("name", 100)
    val imageUrl = varchar("image_url", 255).nullable()
}