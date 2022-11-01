package top.ntutn.wandroidz

import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import top.ntutn.wandroidz.databinding.ItemMainItemBinding
import top.ntutn.wandroidz.smartavatar.AvatarHelper
import top.ntutn.wandroidz.util.dp

class MainItemViewHolder(private val binding: ItemMainItemBinding): MainListAdapter.ViewHolder(binding.root) {
    override fun onBind(position: Int, data: IMainListItem) {
        super.onBind(position, data)
        val data = (data as IMainListItem.NormalItem).data
        binding.title.text = HtmlCompat.fromHtml(data.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
        binding.shareUser.text = "分享人：${data.shareUser}"
        binding.root.setOnClickListener {
            kotlin.runCatching {
                WebViewActivity.showArticle(it.context, data.shareUser, data.link)
            }.onFailure {
                Toast.makeText(binding.root.context, "打开链接失败", Toast.LENGTH_SHORT).show()
            }
        }
        val url = AvatarHelper.getAvatarUrl(data.shareUser, data.link)
        Glide.with(binding.icon)
            .load(url)
            .fallback(R.mipmap.ic_launcher)
            .transform(RoundedCornerCenterCrop(4.dp))
            .into(binding.icon)
    }
}