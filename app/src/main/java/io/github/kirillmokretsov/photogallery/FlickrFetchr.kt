package io.github.kirillmokretsov.photogallery

import android.util.Log
import androidx.paging.PageKeyedDataSource
import io.github.kirillmokretsov.photogallery.api.FlickrApi
import io.github.kirillmokretsov.photogallery.api.FlickrResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "FlickrFetch"

class FlickrFetchr(private val flickrApi: FlickrApi) {

    fun fetchPhotos(
        page: Int,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, GalleryItem>
    ) {
        fetchPhotoMetadata(page, callback, flickrApi.fetchPhotos(page))
    }

    fun fetchPhotos(page: Int, callback: PageKeyedDataSource.LoadCallback<Int, GalleryItem>) {
        fetchPhotoMetadata(page, callback, flickrApi.fetchPhotos(page))
    }

    fun searchPhotos(
        page: Int,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, GalleryItem>,
        query: String
    ) {
        fetchPhotoMetadata(page, callback, flickrApi.searchPhotos(query))
    }

    fun searchPhotos(
        page: Int,
        callback: PageKeyedDataSource.LoadCallback<Int, GalleryItem>,
        query: String
    ) {
        fetchPhotoMetadata(page, callback, flickrApi.searchPhotos(query))
    }

    private fun fetchPhotoMetadata(
        page: Int,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, GalleryItem>,
        flickrRequest: Call<FlickrResponse>
    ) {

        flickrRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                Log.d(TAG, "Response received: ${response.body()}")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                callback.onResult(galleryItems, if (page == 1) null else page, page + 1)
            }
        })
    }

    private fun fetchPhotoMetadata(
        page: Int,
        callback: PageKeyedDataSource.LoadCallback<Int, GalleryItem>,
        flickrRequest: Call<FlickrResponse>
    ) {
        flickrRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                Log.d(TAG, "Response received: ${response.body()}")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                callback.onResult(galleryItems, page + 1)
            }
        })
    }

}