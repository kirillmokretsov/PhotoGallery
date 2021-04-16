package io.github.kirillmokretsov.photogallery

import androidx.paging.PageKeyedDataSource
import io.github.kirillmokretsov.photogallery.api.FlickrApi

class SearchDataSource(flickrApi: FlickrApi, private val query: String) : PageKeyedDataSource<Int, GalleryItem>() {

    private val flickrFetchr = FlickrFetchr(flickrApi)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GalleryItem>
    ) {
        flickrFetchr.searchPhotos(1, callback, query)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GalleryItem>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GalleryItem>) {
        flickrFetchr.searchPhotos(params.key, callback, query)
    }
}