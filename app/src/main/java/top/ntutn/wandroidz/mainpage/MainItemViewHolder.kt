package top.ntutn.wandroidz.mainpage

import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import top.ntutn.wandroidz.R
import top.ntutn.wandroidz.util.RoundedCornerCenterCrop
import top.ntutn.wandroidz.web.WebViewActivity
import top.ntutn.wandroidz.databinding.ItemMainItemBinding
import top.ntutn.wandroidz.smartavatar.AvatarHelper
import top.ntutn.wandroidz.util.TimeUtil
import top.ntutn.wandroidz.util.dp

class MainItemViewHolder(private val binding: ItemMainItemBinding): RecommendListAdapter.ViewHolder(binding.root) {
    override fun onBind(position: Int, item: IMainListItem) {
        super.onBind(position, item)
        val data = (item as IMainListItem.ArticleItem).data

        binding.container.background = ColorDrawable("#66D2624A".toColorInt()).takeIf { item.isTop }

        binding.title.text = HtmlCompat.fromHtml(data.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
        data.author?.takeIf { it.isNotBlank() }?.run {
            binding.author.text = "作者：$this"
        }?: data.shareUser?.run {
            binding.author.text = "分享人：$this"
        }?: kotlin.run {
            binding.author.text = ""
        }
        data.publishTime?.let {
            binding.time.text = TimeUtil.getUserFriendlyTime(it)
        }?: data.shareDate?.let {
            binding.time.text = TimeUtil.getUserFriendlyTime(it)
        }?: kotlin.run {
            binding.time.text = ""
        }
        binding.root.setOnClickListener {
            kotlin.runCatching {
                WebViewActivity.showArticle(
                    it.context,
                    data.author?.takeIf { it.isNotBlank() } ?: data.shareUser,
                    data.link)
            }.onFailure {
                Toast.makeText(binding.root.context, "打开链接失败", Toast.LENGTH_SHORT).show()
            }
        }
        val url = AvatarHelper.getAvatarUrl(data.author?.takeIf { it.isNotBlank() } ?: data.shareUser, data.link)
        Glide.with(binding.icon)
            .load(url)
            .error(R.mipmap.ic_launcher)
            .transform(RoundedCornerCenterCrop(4.dp))
            .into(binding.icon)
    }
}