package com.junka.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bson.types.ObjectId

@Entity
data class RunEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = ObjectId().toHexString(), //mongodb id formatted
    val durationMillis: Long,
    val distanceMeters: Int,
    val dateTimeUtc: String,
    val latitude: Double,
    val longitude: Double,
    val avgSpeedKmH: Double,
    val maxSpeedKmH: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String,
    val avgHeartRate: Int?,
    val maxHeartRate: Int?
)