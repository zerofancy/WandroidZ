package top.ntutn.wandroidz.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import top.ntutn.wandroidz.databinding.FragmentMeBinding

class MeFragment: Fragment() {
    private var _binding: FragmentMeBinding? = null
    private val binding: FragmentMeBinding get() = _binding!!

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
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MeAdapter(object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return false
            }
        })
        binding.recyclerView.adapter = adapter
        adapter.submitList(listOf("title"))
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}