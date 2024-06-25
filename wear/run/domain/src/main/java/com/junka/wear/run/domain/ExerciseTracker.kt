package com.junka.wear.run.domain

import com.junka.core.domain.util.EmptyResult

import kotlinx.coroutines.flow.Flow

interface ExerciseTracker {

    val hearRate: Flow<Int>
    suspend fun isHearTrackingSupported(): Boolean
    suspend fun prepareExercise(): EmptyResult<ExerciseError>
    suspend fun startExercise() : EmptyResult<ExerciseError>
    suspend fun resumeExercise() : EmptyResult<ExerciseError>
    suspend fun pauseExercise() : EmptyResult<ExerciseError>
    suspend fun stopExercise() : EmptyResult<ExerciseError>

}