package top.ntutn.wandroidz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.ntutn.wandroidz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindView()
        observeModel()
        initData()
    }

    private fun bindView() {

    }

    private fun observeModel() {

    }

    private fun initData() {

    }
}