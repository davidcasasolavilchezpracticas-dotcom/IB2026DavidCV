package com.iberdrola.practicas2026.davidcv.domain.model.contract

data class Contract (
    val id: Int,
    val type: ContractType,
    val status: ContractStatus,
    val email: String
) {
}