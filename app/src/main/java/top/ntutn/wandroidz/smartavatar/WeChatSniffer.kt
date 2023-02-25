package top.ntutn.wandroidz.smartavatar

import android.net.Uri
import timber.log.Timber
import java.util.regex.Pattern

class WeChatSniffer(nextSniffer: ArticleAvatarSniffer?): ArticleAvatarSniffer(nextSniffer) {
    private val pattern = Pattern.compile("var ori_head_img_url = \"(.*?)\"")

    override fun match(url: String): Boolean {
        val uri = Uri.parse(url)
        if (uri.authority?.contains("mp.weixin.qq.com") == true) {
            Timber.i("匹配url %s 为微信公众号文章", url)
            return true
        }
        return false
    }

    override fun handle(url: String, html: String): String? {
        val matcher = pattern.matcher(html)
        if (matcher.find()) {
            return matcher.group(1)
        }
        return null
    }
}