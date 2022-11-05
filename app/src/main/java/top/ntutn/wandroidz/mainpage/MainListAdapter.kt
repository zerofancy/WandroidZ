package top.ntutn.wandroidz.mainpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.wandroidz.databinding.ItemMainBannerBinding
import top.ntutn.wandroidz.databinding.ItemMainFooterBinding
import top.ntutn.wandroidz.databinding.ItemMainItemBinding

class MainListAdapter(private val loadMoreListener: ()-> Unit): ListAdapter<IMainListItem, MainListAdapter.ViewHolder>(
    IMainListItem.DiffCallback()
) {
    enum class Type {
        BANNER,
        CONTENT,
        FOOTER,
    }

    abstract class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        open fun onBind(position: Int, data: IMainListItem) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Type.BANNER.ordinal -> BannerViewHolder(ItemMainBannerBinding.inflate(layoutInflater, parent, false))
            Type.CONTENT.ordinal -> MainItemViewHolder(ItemMainItemBinding.inflate(layoutInflater, parent, false))
            Type.FOOTER.ordinal -> FooterViewHolder(ItemMainFooterBinding.inflate(layoutInflater, parent, false))
            else -> throw NotImplementedError()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position, getItem(position))
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        // load more when item is footer
        if (holder is FooterViewHolder) {
             loadMoreListener()
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
            is IMainListItem.ArticleItem -> Type.CONTENT.ordinal
            is IMainListItem.BannerItem -> Type.BANNER.ordinal
            is IMainListItem.FooterItem -> Type.FOOTER.ordinal
            else -> throw NotImplementedError()
    }
}