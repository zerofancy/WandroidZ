package top.ntutn.wandroidz.me

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.wandroidz.databinding.ItemMeUserBinding

class MeAdapter(callback: DiffUtil.ItemCallback<String>): ListAdapter<String, RecyclerView.ViewHolder>(callback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MeUserViewHolder(ItemMeUserBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MeUserViewHolder).onBind()
    }
}