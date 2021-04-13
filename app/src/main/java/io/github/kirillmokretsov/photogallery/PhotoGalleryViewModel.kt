package io.github.kirillmokretsov.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.kirillmokretsov.photogallery.api.FlickrApi
import java.util.concurrent.Executors

class PhotoGalleryViewModel : ViewModel() {

    private val galleryItemDataSourceFactory = GalleryItemDataSourceFactory(FlickrApi.newInstance())
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(30)
        .setPageSize(15)
        .setPrefetchDistance(9)
        .build()
    private val executor = Executors.newFixedThreadPool(5)

    val galleryItemPagedList: LiveData<PagedList<GalleryItem>> =
        LivePagedListBuilder(
            galleryItemDataSourceFactory,
            config
        ).setFetchExecutor(executor).build()

}