package igor.petrov.final_android_lvl1.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import igor.petrov.final_android_lvl1.databinding.RecyclerViewGalleryViewHolderBinding
import igor.petrov.final_android_lvl1.entity.Photo

class GalleryAdapter(private val onPhotoClicked: (Photo,Int) -> Unit) : PagingDataAdapter<Photo, PagedGalleryViewHolder>(DiffUtilGalleryCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedGalleryViewHolder {
        val binding = RecyclerViewGalleryViewHolderBinding.inflate(LayoutInflater.from(parent.context))
        return PagedGalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagedGalleryViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) {
            Glide.with(holder.binding.root).load(photo.previewUrl).into(holder.binding.imageViewPhoto)
            holder.binding.imageViewPhoto.setOnClickListener {
                onPhotoClicked(photo,position)
            }

        }
    }
}



