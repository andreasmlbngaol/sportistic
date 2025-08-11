package com.jawapbo.sportistic.core.database.staffs

import com.jawapbo.sportistic.shared.data.staffs.Staff
import org.jetbrains.exposed.v1.jdbc.insertIgnoreAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object StaffsRepository {
    fun existByCode(code: String) = transaction {
        Staffs
            .select(Staffs.id)
            .where { Staffs.code eq code }
            .count() > 0
    }

    fun save(staff: Staff) = transaction {
        Staffs.insertIgnoreAndGetId {
            it[userId] = staff.userId
            it[merchantId] = staff.merchantId
            it[role] = staff.role
            it[code] = staff.code
        }
    }?.value
}