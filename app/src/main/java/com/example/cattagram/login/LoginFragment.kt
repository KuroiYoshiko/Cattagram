package com.example.cattagram.login

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cattagram.databinding.FragmentLoginBinding
import com.example.cattagram.mainpage.MainPageActivity
import com.example.cattagram.register.RegisterActivity
import com.example.cattagram.retrofit.Api
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private fun loginRequest(): Api? {

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("accept", "application / json")
                .addHeader("Authorization", "Bearer 1234567asdfgh")
                .build()
            chain.proceed(newRequest)
        }.build()

        val retrofitBuilder = Retrofit.Builder()
            .client(client)
            .baseUrl("http://158.101.165.232:7000/")
            .build()
            .create(Api::class.java)


        return retrofitBuilder
    }


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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        val textWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _binding!!.btnLogin.isEnabled = _binding!!.etEmail.text.toString().isNotEmpty()
                        && _binding!!.etPassword.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        _binding!!.btnLogin.isEnabled = false

        _binding!!.etEmail.addTextChangedListener(textWatcher)

        _binding!!.etPassword.addTextChangedListener(textWatcher)

        _binding!!.btnLogin.setOnClickListener {
            val retrofit = loginRequest()
                val username = _binding!!.etEmail.text.toString()
                val password = _binding!!.etPassword.text.toString()
                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .build()

                retrofit?.loginRequest(requestBody)?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val responseBodyString = response.body()!!.string()

                        if (responseBodyString=="null") {
                            Toast.makeText(activity, "Wrong username and/or password", Toast.LENGTH_SHORT).show()
                        }
                        else if (responseBodyString == "[128]") {
                            startActivity(Intent(activity, MainPageActivity::class.java))
                            requireActivity().finish()
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    }
            })

        }

        _binding!!.btnSingUp.setOnClickListener {
            startActivity(Intent(activity, RegisterActivity::class.java));
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}