package com.ianarbuckle.gymplanner.android.utils

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class CoroutinesDispatcherProvider(
    val main: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val io: CoroutineDispatcher,
) {

    @Inject constructor() : this(Dispatchers.Main, Dispatchers.Default, Dispatchers.IO)
}
