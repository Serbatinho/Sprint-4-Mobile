package br.com.fiap.innov8tech.ui.forgotpass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.fiap.innov8tech.databinding.FragmentForgotPassBinding

class ForgotPassFragment : Fragment() {

    private var _binding: FragmentForgotPassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPassBinding.inflate(inflater, container, false)

        // Aqui você pode adicionar a lógica para enviar o email de recuperação

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
