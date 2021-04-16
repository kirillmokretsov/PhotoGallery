package io.github.kirillmokretsov.photogallery

import androidx.paging.PageKeyedDataSource

class GalleryItemDataSource(private val flickrFetchr: FlickrFetchr) :
    PageKeyedDataSource<Int, GalleryItem>() {

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