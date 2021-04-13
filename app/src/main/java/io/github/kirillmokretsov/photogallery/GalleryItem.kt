package io.github.kirillmokretsov.photogallery

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class GalleryItem(
    var title: String = "",
    var id: String = "",
    @SerializedName("url_s") var url: String = ""
) {
    val itemCallback: DiffUtil.ItemCallback<GalleryItem> = CustomItemCallback()

    class CustomItemCallback : DiffUtil.ItemCallback<GalleryItem>() {
        override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
            oldItem.url == newItem.url


        override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
            oldItem.title == newItem.title && oldItem.id == newItem.id
    }
}
