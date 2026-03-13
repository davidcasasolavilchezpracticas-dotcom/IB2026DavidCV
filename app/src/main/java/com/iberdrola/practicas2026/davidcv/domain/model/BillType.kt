package com.iberdrola.practicas2026.davidcv.domain.model

/**
 * BillType
 * Gestiona los posibles tipos de factura
 *
 * @Param label Nombre del tipo de factura
 */
enum class BillType(val label: String) {
    LIGHT("Factura de luz"),
    GAS("Factura de gas")
}