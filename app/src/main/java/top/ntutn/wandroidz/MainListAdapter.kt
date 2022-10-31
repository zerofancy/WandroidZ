package top.ntutn.wandroidz

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import top.ntutn.wandroidz.model.RecommendDataModel

class MainListAdapter(private val loadMoreListener: ()-> Unit): ListAdapter<IMainListItem, MainListAdapter.ViewHolder>(IMainListItem.DiffCallback()) {
    enum class Type {
        CONTENT,
        FOOTER
    }

    abstract class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    class NormalViewHolder(view: View): ViewHolder(view)

    class FooterViewHolder(view: View): ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == Type.FOOTER.ordinal) {
            FooterViewHolder(TextView(parent.context))
        } else {
            NormalViewHolder(TextView(parent.context))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView as TextView
        when (getItemViewType(position)) {
            Type.CONTENT.ordinal -> {
                val data = getItem(position) as IMainListItem.NormalItem
                view.text = data.data.title
                view.setOnClickListener {
                    kotlin.runCatching {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.data.link))
                        it.context.startActivity(intent)
                    }.onFailure {
                        Toast.makeText(view.context, "打开链接失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Type.FOOTER.ordinal -> {
                view.text = "*****************没有更多了*******************"
                view.setOnClickListener(null)
            }
            else -> throw NotImplementedError()
        }

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