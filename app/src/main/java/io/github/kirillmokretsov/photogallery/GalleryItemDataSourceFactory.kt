package io.github.kirillmokretsov.photogallery

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.github.kirillmokretsov.photogallery.api.FlickrApi

class GalleryItemDataSourceFactory(private val flickrApi: FlickrApi) : DataSource.Factory<Int, GalleryItem>() {

    private var mutableLiveData = MutableLiveData<GalleryItemDataSource>()

    override fun create(): DataSource<Int, GalleryItem> {
        val dataSource = GalleryItemDataSource(flickrApi)
        mutableLiveData.postValue(dataSource)
        return dataSource
    }
}