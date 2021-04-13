package io.github.kirillmokretsov.photogallery

import androidx.paging.PageKeyedDataSource
import io.github.kirillmokretsov.photogallery.api.FlickrApi

class GalleryItemDataSource(flickrApi: FlickrApi) :
    PageKeyedDataSource<Int, GalleryItem>() {

    private val flickrFetchr = FlickrFetchr(flickrApi)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GalleryItem>
    ) {
        flickrFetchr.fetchPhotos(1, callback)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GalleryItem>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GalleryItem>
    ) {
        flickrFetchr.fetchPhotos(params.key, callback)
    }

}