package top.ntutn.wandroidz.smartavatar

import android.net.Uri
import org.jsoup.Jsoup
import timber.log.Timber

class JuejinSniffer(nextSniffer: ArticleAvatarSniffer?): ArticleAvatarSniffer(nextSniffer) {
    override fun match(url: String): Boolean {
        val uri = Uri.parse(url)
        if (uri.authority?.contains("juejin.cn") == true && uri.path?.contains("post") == true) {
            Timber.i("匹配url %s 为掘金文章", url)
            return true
        }
        return false
    }

    override fun handle(url: String, html: String): String? {
        val document = Jsoup.parse(html)
        document.selectFirst("a.avatar-link>img")?.let {
            return it.attr("src")
        }
        return null
    }
}