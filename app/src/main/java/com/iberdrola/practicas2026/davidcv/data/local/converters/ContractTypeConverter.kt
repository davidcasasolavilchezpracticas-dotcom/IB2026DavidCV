package com.iberdrola.practicas2026.davidcv.data.local.converters

import androidx.room.TypeConverter
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType

class ContractTypeConverter {

    @TypeConverter
    fun fromContractType(type: ContractType): String { return type.name }

    @TypeConverter
    fun toContractType(value: String): ContractType { return ContractType.valueOf(value) }
}
