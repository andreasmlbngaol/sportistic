package com.jawapbo.sportistic.core.database.staffs

import com.jawapbo.sportistic.core.database.merchants.toMerchant
import com.jawapbo.sportistic.core.database.users.toUser
import com.jawapbo.sportistic.shared.data.staffs.Staff
import com.jawapbo.sportistic.shared.data.staffs.StaffJoint
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toStaff() = Staff(
    id = this[Staffs.id].value,
    userId = this[Staffs.userId],
    merchantId = this[Staffs.merchantId].value,
    role = this[Staffs.role],
    code = this[Staffs.code]
)

fun ResultRow.toStaffJoint(): StaffJoint {
    val userId = this[Staffs.userId]
    val user = userId?.let { this.toUser() }

    val merchant = this.toMerchant()

    return StaffJoint(
        id = this[Staffs.id].value,
        user = user,
        merchant = merchant,
        role = this[Staffs.role],
        code = this[Staffs.code]
    )
}
