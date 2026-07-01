package com.aistudio.ultimate.features.models

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class ModelDownloadWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val modelId = inputData.getString(KEY_MODEL_ID) ?: return Result.failure()
        
        // Simulate download progress
        for (i in 0..100 step 10) {
            setProgress(workDataOf(KEY_PROGRESS to i))
            delay(500)
        }

        return Result.success(workDataOf(KEY_MODEL_ID to modelId))
    }

    companion object {
        const val KEY_MODEL_ID = "model_id"
        const val KEY_PROGRESS = "progress"
    }
}
