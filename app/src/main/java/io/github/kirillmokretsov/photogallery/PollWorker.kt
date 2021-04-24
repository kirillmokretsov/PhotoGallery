package io.github.kirillmokretsov.photogallery

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.github.kirillmokretsov.photogallery.api.FlickrApi

private const val TAG = "PollWorker"

class PollWorker(private val context: Context, workerParams: WorkerParameters) :
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

            val intent = PhotoGalleryActivity.newIntent(context)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val resources = context.resources
            val notification = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(pendingIntent)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(0, notification)

            context.sendBroadcast(Intent(ACTION_SHOW_NOTIFICATION))
        }

        return Result.success()
    }

    companion object {
        const val ACTION_SHOW_NOTIFICATION =
            "io.github.kirillmokretsov.photogallery.SHOW_NOTIFICATION"
    }

}