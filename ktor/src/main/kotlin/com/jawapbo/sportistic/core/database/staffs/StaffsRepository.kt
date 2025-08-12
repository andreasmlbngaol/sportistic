package com.jawapbo.sportistic.core.database.staffs

import com.jawapbo.sportistic.core.database.merchants.Merchants
import com.jawapbo.sportistic.core.database.users.Users
import com.jawapbo.sportistic.shared.data.staffs.Staff
import org.jetbrains.exposed.v1.core.innerJoin
import org.jetbrains.exposed.v1.core.leftJoin
import org.jetbrains.exposed.v1.jdbc.insertIgnoreAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

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

    fun findByUserId(userId: String) = transaction {
        Staffs
            .selectAll()
            .where { Staffs.userId eq userId }
            .map { it.toStaff() }
            .singleOrNull()
    }

    fun findByCode(code: String) = transaction {
        Staffs
            .leftJoin(Users, { Staffs.userId }, { Users.id })
            .innerJoin(Merchants, { Staffs.merchantId }, { Merchants.id })
            .selectAll()
            .where { Staffs.code eq code }
            .map { it.toStaffJoint() }
            .singleOrNull()
    }

    fun enrollUserToStaff(staffId: Long, userId: String) = transaction {
        Staffs
            .update({Staffs.id eq staffId}) {
                it[Staffs.userId] = userId
            }
    }
}