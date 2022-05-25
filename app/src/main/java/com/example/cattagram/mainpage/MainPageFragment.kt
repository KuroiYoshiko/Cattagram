package com.example.cattagram.mainpage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cattagram.databinding.FragmentMainPageBinding
import com.example.cattagram.login.LoginActivity
import com.example.cattagram.profile.ProfileActivity
import com.example.cattagram.search.SearchActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainPageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)

        _binding!!.menuBottom.ivProfile.setOnClickListener {
            startActivity(Intent(activity, ProfileActivity::class.java))
        }

        _binding!!.menuBottom.ivHome.setOnClickListener {
            startActivity(Intent(activity, MainPageActivity::class.java))
            requireActivity().finish()
        }

        _binding!!.menuBottom.ivAdd.setOnClickListener {
            startActivity(Intent(activity, MainPageActivity::class.java))
            requireActivity().finish()
        }

        _binding!!.menuBottom.ivSearch.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
            requireActivity().finish()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}