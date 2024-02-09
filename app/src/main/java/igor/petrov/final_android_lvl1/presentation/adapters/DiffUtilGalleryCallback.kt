package igor.petrov.final_android_lvl1.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import igor.petrov.final_android_lvl1.data.dto.PhotoDto
import igor.petrov.final_android_lvl1.entity.Photo

class DiffUtilGalleryCallback : DiffUtil.ItemCallback<Photo>(){
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.imageUrl == newItem.imageUrl

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem as PhotoDto == newItem as PhotoDto
}