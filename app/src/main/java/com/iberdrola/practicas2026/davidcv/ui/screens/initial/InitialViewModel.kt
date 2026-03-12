package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetGasBillsUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetLightBillsUseCase
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class BillListViewModel @Inject constructor(
    
) : ViewModel() {

    fun useLocal() {

    }
}