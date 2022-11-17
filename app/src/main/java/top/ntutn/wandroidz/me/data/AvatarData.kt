package top.ntutn.wandroidz.me.data

import top.ntutn.wandroidz.model.UserInfoBean

data class AvatarData(
    val userInfoBean: UserInfoBean?
): IMeListItemData {
    override fun contentEquals(data: IMeListItemData): Boolean {
        if (data !is AvatarData) {
            return false
        }
        return data.userInfoBean == userInfoBean
    }
}