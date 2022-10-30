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

class MainListAdapter: ListAdapter<RecommendDataModel, MainListAdapter.ViewHolder>(RecommendDataModel.DiffCallback()) {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent.context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView as TextView
        view.text = getItem(position).title
        view.setOnClickListener {
            kotlin.runCatching {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getItem(position).link))
                it.context.startActivity(intent)
            }.onFailure {
                Toast.makeText(view.context, "打开链接失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}