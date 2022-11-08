package top.ntutn.wandroidz

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEachIndexed
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import top.ntutn.wandroidz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val backToRecommendCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            binding.mainViewpager2.currentItem = 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindView()
        observeModel()
        initData()
    }

    private fun bindView() {
        onBackPressedDispatcher.addCallback(this, backToRecommendCallback)
        binding.mainViewpager2.adapter = MainPagerAdapter(this)
        binding.mainViewpager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigationView.menu.getItem(position).isChecked = true
                // 当前不在首页，回到首页
                backToRecommendCallback.isEnabled = position != 0
            }
        })
        binding.mainViewpager2.isUserInputEnabled = false
        binding.bottomNavigationView.setOnItemSelectedListener {
            binding.bottomNavigationView.menu.forEachIndexed { index, item ->
                if (it == item) {
                    binding.mainViewpager2.currentItem = index
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun observeModel() {

    }

    private fun initData() {

    }
}