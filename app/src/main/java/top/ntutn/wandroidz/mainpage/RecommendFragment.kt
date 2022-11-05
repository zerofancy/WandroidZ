package top.ntutn.wandroidz.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import top.ntutn.wandroidz.databinding.FragmentRecommendBinding
import top.ntutn.wandroidz.vm.RecommendViewModel

class RecommendFragment: Fragment() {
    private var _binding: FragmentRecommendBinding? = null
    private val binding: FragmentRecommendBinding get() =  _binding!!
    private val recommendViewModel by viewModels<RecommendViewModel>()
    private val adapter by lazy {
        RecommendListAdapter(recommendViewModel::loadMore)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        observeModel()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindView() {
        binding.mainList.adapter = adapter
        binding.mainList.layoutManager = LinearLayoutManager(requireContext())

        binding.refreshLayout.setOnRefreshListener {
            recommendViewModel.refresh()
        }
    }

    private fun observeModel() {
        recommendViewModel.currentState.onEach {
            binding.refreshLayout.isRefreshing = it in listOf(RecommendViewModel.State.LOADING, RecommendViewModel.State.REFRESHING)
        }.launchIn(lifecycleScope)
        recommendViewModel.datas.onEach {
            val recyclerViewState = binding.mainList.layoutManager?.onSaveInstanceState()
            adapter.submitList(it) {
                lifecycleScope.launchWhenResumed {
                    recyclerViewState?.let {
                        binding.mainList.layoutManager?.onRestoreInstanceState(it)
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun initData() {
        recommendViewModel.refresh()
    }
}