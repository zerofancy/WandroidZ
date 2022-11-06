package top.ntutn.wandroidz

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import top.ntutn.wandroidz.mainpage.RecommendFragment
import top.ntutn.wandroidz.me.MeFragment

class MainPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RecommendFragment()
            1 -> MeFragment()
            else -> throw NotImplementedError()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}