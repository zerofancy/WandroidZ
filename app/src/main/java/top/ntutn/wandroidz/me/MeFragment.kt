package top.ntutn.wandroidz.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import top.ntutn.wandroidz.databinding.FragmentMeBinding
import top.ntutn.wandroidz.me.data.IMeListItemData

class MeFragment: Fragment() {
    private var _binding: FragmentMeBinding? = null
    private val binding: FragmentMeBinding get() = _binding!!
    private var adapter: MeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMeBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MeAdapter(object : DiffUtil.ItemCallback<IMeListItemData>() {
            override fun areItemsTheSame(
                oldItem: IMeListItemData,
                newItem: IMeListItemData
            ): Boolean {
                return oldItem.javaClass == newItem.javaClass
            }

            override fun areContentsTheSame(
                oldItem: IMeListItemData,
                newItem: IMeListItemData
            ): Boolean {
                return oldItem.contentEquals(newItem)
            }
        })
        binding.recyclerView.adapter = adapter
        bindData()
    }

    private fun bindData() {
        val vm by viewModels<MeVM>()
        vm.mainList.onEach {
            adapter?.submitList(it)
        }.launchIn(lifecycleScope)

        vm.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}