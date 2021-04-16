package io.github.kirillmokretsov.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.kirillmokretsov.photogallery.api.FlickrApi
import java.util.concurrent.Executors

class PhotoGalleryViewModel : ViewModel() {

    private val flickrApi = FlickrApi.newInstance()

    private val galleryItemDataSourceFactory = GalleryItemDataSourceFactory(flickrApi)
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(30)
        .setPageSize(15)
        .setPrefetchDistance(12)
        .build()
    private val galleryItemExecutor = Executors.newFixedThreadPool(5)
    val galleryItemPagedList: LiveData<PagedList<GalleryItem>> =
        LivePagedListBuilder(
            galleryItemDataSourceFactory,
            config
        ).setFetchExecutor(galleryItemExecutor).build()

    private val searchDataSourceFactory = SearchDataSourceFactory(flickrApi, "planets")
    private val searchExecutor = Executors.newFixedThreadPool(5)
    val searchPagedList: LiveData<PagedList<GalleryItem>> =
        LivePagedListBuilder(
            searchDataSourceFactory,
            config
        ).setFetchExecutor(searchExecutor).build()

}