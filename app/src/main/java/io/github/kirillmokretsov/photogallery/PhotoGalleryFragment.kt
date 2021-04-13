package io.github.kirillmokretsov.photogallery

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment(), ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private lateinit var photoRecyclerView: RecyclerView
    private var spanCountHasBeenSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoGalleryViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(PhotoGalleryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    private class PhotoHolder(itemTextView: TextView) : RecyclerView.ViewHolder(itemTextView) {
        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
    }

    private class PhotoAdapter :
        PagedListAdapter<GalleryItem, PhotoHolder>(GalleryItem.itemCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
            PhotoHolder(TextView(parent.context))

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = getItem(position)
            if (galleryItem != null) {
                holder.bindTitle(galleryItem.title)
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
