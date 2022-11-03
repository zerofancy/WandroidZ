package top.ntutn.wandroidz.mainpage

import androidx.recyclerview.widget.DiffUtil
import top.ntutn.wandroidz.model.BannerDataModel
import top.ntutn.wandroidz.model.RecommendDataModel

interface IMainListItem {
    data class NormalItem(val data: RecommendDataModel): IMainListItem
    class BannerItem(val data: BannerDataModel): IMainListItem
    class FooterItem(): IMainListItem

    class DiffCallback: DiffUtil.ItemCallback<IMainListItem>() {
        override fun areItemsTheSame(oldItem: IMainListItem, newItem: IMainListItem): Boolean {
            if (oldItem is NormalItem && newItem is NormalItem) {
                return oldItem.data.id == newItem.data.id
            }
            if (oldItem.javaClass != newItem.javaClass) {
                return false
            }
            return true
        }

        override fun areContentsTheSame(oldItem: IMainListItem, newItem: IMainListItem): Boolean {
            if (oldItem.javaClass != newItem.javaClass) {
                return false
            }
            if (oldItem is BannerItem && newItem is BannerItem) {
                return oldItem.data == newItem.data
            }
            if (oldItem is FooterItem && newItem is FooterItem) {
                return true
            }
            if (oldItem is NormalItem && newItem is NormalItem) {
                return oldItem.data == newItem.data
            }
            return false
        }
    }
}