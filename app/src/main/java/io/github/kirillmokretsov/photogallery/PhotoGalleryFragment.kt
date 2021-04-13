package io.github.kirillmokretsov.photogallery

import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment(), ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>
    private var spanCountHasBeenSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true // for more simple configuration

        photoGalleryViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(PhotoGalleryViewModel::class.java)

        val responseHandler = Handler()
        thumbnailDownloader =
            ThumbnailDownloader(responseHandler) { photoHolder, bitmap ->
                val drawable = BitmapDrawable(resources, bitmap)
                photoHolder.bindDrawable(drawable)
            }
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycle.addObserver(
            thumbnailDownloader.viewLifecyclerObserver
        )
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            photoRecyclerView.layoutManager = GridLayoutManager(context, 3)
        else
            photoRecyclerView.layoutManager = GridLayoutManager(context, 6)
        photoRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(this)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.galleryItemPagedList.observe(
            viewLifecycleOwner,
            { galleryItems ->
                val photoAdapter = PhotoAdapter()
                photoAdapter.submitList(galleryItems)
                photoRecyclerView.adapter = photoAdapter
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.removeObserver(thumbnailDownloader.viewLifecyclerObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    class PhotoHolder(itemImageView: ImageView) : RecyclerView.ViewHolder(itemImageView) {
        val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable
    }

    private inner class PhotoAdapter :
        PagedListAdapter<GalleryItem, PhotoHolder>(GalleryItem.itemCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
            PhotoHolder(
                layoutInflater.inflate(
                    R.layout.list_item_gallery,
                    parent,
                    false
                ) as ImageView
            )

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = getItem(position)
            if (galleryItem != null) {
                thumbnailDownloader.queueThumbnail(holder, galleryItem.url)
                holder.bindDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.empty)
                        ?: ColorDrawable()
                )
            }
        }

    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }

    override fun onGlobalLayout() {
        if (!spanCountHasBeenSet) {
            val width = resources.configuration.screenWidthDp
            val spanCount = width.div(100)
            Log.d(TAG, "params.width = $width; span count = $spanCount")
            photoRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
            spanCountHasBeenSet = true
        }
    }

}
