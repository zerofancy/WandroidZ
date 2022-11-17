package top.ntutn.wandroidz.me.data

import android.view.View
import androidx.annotation.DrawableRes

data class ClickableItemData(
    @DrawableRes
    val iconRes: Int,
    val title: String,
    val clickAction: (View) -> Unit
): IMeListItemData {
    override fun contentEquals(data: IMeListItemData): Boolean {
        return data is ClickableItemData && data == this
    }
}