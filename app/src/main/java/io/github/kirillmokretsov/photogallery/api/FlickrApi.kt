package io.github.kirillmokretsov.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {

    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=f056d948cffa64689b6eb4c46d46d5eb" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s" +
                "&per_page=42"
    )
    fun fetchPhotos(@Query("page") page: Int): Call<FlickrResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    companion object {
        fun newInstance(): FlickrApi =
            Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FlickrApi::class.java)
    }

}