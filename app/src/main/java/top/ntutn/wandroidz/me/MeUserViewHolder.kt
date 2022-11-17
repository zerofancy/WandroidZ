package top.ntutn.wandroidz.me

import android.widget.Toast
import com.bumptech.glide.Glide
import org.apache.commons.codec.digest.DigestUtils
import top.ntutn.wandroidz.R
import top.ntutn.wandroidz.account.ui.login.LoginActivity
import top.ntutn.wandroidz.databinding.ItemMeUserBinding
import top.ntutn.wandroidz.me.data.AvatarData
import top.ntutn.wandroidz.me.data.IMeListItemData

class MeUserViewHolder(val binding: ItemMeUserBinding): MeAdapter.ViewHolder(binding.root) {
    override fun onBind(data: IMeListItemData) {
        if (data !is AvatarData) {
            return
        }
        val email = data.userInfoBean?.userInfo?.email
        val emailHash = DigestUtils.md5Hex(email)
        val url = "https://www.gravatar.com/avatar/$emailHash"

        binding.nickname.text = data.userInfoBean?.userInfo?.nickname ?: "未登录"
        binding.root.setOnClickListener {
            if (data.userInfoBean?.userInfo != null) {
                Toast.makeText(it.context, "已登陆", Toast.LENGTH_SHORT).show()
            } else {
                LoginActivity.actionStart(it.context)
            }
        }
        Glide.with(binding.avatar)
            .load(url)
            .error(R.mipmap.ic_launcher_round)
            .circleCrop()
            .into(binding.avatar)
    }
}