package top.ntutn.wandroidz

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.text.StringEscapeUtils
import timber.log.Timber
import top.ntutn.wandroidz.smartavatar.AvatarHelper
import top.ntutn.wandroidz.smartavatar.JuejinSniffer

class WebViewActivity: AppCompatActivity() {
    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_SHARE_USER = "key_share_user"

        fun actionStart(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            context.startActivity(intent)
        }

        fun showArticle(context: Context, shareUser: String?, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_SHARE_USER, shareUser)
            context.startActivity(intent)
        }
    }

    private lateinit var webView: WebView
    private var shareUser: String? = null
    private val sniffer by lazy {
        JuejinSniffer(null)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)

        shareUser = intent.getStringExtra(KEY_SHARE_USER)
        val linkUrl = intent?.extras?.getString(KEY_URL)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Timber.d("页面 %s 加载完成", url)
                val user = shareUser ?: return
                if (user.isBlank()) {
                    return
                }
                if (Uri.parse(url ?: return).path != Uri.parse(linkUrl ?: return).path) {
                    return
                }
                // 查找页面加载头像

                view?.evaluateJavascript("new XMLSerializer().serializeToString(document)") {
                    // todo 寻找头像
                    lifecycleScope.launch(Dispatchers.Default) {
                        val html = StringEscapeUtils.unescapeJava(it)
                        val avatarUrl = sniffer.sniff(url, html)
                        if (!avatarUrl.isNullOrBlank()) {
                            Timber.d("用户 %s 的头像解析成功 %s", user, avatarUrl)
                            AvatarHelper.saveAvatarUrl(user, avatarUrl)
                        }
                    }
                } ?: return

                shareUser = null
            }
        }
        webView.settings.apply {
            javaScriptEnabled = true
        }

        linkUrl?.let {
            webView.loadUrl(it)
        }

        val webViewBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(webViewBackPressedCallback)
    }
}