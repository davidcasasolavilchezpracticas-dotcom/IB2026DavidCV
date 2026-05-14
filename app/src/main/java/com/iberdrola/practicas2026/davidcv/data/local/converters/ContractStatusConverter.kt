package com.iberdrola.practicas2026.davidcv.data.local.converters

import androidx.room.TypeConverter
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus

class ContractStatusConverter {

    @TypeConverter
    fun fromContractStatus(status: ContractStatus): String { return status.name }

    @TypeConverter
    fun toContractStatus(value: String): ContractStatus { return ContractStatus.valueOf(value) }
}
