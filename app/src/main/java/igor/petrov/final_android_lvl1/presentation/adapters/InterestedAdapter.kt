package igor.petrov.final_android_lvl1.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionForRecyclerView
import igor.petrov.final_android_lvl1.databinding.RecyclerViewFilmViewHolderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class InterestedAdapter(private val onItemClicked: (ItemCollectionForRecyclerView) -> Unit) : RecyclerView.Adapter<PagedFilmViewHolder>() {
    private var data: List<ItemCollectionForRecyclerView>? = null
    private val adapterScope = CoroutineScope(Dispatchers.Main)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ItemCollectionForRecyclerView>?) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedFilmViewHolder {
        val binding = RecyclerViewFilmViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagedFilmViewHolder(binding)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: PagedFilmViewHolder, position: Int) {
        val item = data?.getOrNull(position)
        if (item != null) {
            holder.binding.textViewFilmName.text = item.itemName
            holder.binding.textViewFilmGenre.text = item.itemDescription
            if ((item.itemRating ?: 0f) > 0f) {
                holder.binding.textViewFilmRating.text = item.itemRating.toString()
            } else {
                holder.binding.textViewFilmRating.visibility = View.GONE
            }
            when (item.itemName) {
                "Clear history" -> {
                    holder.binding.imageViewFilmPoster.scaleX = 0.4f
                    holder.binding.imageViewFilmPoster.scaleY = 0.4f
                    Glide.with(holder.binding.root).load(R.drawable.ic_trash).into(holder.binding.imageViewFilmPoster)
                }
                "No films :-(" -> {
                    holder.binding.imageViewFilmPoster.scaleX = 0.8f
                    holder.binding.imageViewFilmPoster.scaleY = 0.8f
                    Glide.with(holder.binding.root).load(R.drawable.ic_folder_empty).into(holder.binding.imageViewFilmPoster)
                }
                else -> {
                    holder.binding.imageViewFilmPoster.scaleX = 1f
                    holder.binding.imageViewFilmPoster.scaleY = 1f
                    Glide.with(holder.binding.root).load(item.posterUrl).into(holder.binding.imageViewFilmPoster)
                }
            }
            holder.binding.imageViewFilmPoster.setOnClickListener {
                onItemClicked(item)
            }
        }
    }
}


