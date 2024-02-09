package igor.petrov.final_android_lvl1.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import igor.petrov.final_android_lvl1.databinding.LoadStateBinding

class PagedLoadStateAdapter : LoadStateAdapter<PagedLoadStateViewHolder>(){
        override fun onBindViewHolder(holder: PagedLoadStateViewHolder, loadState: LoadState) = Unit

        override fun onCreateViewHolder(
            parent: ViewGroup,
            loadState: LoadState
        ): PagedLoadStateViewHolder {
            val binding = LoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PagedLoadStateViewHolder(binding)
        }
    }
