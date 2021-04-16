package io.github.kirillmokretsov.photogallery

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment(), ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private lateinit var photoRecyclerView: RecyclerView
    private var spanCountHasBeenSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true // for more simple configuration
        setHasOptionsMenu(true)

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
        spanCountHasBeenSet = false
        photoRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(this)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.searchPagedList.observe(
            viewLifecycleOwner,
            { galleryItems ->
                val photoAdapter = PhotoAdapter()
                photoAdapter.submitList(galleryItems)
                photoRecyclerView.adapter = photoAdapter
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {

            setQuery(photoGalleryViewModel.searchTerm, false)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $query")
                    if (query != null) {
                        photoGalleryViewModel.fetchPhotos(query)
                    }
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "QueryTextChange: $newText")
                    return false
                }

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                photoGalleryViewModel.fetchPhotos("")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class PhotoHolder(private val itemImageView: ImageView) : RecyclerView.ViewHolder(itemImageView) {
        fun bindGalleryItem(galleryItem: GalleryItem) {
            Picasso.get()
                .load(galleryItem.url)
                .placeholder(R.color.placeholder)
                .into(itemImageView)
        }
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
                holder.bindGalleryItem(galleryItem)
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
