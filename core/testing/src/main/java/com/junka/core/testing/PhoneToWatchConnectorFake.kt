package com.junka.core.testing

import com.junka.core.connectivity.domain.DeviceNode
import com.junka.core.connectivity.domain.messaging.MessagingAction
import com.junka.core.connectivity.domain.messaging.MessagingError
import com.junka.core.domain.util.EmptyResult
import com.junka.core.domain.util.Result
import com.junka.run.domain.WatchConnector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class PhoneToWatchConnectorFake : WatchConnector {

    var sendError: MessagingError? = null

    private val _isTrackable = MutableStateFlow(true)

    private val _connectedDevice = MutableStateFlow<DeviceNode?>(null)

    override val connectedDevice: StateFlow<DeviceNode?>
        get() = _connectedDevice.asStateFlow()

    private val _messagingAction = MutableSharedFlow<MessagingAction>()

    override val messagingActions: Flow<MessagingAction>
        get() = _messagingAction.asSharedFlow()

    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        return if (sendError == null) {
            Result.Success(Unit)
        } else {
            Result.Failure(sendError!!)
        }
    }

    override fun setIsTrackable(isTrackable: Boolean) {
        _isTrackable.value = isTrackable
    }

    suspend fun sendFromWatchToPhone(action: MessagingAction) {
        _messagingAction.emit(action)
    }
}