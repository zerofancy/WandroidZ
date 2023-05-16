package top.ntutn.wandroidz.web

import android.net.Uri
import org.jsoup.Jsoup
import timber.log.Timber
import java.io.File

class JuejinArticleArchiver: ArticleArchiver() {
    override fun match(url: String): Boolean {
        val uri = Uri.parse(url)
        if (uri.authority?.contains("juejin.cn") == true && uri.path?.contains("post") == true) {
            Timber.i("匹配url %s 为掘金文章", url)
            return true
        }
        return false
    }

    override fun handle(url: String, html: String): File? {
        val document = Jsoup.parse(html)

        val html = document.selectFirst("div.main-area>article")?.also {
            it.select("div.code-block-extension-header").remove()
        }?.outerHtml()

        if (html.isNullOrBlank()) {
            return null
        }

        val output = """ // todo 图像嵌入，代码高亮
            <html>
            <head>
            <title>文件导出</title>
            </head>
            <body>
            $html
            </body>
            </html>
        """.trimIndent()

        val file = File.createTempFile("wanAndroid", ".html")
        file.deleteOnExit()
        file.outputStream().use {
            it.write(output.encodeToByteArray())
        }
        return file
    }
}