package top.ntutn.wandroidz.smartavatar

import android.net.Uri
import com.tencent.mmkv.MMKV

/**
 * 对于未记录过的作者，加载favicon
 * todo 对于记录过的作者，加载记录头像
 */
object AvatarHelper {
    private val kv by lazy {
        MMKV.mmkvWithID("user_avatar")
    }

    fun getAvatarUrl(user: String?, link: String): String {
        val stored = kv.decodeString(user)
        return stored ?: getFaviconUrl(link)
    }

    private fun getFaviconUrl(url: String): String {
        return Uri.parse(url)
            .buildUpon()
            .path("/favicon.ico")
            .build()
            .toString()
    }

    fun saveAvatarUrl(user: String, url: String) {
        kv.encode(user, url)
    }
}