package com.iberdrola.practicas2026.davidcv.data.mappers

import com.iberdrola.practicas2026.davidcv.data.local.entity.ContractEntity
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract

/**
 * Mapper para pasar del modelo de domain al modelo de data
 */
fun Contract.toEntity(): ContractEntity {
    return ContractEntity(
        id = this.id,
        type = this.type,
        status = this.status,
        email = this.email
    )
}

/**
 * Mapper para pasar del modelo de data al modelo de domain
 */
fun ContractEntity.toModel(): Contract {
    return Contract(
        id = this.id,
        type = this.type,
        status = this.status,
        email = this.email
    )
}
