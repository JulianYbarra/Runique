package com.junka.wear.run.data.connectivity

import com.junka.core.connectivity.domain.DeviceNode
import com.junka.core.connectivity.domain.DeviceType
import com.junka.core.connectivity.domain.NodeDiscovery
import com.junka.core.connectivity.domain.messaging.MessagingAction
import com.junka.core.connectivity.domain.messaging.MessagingClient
import com.junka.core.connectivity.domain.messaging.MessagingError
import com.junka.core.domain.util.EmptyResult
import com.junka.wear.run.domain.PhoneConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

@OptIn(ExperimentalCoroutinesApi::class)
class WatchToPhoneConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope,
    private val messagingClient: MessagingClient
) : PhoneConnector {

    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedNode = _connectedNode.asStateFlow()

    override val messagingActions: Flow<MessagingAction> = nodeDiscovery
        .observeConnectedDevices(DeviceType.WATCH)
        .flatMapLatest { connectedDevices ->
            val node = connectedDevices.firstOrNull()
            if (node != null && node.isNearBy) {
                _connectedNode.value = node
                messagingClient.connectToNode(node.id)
            } else flowOf()
        }
        .shareIn(
            applicationScope,
            started = SharingStarted.Eagerly
        )

    override suspend fun sendActionToPhone(action: MessagingAction): EmptyResult<MessagingError> {
        return messagingClient.sendOrQueueAction(action)
    }
}