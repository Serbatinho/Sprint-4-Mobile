package br.com.fiap.innov8tech.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.innov8tech.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var adapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurando o RecyclerView com o Adapter
        adapter = NotificationsAdapter(listOf())
        binding.completedReportsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.completedReportsRecyclerView.adapter = adapter

        // Observa relatórios concluídos e atualiza o RecyclerView
        notificationsViewModel.completedReports.observe(viewLifecycleOwner) { reports ->
            adapter.updateReports(reports)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
