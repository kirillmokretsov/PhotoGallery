package io.github.kirillmokretsov.photogallery

import androidx.paging.DataSource
import io.github.kirillmokretsov.photogallery.api.FlickrApi

class GalleryItemDataSourceFactory(private val flickrFetchr: FlickrFetchr) : DataSource.Factory<Int, GalleryItem>() {

    override fun create(): DataSource<Int, GalleryItem> = GalleryItemDataSource(flickrFetchr)

}