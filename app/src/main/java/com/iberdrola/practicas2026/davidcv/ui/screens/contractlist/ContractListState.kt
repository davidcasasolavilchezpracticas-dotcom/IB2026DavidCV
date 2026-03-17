package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.exception.ContractException
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListState

sealed class ContractListState {
    data object Loading : ContractListState()
    data class Error(val exception: ContractException) : ContractListState()
    data class Success(val contracts: List<Contract>) : ContractListState()
}