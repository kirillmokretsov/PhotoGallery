package io.github.kirillmokretsov.photogallery

import androidx.paging.PageKeyedDataSource

class SearchDataSource(private val flickrFetchr: FlickrFetchr, private val query: String) :
    PageKeyedDataSource<Int, GalleryItem>() {

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