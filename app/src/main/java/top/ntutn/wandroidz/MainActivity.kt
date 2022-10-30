package top.ntutn.wandroidz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import top.ntutn.wandroidz.databinding.ActivityMainBinding
import top.ntutn.wandroidz.vm.MainViewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        MainListAdapter()
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
        binding.mainList.adapter = adapter
        binding.mainList.layoutManager = LinearLayoutManager(this)

        binding.refreshLayout.setOnRefreshListener {
            mainViewModel.refresh()
        }
    }

    private fun observeModel() {
        mainViewModel.currentState.onEach {
            binding.refreshLayout.isRefreshing = it == MainViewModel.State.LOADING
        }.launchIn(lifecycleScope)
        mainViewModel.datas.onEach {
            adapter.submitList(it)
        }.launchIn(lifecycleScope)
    }

    private fun initData() {
        mainViewModel.refresh()
    }
}