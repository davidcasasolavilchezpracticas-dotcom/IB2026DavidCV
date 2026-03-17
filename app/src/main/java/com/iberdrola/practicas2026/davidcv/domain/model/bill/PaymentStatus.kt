package com.iberdrola.practicas2026.davidcv.domain.model.bill

/**
 * PaymentStatus
 * Gestiona los posibles estados de pago
 *
 * @Param label Nombre del estado
 */
enum class PaymentStatus(val label: String) {
    PAID("Pagada"),
    PENDING("Pendiente de Pago"),
    TRAMITED("En trámite de cobro"),
    CANCELED("Anulada"),
    FIXED_PAYMENT("Cuota Fija")
}