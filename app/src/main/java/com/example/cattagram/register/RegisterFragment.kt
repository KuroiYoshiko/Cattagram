package com.example.cattagram.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cattagram.databinding.FragmentRegisterBinding
import com.example.cattagram.login.LoginActivity
import com.example.cattagram.retrofit.Api
import okhttp3.*
import okio.Buffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegisterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private fun registerRequest(): Api? {

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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val textWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _binding!!.btnRegister.isEnabled = _binding!!.etEmail.text.toString().isNotEmpty()
                        && _binding!!.etPassword.text.toString().isNotEmpty()
                        && _binding!!.etPassword2.text.toString().isNotEmpty()
                        && _binding!!.etLogin.text.toString().isNotEmpty()
                        && _binding!!.etBirthdate.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        _binding!!.btnRegister.isEnabled = false

        _binding!!.etEmail.addTextChangedListener(textWatcher)
        _binding!!.etPassword.addTextChangedListener(textWatcher)
        _binding!!.etPassword2.addTextChangedListener(textWatcher)
        _binding!!.etLogin.addTextChangedListener(textWatcher)

        _binding!!.btnRegister.setOnClickListener {

            if (_binding!!.etPassword.text.toString() == _binding!!.etPassword2.text.toString()) {
                val retrofit = registerRequest()
                val username = _binding!!.etEmail.text.toString()
                val password = _binding!!.etPassword.text.toString()
                val email = _binding!!.etEmail.text.toString()
                val birthdate = _binding!!.etBirthdate.text.toString()

                val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .addFormDataPart("email", email)
                    .addFormDataPart("birthdate", birthdate)
                    .build()

                retrofit?.registerRequest(requestBody)?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val responseBodyString = response.body()!!.string()

                       when (responseBodyString) {
                            "null" -> {
                                Toast.makeText(activity, "Username with this login or email already exists", Toast.LENGTH_SHORT).show()
                            }
                            "{\"response\":200}" -> {
                                startActivity(Intent(activity, LoginActivity::class.java))
                                requireActivity().finish()
                            }
                            else -> {
                                Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(activity, "Server problems, try again later", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else {
                Toast.makeText(activity, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }



        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}