package com.iberdrola.practicas2026.davidcv.domain.model.contract

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contract(
    val id: Int,
    val type: ContractType,
    val status: ContractStatus,
    val phone: String? = null,
    val email: String? = null
) : Parcelable
