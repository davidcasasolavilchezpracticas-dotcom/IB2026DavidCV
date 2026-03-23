package com.iberdrola.practicas2026.davidcv.data.mappers

import com.iberdrola.practicas2026.davidcv.data.local.entity.ContractEntity
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType
import org.junit.Assert.assertEquals
import org.junit.Test

class ContractMapperTest {

    @Test
    fun `when contract toEntity is called, it should map all fields correctly`() {
        // Given
        val contract = Contract(
            id = 1,
            type = ContractType.LIGHT,
            status = ContractStatus.ACTIVE,
            phone = "600000000",
            email = "test@test.com"
        )

        // When
        val entity = contract.toEntity()

        // Then
        assertEquals(contract.id, entity.id)
        assertEquals(contract.type, entity.type)
        assertEquals(contract.status, entity.status)
        assertEquals(contract.phone, entity.phone)
        assertEquals(contract.email, entity.email)
    }

    @Test
    fun `when contractEntity toModel is called, it should map all fields correctly`() {
        // Given
        val entity = ContractEntity(
            id = 1,
            type = ContractType.GAS,
            status = ContractStatus.INACTIVE,
            phone = "900000000",
            email = "gas@test.com"
        )

        // When
        val model = entity.toModel()

        // Then
        assertEquals(entity.id, model.id)
        assertEquals(entity.type, model.type)
        assertEquals(entity.status, model.status)
        assertEquals(entity.phone, model.phone)
        assertEquals(entity.email, model.email)
    }
}
