package top.ntutn.wandroidz.mainpage

import com.bumptech.glide.Glide
import top.ntutn.wandroidz.web.WebViewActivity
import top.ntutn.wandroidz.databinding.ItemMainBannerBinding

class BannerViewHolder(private val binding: ItemMainBannerBinding): RecommendListAdapter.ViewHolder(binding.root) {
    override fun onBind(position: Int, data: IMainListItem) {
        super.onBind(position, data as IMainListItem.BannerItem)
        val item = data.data
        Glide.with(binding.image)
            .load(item.imagePath)
            .into(binding.image)
        binding.root.setOnClickListener {
            WebViewActivity.actionStart(it.context, item.url)
        }
    }
}