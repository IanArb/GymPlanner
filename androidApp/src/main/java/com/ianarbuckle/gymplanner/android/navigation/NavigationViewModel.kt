package com.ianarbuckle.gymplanner.android.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.api.GymPlanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    dispatchers: CoroutinesDispatcherProvider,
): ViewModel() {

    private val _rememberMe = Channel<Boolean>()
    val rememberMe = _rememberMe.receiveAsFlow()

    init {
        viewModelScope.launch(dispatchers.io) {
            _rememberMe.send(gymPlanner.fetchRememberMe())
        }
    }
}