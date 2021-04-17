package io.github.kirillmokretsov.photogallery

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.github.kirillmokretsov.photogallery.api.FlickrApi

private const val TAG = "PollWorker"

class PollWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val lastResultId = QueryPreferences.getLastResultId(context)
        val items: List<GalleryItem> =
            FlickrFetchr(FlickrApi.newInstance()).fetchPhotosRequest(1).execute()
                .body()?.photos?.galleryItems ?: emptyList()
        return Result.success()
    }
}