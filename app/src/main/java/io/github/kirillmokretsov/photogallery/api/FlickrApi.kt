package io.github.kirillmokretsov.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    @GET(
            "services/rest/?method=flickr.interestingness.getList" +
                    "&api_key=f056d948cffa64689b6eb4c46d46d5eb" +
                    "&format=json" +
                    "&nojsoncallback=1" +
                    "&extras=url_s"
    )
    fun fetchPhotos(): Call<FlickrResponse>

}