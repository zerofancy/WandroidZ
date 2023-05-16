package top.ntutn.wandroidz.web

import java.io.File

abstract class ArticleArchiver {
    abstract fun match(url: String): Boolean

    abstract fun handle(url: String, html: String): File?
}