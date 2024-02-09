package igor.petrov.final_android_lvl1.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import igor.petrov.final_android_lvl1.databinding.RecyclerViewStaffViewHolderBinding
import igor.petrov.final_android_lvl1.entity.Staff

class StaffAdapter(private val onStaffClicked: (Staff) -> Unit):Adapter<StaffViewHolder>() {

    private var data: List<Staff> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Staff>?) {
        this.data = data ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val binding = RecyclerViewStaffViewHolderBinding.inflate(LayoutInflater.from(parent.context))
        return StaffViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staff = data.getOrNull(position)
        if (staff!=null){
            holder.binding.textViewActorsName.text = if (staff.nameRu.isNullOrBlank()) staff.nameEn else staff.nameRu
            holder.binding.textViewActorsRole.text = if (staff.description.isNullOrBlank()) staff.professionText else staff.description
            Glide.with(holder.binding.root).load(staff.posterUrl).into(holder.binding.imageViewActorPhoto)
            holder.binding.imageViewActorPhoto.setOnClickListener {
                onStaffClicked(staff)
            }
        }
    }

}

