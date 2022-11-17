package top.ntutn.wandroidz.me

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.wandroidz.me.data.AvatarData
import top.ntutn.wandroidz.me.data.ClickableItemData
import top.ntutn.wandroidz.me.data.IMeListItemData

class MeAdapter(callback: DiffUtil.ItemCallback<IMeListItemData>): ListAdapter<IMeListItemData, MeAdapter.ViewHolder>(callback) {
    private val itemBuilders = buildMap<Class<out IMeListItemData>, (ViewGroup) -> ViewHolder> {
        put(AvatarData::class.java, ::MeUserViewHolder)
        put(ClickableItemData::class.java, ::NormalClickableItemViewHolder)
    }

    abstract class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract fun onBind(data: IMeListItemData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemBuilders.forEach {
            if (it.key.hashCode() == viewType) {
                return it.value(parent)
            }
        }
        return NormalClickableItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val dataClass = getItem(position).javaClass // fixme 这样还是有可能重复啊
        return dataClass.hashCode()
    }
}