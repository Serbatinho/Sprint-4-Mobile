package br.com.fiap.innov8tech.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.innov8tech.databinding.FragmentDashboardBinding
import br.com.fiap.innov8tech.R

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        reportAdapter = ReportAdapter(
            reports = listOf(),
            onDetailsClicked = { report ->
                val bundle = Bundle()
                bundle.putString("twitterHandle", report.twitterHandle)
                bundle.putString("reportContent", report.fileName)
                bundle.putString("fileUrl", report.fileUrl)
                findNavController().navigate(R.id.reportDetailsFragment, bundle)
            }
        )
        binding.reportList.layoutManager = LinearLayoutManager(requireContext())
        binding.reportList.adapter = reportAdapter

        dashboardViewModel.reports.observe(viewLifecycleOwner) { reports ->
            reportAdapter.updateReports(reports)
        }

        checkUserStatus()

        binding.newReportButton.setOnClickListener {
            val twitterHandle = binding.twitterHandleInput.text.toString()
            if (twitterHandle.isNotBlank()) {
                dashboardViewModel.requestNewReport(twitterHandle)
                Toast.makeText(requireContext(), "Relatório solicitado para @$twitterHandle", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Por favor, insira um @conta válido", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun checkUserStatus() {
        val currentUser = dashboardViewModel.getCurrentUser()
        if (currentUser != null) {
            binding.twitterHandleInput.visibility = View.VISIBLE
            binding.newReportButton.visibility = View.VISIBLE
            binding.notLoggedInMessage.visibility = View.GONE
        } else {
            dashboardViewModel.clearReports()
            binding.twitterHandleInput.visibility = View.GONE
            binding.newReportButton.visibility = View.GONE
            binding.notLoggedInMessage.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
