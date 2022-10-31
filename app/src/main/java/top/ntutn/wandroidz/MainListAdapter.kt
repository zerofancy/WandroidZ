package top.ntutn.wandroidz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.wandroidz.databinding.ItemMainItemBinding

class MainListAdapter(private val loadMoreListener: ()-> Unit): ListAdapter<IMainListItem, MainListAdapter.ViewHolder>(IMainListItem.DiffCallback()) {
    enum class Type {
        CONTENT,
        FOOTER
    }

    abstract class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        open fun onBind(position: Int, data: IMainListItem) {}
    }

    class FooterViewHolder(view: View): ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == Type.FOOTER.ordinal) {
            FooterViewHolder(TextView(parent.context))
        } else {
            MainItemViewHolder(ItemMainItemBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView
        when (getItemViewType(position)) {
            Type.CONTENT.ordinal -> {

            }
            Type.FOOTER.ordinal -> {
                view as TextView
                view.text = "*****************没有更多了*******************"
                view.setOnClickListener(null)
            }
            else -> throw NotImplementedError()
        }
        holder.onBind(position, getItem(position))
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        // load more when item is footer
        if (holder is FooterViewHolder) {
             loadMoreListener()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is IMainListItem.NormalItem) {
            return Type.CONTENT.ordinal
        }
        return Type.FOOTER.ordinal
    }
}