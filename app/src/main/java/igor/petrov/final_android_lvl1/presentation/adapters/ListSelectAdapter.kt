package igor.petrov.final_android_lvl1.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import igor.petrov.final_android_lvl1.App
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.RecyclerViewListSelectViewHolderBinding

class ListSelectAdapter(private val onItemClicked: (String) -> Unit):Adapter<ListSelectViewHolder>() {

    private var data: List<String> = emptyList()
    var selectedData: String? = null
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>?) {
        this.data = (data ?: emptyList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectViewHolder {
        val binding = RecyclerViewListSelectViewHolderBinding.inflate(LayoutInflater.from(parent.context))
        return ListSelectViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ListSelectViewHolder, position: Int) {
        val text = data.getOrNull(position)
        if (text!=null){
            if (text== selectedData) {
                holder.binding.textView.setBackgroundColor(App.applicationContext().getColor(R.color.main_purple))
                holder.binding.textView.setTextColor(App.applicationContext().getColor(R.color.white))
            }else{
                holder.binding.textView.setBackgroundColor(App.applicationContext().getColor(R.color.white))
                holder.binding.textView.setTextColor(App.applicationContext().getColor(R.color.black))
            }
            holder.binding.textView.text = text
            holder.binding.textView.setOnClickListener {
                onItemClicked(text)
            }
        }
    }

}

