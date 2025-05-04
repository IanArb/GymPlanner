package com.ianarbuckle.gymplanner.android.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.REMEMBER_ME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val _rememberMe = Channel<Boolean>()
    val rememberMe = _rememberMe.receiveAsFlow()

    init {
        viewModelScope.launch {
            _rememberMe.send(dataStoreRepository.getBooleanData(key = REMEMBER_ME_KEY) ?: false)
        }
    }
}
