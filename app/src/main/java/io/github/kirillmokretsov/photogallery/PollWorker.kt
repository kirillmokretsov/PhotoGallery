package io.github.kirillmokretsov.photogallery

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.github.kirillmokretsov.photogallery.api.FlickrApi

private const val TAG = "PollWorker"

class PollWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val lastResultId = QueryPreferences.getLastResultId(context)
        val items: List<GalleryItem>
        try {
            items =
                FlickrFetchr(FlickrApi.newInstance()).fetchPhotosRequest(1).execute()
                    .body()?.photos?.galleryItems ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get items", e)
            return Result.failure()
        }

        if (items.isEmpty())
            return Result.success()

        val resultId = items.first().id
        if (resultId == lastResultId) {
            Log.i(TAG, "Got and old result: $resultId")
        } else {
            Log.i(TAG, "Got a new result: $resultId")
            QueryPreferences.setLastResultId(context, resultId)
        }

        return Result.success()
    }
}