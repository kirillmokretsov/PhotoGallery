package io.github.kirillmokretsov.photogallery

import androidx.paging.DataSource

class SearchDataSourceFactory(private val flickrFetchr: FlickrFetchr, var query: String) :
    DataSource.Factory<Int, GalleryItem>() {

    override fun create(): DataSource<Int, GalleryItem> = SearchDataSource(flickrFetchr, query)
}