package br.com.fiap.innov8tech.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.fiap.innov8tech.databinding.FragmentReportDetailsBinding

class ReportDetailsFragment : Fragment() {

    private var _binding: FragmentReportDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportDetailsBinding.inflate(inflater, container, false)

        // Obtém o twitterHandle do bundle
        val twitterHandle = arguments?.getString("twitterHandle")

        // Atualiza a UI com os dados do relatório
        binding.reportDetailsTextView.text = "Detalhes do relatório para @$twitterHandle:\nRelatório gerado automaticamente."

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
