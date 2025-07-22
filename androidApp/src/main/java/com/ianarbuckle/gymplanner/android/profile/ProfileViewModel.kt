package com.ianarbuckle.gymplanner.android.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _user = MutableStateFlow(Pair("", ""))
    val user = _user.asSharedFlow()

    init {
        viewModelScope.launch {
            val userId = dataStoreRepository.getStringData(USER_ID) ?: ""
            profileRepository
                .fetchProfile(userId)
                .fold(
                    onSuccess = { _user.emit(Pair(it.username, it.userId)) },
                    onFailure = { _user.emit(Pair("Guest", "")) },
                )
        }
    }
}
