package io.github.kirillmokretsov.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.kirillmokretsov.photogallery.api.FlickrApi

class PhotoGalleryViewModel : ViewModel() {

    val galleryItemLiveData: LiveData<List<GalleryItem>> = FlickrFetchr(FlickrApi.newInstance()).fetchPhotos()

}