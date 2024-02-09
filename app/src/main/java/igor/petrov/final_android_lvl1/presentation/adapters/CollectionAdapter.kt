package igor.petrov.final_android_lvl1.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.RecyclerViewCollectionsViewHolderBinding
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB

class CollectionAdapter (private val onCollectionClicked: (CollectionsDB) -> Unit, private val onDeleteCollectionClicked: (CollectionsDB) -> Unit): RecyclerView.Adapter<CollectionViewHolder>() {
    private var data: MutableList<CollectionsDB>? = null
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<CollectionsDB>?) {
        this.data = data
        notifyDataSetChanged()
    }
    fun deleteData(deletedCollection:CollectionsDB){
        val position = this.data?.indexOf(deletedCollection)
        this.data?.remove(deletedCollection)
        notifyItemRemoved(position!!)
    }
    fun addNewData(newCollection:CollectionsDB){
        this.data?.add(newCollection)
        notifyItemInserted(data?.size?:0)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = RecyclerViewCollectionsViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CollectionViewHolder(binding)
    }

    override fun getItemCount(): Int = data?.size?:0

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collection = data?.getOrNull(position)
        if (collection!=null){
            holder.binding.textViewCollectionName.text = collection.collectionName
            holder.binding.textViewCollectionSize.text = "  ${collection.collectionSize}  "
            holder.binding.btnDeleteCollection.visibility = if (collection.buildIn) View.GONE else View.VISIBLE
            holder.binding.imageViewCollectionIcon.setImageResource(when (collection.collectionName){
                "Favorites" -> R.drawable.ic_heart
                "View later" -> R.drawable.ic_bookmark
                else -> R.drawable.ic_user
            })

            holder.binding.btnDeleteCollection.setOnClickListener {
                onDeleteCollectionClicked(collection)
            }

            holder.binding.btnOpenCollection.setOnClickListener {
                onCollectionClicked(collection)
            }
        }
    }


}