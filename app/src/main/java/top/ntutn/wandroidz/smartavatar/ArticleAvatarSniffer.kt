package top.ntutn.wandroidz.smartavatar

/**
 * 用于寻找作者头像
 */
abstract class ArticleAvatarSniffer(private val nextSniffer: ArticleAvatarSniffer?) {
    abstract fun match(url: String): Boolean

    fun sniff(url: String, html: String): String? {
        if (!match(url)) {
            return nextSniffer?.sniff(url, html)
        }
        return handle(url, html)
    }

    protected abstract fun handle(url: String, html: String): String?
}