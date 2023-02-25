package top.ntutn.wandroidz.smartavatar

import android.net.Uri
import org.jsoup.Jsoup
import timber.log.Timber

class CSDNSniffer(nextSniffer: ArticleAvatarSniffer?): ArticleAvatarSniffer(nextSniffer) {
    override fun match(url: String): Boolean {
        val uri = Uri.parse(url)
        if (uri.authority?.contains("blog.csdn.net") == true) {
            Timber.i("匹配url %s 为掘金文章", url)
            return true
        }
        return false
    }

    override fun handle(url: String, html: String): String? {
        val document = Jsoup.parse(html)
        document.selectFirst(".avatar-box > a:nth-child(1) > img:nth-child(1)")?.let {
            return it.attr("src")
        }
        return null
    }
}