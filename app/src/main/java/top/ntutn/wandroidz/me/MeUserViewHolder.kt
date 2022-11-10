package top.ntutn.wandroidz.me

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.apache.commons.codec.digest.DigestUtils
import top.ntutn.wandroidz.R
import top.ntutn.wandroidz.account.ui.login.LoginActivity
import top.ntutn.wandroidz.databinding.ItemMeUserBinding

class MeUserViewHolder(val binding: ItemMeUserBinding): RecyclerView.ViewHolder(binding.root) {
    fun onBind() {
        // todo email换掉
        val email = "ntutn.top@gmail.com"
        val emailHash = DigestUtils.md5Hex(email)
        val url = "https://www.gravatar.com/avatar/$emailHash"

        binding.nickname.text = "未登录"
        binding.root.setOnClickListener {
            LoginActivity.actionStart(it.context)
        }
        Glide.with(binding.avatar)
            .load(url)
            .error(R.mipmap.ic_launcher_round)
            .circleCrop()
            .into(binding.avatar)
    }
}