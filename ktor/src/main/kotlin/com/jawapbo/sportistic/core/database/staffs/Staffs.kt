package com.jawapbo.sportistic.core.database.staffs

import com.jawapbo.sportistic.shared.data.staffs.StaffRole
import com.jawapbo.sportistic.core.database.merchants.Merchants
import com.jawapbo.sportistic.core.database.users.Users
import org.jetbrains.exposed.v1.core.ReferenceOption.CASCADE
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Staffs: LongIdTable("staffs") {
    val userId = optReference("user_id", Users.id, onDelete = CASCADE)
    val merchantId = reference("merchant_id", Merchants.id, onDelete = CASCADE)
    val role = enumerationByName<StaffRole>("role", 32)
    val code = varchar("code", 64).uniqueIndex()
}