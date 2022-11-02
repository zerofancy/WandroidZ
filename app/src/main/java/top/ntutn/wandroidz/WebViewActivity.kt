package top.ntutn.wandroidz

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.text.StringEscapeUtils
import timber.log.Timber
import top.ntutn.wandroidz.databinding.ActivityWebviewBinding
import top.ntutn.wandroidz.smartavatar.AvatarHelper
import top.ntutn.wandroidz.smartavatar.JuejinSniffer

class WebViewActivity: AppCompatActivity() {
    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_AUTHOR = "key_author"

        fun actionStart(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            context.startActivity(intent)
        }

        fun showArticle(context: Context, author: String?, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_AUTHOR, author)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityWebviewBinding
    private lateinit var webView: WebView
    private var author: String? = null
    private val sniffer by lazy {
        JuejinSniffer(null)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        webView = binding.webview
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)

        author = intent.getStringExtra(KEY_AUTHOR)
        val linkUrl = intent?.extras?.getString(KEY_URL)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view ?: return, url)
                Timber.d("页面 %s 加载完成", url)
                binding.myToolbar.title = view.title
                val user = author ?: return
                if (user.isBlank()) {
                    return
                }
                if (Uri.parse(url ?: return).path != Uri.parse(linkUrl ?: return).path) {
                    return
                }
                // 查找页面加载头像

                view.evaluateJavascript("new XMLSerializer().serializeToString(document)") {
                    lifecycleScope.launch(Dispatchers.Default) {
                        val html = StringEscapeUtils.unescapeJava(it)
                        val avatarUrl = sniffer.sniff(url, html)
                        if (!avatarUrl.isNullOrBlank()) {
                            Timber.d("用户 %s 的头像解析成功 %s", user, avatarUrl)
                            AvatarHelper.saveAvatarUrl(user, avatarUrl)
                        }
                    }
                }

                author = null
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_share -> {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, binding.webview.url)
            startActivity(Intent.createChooser(shareIntent, "分享到..."))
            true
        }
        else -> false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.webview_toolbar, menu)
        return true
    }
}