package top.ntutn.wandroidz.me

import androidx.recyclerview.widget.RecyclerView
import top.ntutn.wandroidz.account.ui.login.LoginActivity
import top.ntutn.wandroidz.databinding.ItemMeUserBinding

class MeUserViewHolder(val binding: ItemMeUserBinding): RecyclerView.ViewHolder(binding.root) {
    fun onBind() {
        binding.root.setOnClickListener {
            LoginActivity.actionStart(it.context)
        }
    }
}