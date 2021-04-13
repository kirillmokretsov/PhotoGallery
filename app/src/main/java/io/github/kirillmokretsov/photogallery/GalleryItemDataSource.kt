package io.github.kirillmokretsov.photogallery

import android.content.Context
import androidx.paging.PageKeyedDataSource
import io.github.kirillmokretsov.photogallery.api.FlickrApi

class GalleryItemDataSource(flickrApi: FlickrApi, context: Context) :
    PageKeyedDataSource<Int, GalleryItem>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GalleryItem>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GalleryItem>) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GalleryItem>) {
        TODO("Not yet implemented")
    }

}