package com.iberdrola.practicas2026.davidcv.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType

@Entity(tableName = "contracts")
data class ContractEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val type: ContractType,
    val status: ContractStatus,
    val email: String? = null
)
