package top.ntutn.wandroidz

import android.widget.Toast
import androidx.core.text.HtmlCompat
import top.ntutn.wandroidz.databinding.ItemMainItemBinding

class MainItemViewHolder(private val binding: ItemMainItemBinding): MainListAdapter.ViewHolder(binding.root) {
    override fun onBind(position: Int, data: IMainListItem) {
        super.onBind(position, data)
        val data = (data as IMainListItem.NormalItem).data
        binding.title.text = HtmlCompat.fromHtml(data.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
        binding.shareUser.text = "分享人：${data.shareUser}"
        binding.title.setOnClickListener {
            kotlin.runCatching {
                WebViewActivity.actionStart(it.context, data.link)
            }.onFailure {
                Toast.makeText(binding.root.context, "打开链接失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}