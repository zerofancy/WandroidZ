package top.ntutn.wandroidz.mainpage

import com.bumptech.glide.Glide
import top.ntutn.wandroidz.WebViewActivity
import top.ntutn.wandroidz.databinding.ItemMainBannerBinding

class BannerViewHolder(private val binding: ItemMainBannerBinding): MainListAdapter.ViewHolder(binding.root) {
    override fun onBind(position: Int, data: IMainListItem) {
        super.onBind(position, data as IMainListItem.BannerItem)
        if (data.data.isEmpty()) {
            binding.image.background = null
            binding.root.setOnClickListener(null)
            binding.root.isClickable = false
            return
        }
        val item = data.data.random()
        Glide.with(binding.image)
            .load(item.imagePath)
            .into(binding.image)
        binding.root.setOnClickListener {
            WebViewActivity.actionStart(it.context, item.url)
        }
    }
}