package io.github.kirillmokretsov.photogallery

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class GalleryItem(
    var title: String = "",
    var id: String = "",
    @SerializedName("url_s") var url: String = "",
    @SerializedName("owner") var owner: String = ""
) {
    val photoPageUri: Uri
        get() = Uri.parse("https://www.flickr.com/photos")
            .buildUpon()
            .appendPath(owner)
            .appendPath(id)
            .build()

    companion object {
        val itemCallback: DiffUtil.ItemCallback<GalleryItem> = CustomItemCallback()
    }

    class CustomItemCallback : DiffUtil.ItemCallback<GalleryItem>() {
        override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
            oldItem.url == newItem.url


        override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
            oldItem.title == newItem.title && oldItem.id == newItem.id
    }
}
