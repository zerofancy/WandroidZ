package top.ntutn.wandroidz.mainpage

import androidx.recyclerview.widget.DiffUtil
import top.ntutn.wandroidz.model.BannerDataModel
import top.ntutn.wandroidz.model.ArticleDataModel

interface IMainListItem {
    data class ArticleItem(val data: ArticleDataModel, val isTop: Boolean = false): IMainListItem
    class BannerItem(val data: BannerDataModel): IMainListItem
    class FooterItem(): IMainListItem

    class DiffCallback: DiffUtil.ItemCallback<IMainListItem>() {
        override fun areItemsTheSame(oldItem: IMainListItem, newItem: IMainListItem): Boolean {
            if (oldItem is ArticleItem && newItem is ArticleItem) {
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
            if (oldItem is ArticleItem && newItem is ArticleItem) {
                return oldItem.data == newItem.data
            }
            return false
        }
    }
}