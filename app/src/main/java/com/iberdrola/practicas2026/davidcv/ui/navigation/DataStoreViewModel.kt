package com.iberdrola.practicas2026.davidcv.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iberdrola.practicas2026.davidcv.data.local.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val bsCounter: StateFlow<Int> = dataStoreManager.bsCounter
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val trys: StateFlow<Int> = dataStoreManager.trys
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 3
        )

    fun updateBsCounter(counter: Int) {
        viewModelScope.launch {
            dataStoreManager.saveBsCounter(counter)
        }
    }

    fun updateTrys(trys: Int) {
        viewModelScope.launch {
            dataStoreManager.saveTrys(trys)
        }
    }
}
