package io.github.kirillmokretsov.photogallery

import androidx.paging.DataSource
import io.github.kirillmokretsov.photogallery.api.FlickrApi

class SearchDataSourceFactory(private val flickrApi: FlickrApi, var query: String) :
    DataSource.Factory<Int, GalleryItem>() {

    override fun create(): DataSource<Int, GalleryItem> = SearchDataSource(flickrApi, query)
}