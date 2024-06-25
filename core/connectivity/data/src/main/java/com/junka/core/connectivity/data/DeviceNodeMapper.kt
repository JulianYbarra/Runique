package com.junka.core.connectivity.data

import com.google.android.gms.wearable.Node
import com.junka.core.connectivity.domain.DeviceNode

fun Node.toDeviceNode() = DeviceNode(
    id = id,
    displayName = displayName,
    isNearBy = isNearby
)