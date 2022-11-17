package top.ntutn.wandroidz.me

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import top.ntutn.wandroidz.databinding.ItemNormalClickableBinding
import top.ntutn.wandroidz.me.data.ClickableItemData
import top.ntutn.wandroidz.me.data.IMeListItemData

class NormalClickableItemViewHolder(val parent: ViewGroup): MeAdapter.ViewHolder(FrameLayout(parent.context)) {
    val binding: ItemNormalClickableBinding

    init {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemNormalClickableBinding.inflate(inflater, itemView as FrameLayout, true)
    }

    override fun onBind(data: IMeListItemData) {
        if (data !is ClickableItemData) {
            return
        }
        binding.icon.setImageResource(data.iconRes)
        binding.title.text = data.title
        binding.root.setOnClickListener(data.clickAction)
    }
}