package br.com.fiap.innov8tech.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.fiap.innov8tech.R
import br.com.fiap.innov8tech.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        checkUserStatus()

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_login)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_register)
        }

        binding.logoutButton.setOnClickListener {
            logoutUser()
        }

        return binding.root
    }

    private fun checkUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.logoutButton.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
            binding.registerButton.visibility = View.GONE
        } else {
            binding.logoutButton.visibility = View.GONE
            binding.loginButton.visibility = View.VISIBLE
            binding.registerButton.visibility = View.VISIBLE
        }
    }

    private fun logoutUser() {
        auth.signOut()
        checkUserStatus()
        Toast.makeText(context, "Você saiu da conta", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        checkUserStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
