package io.github.kirillmokretsov.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.kirillmokretsov.photogallery.api.FlickrApi
import java.util.concurrent.Executors

class PhotoGalleryViewModel : ViewModel() {

    private val flickrApi = FlickrApi.newInstance()
    private val flickrFetchr = FlickrFetchr(flickrApi)
    private val mutableSearchTerm = MutableLiveData<String>()
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(30)
        .setPageSize(15)
        .setPrefetchDistance(12)
        .build()

    private val galleryItemDataSourceFactory = GalleryItemDataSourceFactory(flickrFetchr)
    private val galleryItemExecutor = Executors.newFixedThreadPool(5)
    private val galleryItemPagedList: LiveData<PagedList<GalleryItem>> =
        LivePagedListBuilder(
            galleryItemDataSourceFactory,
            config
        ).setFetchExecutor(galleryItemExecutor).build()

    private val searchDataSourceFactory = SearchDataSourceFactory(flickrFetchr, "")
    private val searchExecutor = Executors.newFixedThreadPool(5)
    var searchPagedList: LiveData<PagedList<GalleryItem>> =
        LivePagedListBuilder(
            searchDataSourceFactory,
            config
        ).setFetchExecutor(searchExecutor).build()

    init {
        mutableSearchTerm.value = ""
        searchPagedList = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            if (searchTerm.isBlank()) {
                galleryItemPagedList
            } else {
                searchDataSourceFactory.query = searchTerm
                LivePagedListBuilder(
                    searchDataSourceFactory,
                    config
                ).setFetchExecutor(searchExecutor).build()
            }
        }
    }

    fun fetchPhotos(query: String = "") {
        mutableSearchTerm.value = query
    }

    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""


}