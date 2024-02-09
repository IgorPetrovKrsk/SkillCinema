package igor.petrov.final_android_lvl1.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import igor.petrov.final_android_lvl1.databinding.RecyclerViewBottomCollectionsViewHolderBinding
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB
import igor.petrov.final_android_lvl1.entity.collectionDB.ItemCollectionDB

class BottomCollectionAdapter(private val onCollectionClicked: (CollectionsDB) -> Unit) : RecyclerView.Adapter<BottomCollectionViewHolder>() {
    private var data: MutableList<CollectionsDB>? = null
    private var filmCollections: MutableList<ItemCollectionDB>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<CollectionsDB>?, filmCollections: MutableList<ItemCollectionDB>) {
        this.data = data
        this.filmCollections = filmCollections
        notifyDataSetChanged()
    }

    fun deleteData(deletedCollection: CollectionsDB) {
        val position = this.data?.indexOf(deletedCollection)
        this.data?.remove(deletedCollection)
        notifyItemRemoved(position!!)
    }

    fun addNewData(newCollection: CollectionsDB) {
        this.data?.add(newCollection)
        notifyItemInserted(data?.size ?: 0)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomCollectionViewHolder {
        val binding = RecyclerViewBottomCollectionsViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BottomCollectionViewHolder(binding)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: BottomCollectionViewHolder, position: Int) {
        val collection = data?.getOrNull(position)
        if (collection != null) {
            holder.binding.checkBoxBottomCollectionSelect.text = collection.collectionName
            holder.binding.textViewBottomCollectionSize.text = "${collection.collectionSize}"
            holder.binding.checkBoxBottomCollectionSelect.isChecked = filmCollections?.any { it.collectionId == collection.collectionId } ?: false
            holder.binding.checkBoxBottomCollectionSelect.setOnClickListener{
                if ((it as CheckBox).isChecked) {
                    holder.binding.textViewBottomCollectionSize.text = (holder.binding.textViewBottomCollectionSize.text.toString().toInt()+1).toString()
                }else{
                    holder.binding.textViewBottomCollectionSize.text = (holder.binding.textViewBottomCollectionSize.text.toString().toInt()-1).toString()
                }
                onCollectionClicked(collection)
            }
        }
    }


}