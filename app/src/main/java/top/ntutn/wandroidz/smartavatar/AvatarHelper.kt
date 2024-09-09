package top.ntutn.wandroidz.smartavatar

import android.net.Uri
import com.tencent.mmkv.MMKV

/**
 * 对于未记录过的作者，加载favicon
 * 对于记录过的作者，加载记录头像
 */
object AvatarHelper {
    private val kv by lazy {
        MMKV.mmkvWithID("user_avatar")
    }

    fun getAvatarUrl(user: String?, link: String): String {
        val stored = kv.decodeString(user)
        return stored ?: getFaviconUrl(link)
    }

    /**
     * 获取图标服务来自 https://github.com/seadfeng/favicons-proxy
     */
    private fun getFaviconUrl(url: String): String {
        val uri = Uri.parse(url)
        return Uri.parse("https://favicons.seadfeng.workers.dev/juejin.cn.ico")
            .buildUpon()
            .path(uri.host + ".ico")
            .toString()
    }

    fun saveAvatarUrl(user: String, url: String) {
        kv.encode(user, url)
    }
}