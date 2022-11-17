package top.ntutn.wandroidz.me

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.wandroidz.databinding.ItemMeUserBinding
import top.ntutn.wandroidz.me.data.IMeListItemData

class MeAdapter(callback: DiffUtil.ItemCallback<IMeListItemData>): ListAdapter<IMeListItemData, MeAdapter.ViewHolder>(callback) {
    abstract class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract fun onBind(data: IMeListItemData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MeUserViewHolder(ItemMeUserBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}