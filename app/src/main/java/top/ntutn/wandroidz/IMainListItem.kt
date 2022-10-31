package top.ntutn.wandroidz

import androidx.recyclerview.widget.DiffUtil
import top.ntutn.wandroidz.model.RecommendDataModel

interface IMainListItem {
    data class NormalItem(val data: RecommendDataModel): IMainListItem
    class FooterItem(): IMainListItem

    class DiffCallback: DiffUtil.ItemCallback<IMainListItem>() {
        override fun areItemsTheSame(oldItem: IMainListItem, newItem: IMainListItem): Boolean {
            if (oldItem is FooterItem && newItem is FooterItem) {
                return true
            }
            if (oldItem is FooterItem || newItem is FooterItem) {
                return false
            }
            if (oldItem is NormalItem && newItem is NormalItem) {
                return oldItem.data.id == newItem.data.id
            }
            return false
        }

        override fun areContentsTheSame(oldItem: IMainListItem, newItem: IMainListItem): Boolean {
            if (oldItem is FooterItem && newItem is FooterItem) {
                return true
            }
            if (oldItem is FooterItem || newItem is FooterItem) {
                return false
            }
            if (oldItem is NormalItem && newItem is NormalItem) {
                return oldItem.data == newItem.data
            }
            return false
        }
    }
}