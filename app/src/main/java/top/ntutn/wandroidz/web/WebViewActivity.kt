package top.ntutn.wandroidz.web

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.text.StringEscapeUtils
import timber.log.Timber
import top.ntutn.wandroidz.BuildConfig
import top.ntutn.wandroidz.R
import top.ntutn.wandroidz.databinding.ActivityWebviewBinding
import top.ntutn.wandroidz.smartavatar.AvatarHelper
import top.ntutn.wandroidz.smartavatar.CSDNSniffer
import top.ntutn.wandroidz.smartavatar.JuejinSniffer
import top.ntutn.wandroidz.smartavatar.WeChatSniffer
import java.io.File
import java.net.URLConnection
import java.util.UUID

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
        CSDNSniffer(WeChatSniffer(JuejinSniffer(null)))
    }
    private val articleArchivers = listOf(JuejinArticleArchiver())
    private val blocker = BuiltinURLBlocker()
    private var loadingFinished = false

    private var currentArticleFile: File? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linkUrl = intent?.extras?.getString(KEY_URL)

        if (blocker.check(linkUrl) != URLBlocker.Result.PASS) {
            Toast.makeText(applicationContext, "请求地址被屏蔽", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding = ActivityWebviewBinding.inflate(layoutInflater)
        webView = binding.webview
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)

        author = intent.getStringExtra(KEY_AUTHOR)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingFinished = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view ?: return, url)
                Timber.d("页面 %s 加载完成", url)

                loadingFinished = true

                binding.myToolbar.title = view.title

                url ?: return

                view.evaluateJavascript("new XMLSerializer().serializeToString(document)") {
                    // 查找页面加载头像
                    lifecycleScope.launch(Dispatchers.Default) {
                        val user = author ?: return@launch
                        if (user.isBlank()) {
                            return@launch
                        }
                        if (Uri.parse(url).path != Uri.parse(linkUrl ?: return@launch).path) {
                            return@launch
                        }
                        val html = StringEscapeUtils.unescapeJava(it)
                        val avatarUrl = sniffer.sniff(url, html)
                        if (!avatarUrl.isNullOrBlank()) {
                            Timber.d("用户 %s 的头像解析成功 %s", user, avatarUrl)
                            AvatarHelper.saveAvatarUrl(user, avatarUrl)
                        }
                    }
                    // 生成可导出文档
                    lifecycleScope.launch(Dispatchers.Default) {
                        val html = StringEscapeUtils.unescapeJava(it)
                        articleArchivers.firstOrNull { it.match(url) }?.let {
                            currentArticleFile = it.handle(url, html)
                        }
                    }
                }

                author = null
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Timber.e(error.toString())
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                Timber.e(error.toString())
                super.onReceivedSslError(view, handler, error)
            }
        }
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
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
        R.id.action_copy -> {
            val manager = getSystemService<ClipboardManager>()
            manager?.setPrimaryClip(ClipData.newPlainText("WanAndroid", binding.webview.url))
            Snackbar.make(binding.root, "已复制", Snackbar.LENGTH_SHORT).show()
            true
        }
        R.id.action_share -> {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, binding.webview.url)
            startActivity(Intent.createChooser(shareIntent, "分享到..."))
            true
        }
        R.id.action_save -> { // 能保存，但好像没啥用
            File(filesDir, "archive").mkdirs()
            if (!loadingFinished) {
                Toast.makeText(this, "页面未加载完毕，无法保存", Toast.LENGTH_SHORT).show()
            } else if (currentArticleFile != null) {
                currentArticleFile?.let {
                    val archiveFile = File(filesDir, "archive/${UUID.randomUUID()}.${it.extension}")
                    it.copyTo(archiveFile)
                    shareFile(archiveFile)
                }
            } else{
                val archiveFile = File(filesDir, "archive/${UUID.randomUUID()}.mht")
                binding.webview.saveWebArchive(archiveFile.absolutePath, false) { value ->
                    if (value == null) {
                        Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()
                        return@saveWebArchive
                    }
                    shareFile(archiveFile)
                }
            }
            true
        }
        else -> false
    }

    private fun shareFile(archiveFile: File) {
        val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", archiveFile)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "*/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        //shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(Intent.createChooser(shareIntent, "Send"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.webview_toolbar, menu)
        return true
    }
}