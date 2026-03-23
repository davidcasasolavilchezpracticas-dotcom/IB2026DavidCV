package com.iberdrola.practicas2026.davidcv.data.mappers

import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class BillMapperTest {

    private val testDate = LocalDateTime.of(2023, 1, 1, 12, 0)

    @Test
    fun `when bill toEntity is called, it should map all fields correctly`() {
        // Given
        val bill = Bill(
            id = 1,
            type = BillType.LIGHT,
            value = 55.5f,
            startDate = testDate,
            endDate = testDate.plusMonths(1),
            paymentStatus = PaymentStatus.PAID
        )

        // When
        val entity = bill.toEntity()

        // Then
        assertEquals(bill.id, entity.id)
        assertEquals(bill.type, entity.type)
        assertEquals(bill.value, entity.value)
        assertEquals(bill.startDate, entity.startDate)
        assertEquals(bill.endDate, entity.endDate)
        assertEquals(bill.paymentStatus, entity.paymentStatus)
    }

    @Test
    fun `when billEntity toModel is called, it should map all fields correctly`() {
        // Given
        val entity = BillEntity(
            id = 10,
            type = BillType.GAS,
            value = 120.0f,
            startDate = testDate,
            endDate = testDate.plusMonths(1),
            paymentStatus = PaymentStatus.PENDING
        )

        // When
        val model = entity.toModel()

        // Then
        assertEquals(entity.id, model.id)
        assertEquals(entity.type, model.type)
        assertEquals(entity.value, model.value)
        assertEquals(entity.startDate, model.startDate)
        assertEquals(entity.endDate, model.endDate)
        assertEquals(entity.paymentStatus, model.paymentStatus)
    }
}
